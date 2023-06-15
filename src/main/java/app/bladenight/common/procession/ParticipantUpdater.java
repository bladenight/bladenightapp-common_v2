package app.bladenight.common.procession;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import app.bladenight.common.keyvaluestore.KeyValueStore;
import app.bladenight.common.routes.Route.ProjectedLocation;
import app.bladenight.common.time.Clock;
import app.bladenight.common.time.SystemClock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Updates a participant based on his previous location, the route, and other dynamic values.
 * Typically called when the device of the participant calls in to update its position.
 */
public class ParticipantUpdater {

    static public class Builder {

        Builder() {
            updater = new ParticipantUpdater();
        }

        Builder setPotentialLocations(List<ProjectedLocation> potentialLocations) {
            updater.setPotentialLocations(potentialLocations);
            return this;
        }
        Builder setParticipant(Participant participant) {
            updater.setParticipant(participant);
            return this;
        }
        Builder setParticipantInput(ParticipantInput participantInput) {
            updater.setParticipantInput(participantInput);
            return this;
        }
        Builder setProcessionEnds(double tailPosition, double headPosition) {
            updater.setProcessionEnds(tailPosition, headPosition);
            return this;
        }
        Builder setRouteLength(double routeLength) {
            updater.setRouteLength(routeLength);
            return this;
        }
        Builder setClock(Clock clock) {
            updater.setClock(clock);
            return this;
        }
        ParticipantUpdater build() {
            return updater;
        }

        ParticipantUpdater updater;
    }

    public ParticipantUpdater() {

    }

    void setPotentialLocations(List<ProjectedLocation> potentialLocations) {
        this.potentialLocations = potentialLocations;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    void setParticipantInput(ParticipantInput participantInput) {
        this.participantInput = participantInput;
    }

    public void setProcessionEnds(double tailPosition, double headPosition) {
        this.processionTailPosition = tailPosition;
        this.processionHeadPosition = headPosition;
    }

    public void setRouteLength(double routeLength) {
        this.routeLength = routeLength;
    }


    public boolean updateParticipant() {

        long timestamp = clock.currentTimeMillis();

        participant.setLastLifeSign(timestamp);

        String deviceId = participantInput.getParticipantId();

        //      if ( input.getTimestamp() == 0 ) {
        //          getLog().debug("User doesn't have a proper fix.");
        //          return true;
        //      }

        ProjectedLocation locationOnRoute = findBestNewLocationOnRoute();

        MovingPoint point = participant.getLastKnownPoint();

        point.setAccuracy(participantInput.getAccuracy());
        if ( locationOnRoute != null ) {
            getLog().trace("update: findBestNewLocationOnRoute("+deviceId+") returned " + locationOnRoute.linearPosition);
            point.update(participantInput.getLatitude(), participantInput.getLongitude(), locationOnRoute.linearPosition);
            point.isOnRoute(true);
            point.isInProcession(true);
            point.setRealSpeed(participantInput.getRealSpeed());
        }
        else {
            getLog().trace("Position on the route couldn't be determined");
            point.setLatitude(participantInput.getLatitude());
            point.setLongitude(participantInput.getLongitude());
            point.setRealSpeed(participantInput.getRealSpeed());
            point.isOnRoute(false);
            point.isInProcession(false);
            point.setTimestamp(point.getTimestamp());
        }

        return true;
    }

    protected ProjectedLocation findBestNewLocationOnRoute() {


        if ( potentialLocations.size() == 0 ) {
            getLog().debug("findBestNewLocationOnRoute: User is out of corridor !");
            return null;
        }

        for ( ProjectedLocation l : potentialLocations ) {
            l.evaluation = 1.0;
            l.evaluation *= evaluateCandidateOnDistanceToSegment(l);
            l.evaluation *= evaluateCandidateOnDistanceToStart(l);
            l.evaluation *= evaluateCandidateOnDistanceToPrevious(l);
            l.evaluation *= evaluateCandidateOnDistanceToProcession(l);

            // TODO improve the algorithm to make this hack unnecessary
            if ( l.linearPosition < minLinearPosition )
                l.evaluation = 0;
        }


        // Sort by evaluation:
        Collections.sort(potentialLocations, new Comparator<ProjectedLocation>() {
            public int compare(ProjectedLocation o1, ProjectedLocation o2) {
                return (int)Math.signum(o2.evaluation - o1.evaluation);
            }
        }
                );


        getLog().debug("Evaluated potential new locations for " + participant.getDeviceId());
        for ( ProjectedLocation l : potentialLocations ) {
            getLog().debug("pos="+l.linearPosition);
            getLog().debug("  dist="+l.distanceToSegment);
            getLog().debug("  eval="+l.evaluation);
        }

        return potentialLocations.get(0);
    }

    protected double evaluateCandidateOnDistanceToSegment(ProjectedLocation candidate) {
        final double referenceDistance = 100.0;
        final double referenceEvaluation = 0.8;
        final double alpha = ( 1 - referenceEvaluation ) / ( referenceDistance * referenceEvaluation);

        double evaluation = capEvaluation(1.0 / ( 1 + alpha * candidate.distanceToSegment));
        getLog().debug("evaluateCandidateOnDistanceToSegment: "+evaluation);
        return evaluation;
    }

    protected double evaluateCandidateOnDistanceToStart(ProjectedLocation candidate) {
        double evaluation = capEvaluation(1.0 -  0.5 * candidate.linearPosition / routeLength);
        getLog().debug("evaluateCandidateOnDistanceToStart: "+evaluation);
        return capEvaluation(evaluation);
    }

    protected double evaluateCandidateOnDistanceToPrevious(ProjectedLocation candidate) {

        if ( ! participant.getLastKnownPoint().isOnRoute() )
            return 1.0;

        getLog().debug("evaluateCandidateOnDistanceToPrevious: several segments available, and previous position is known " + participant.getLinearPosition());

        double evaluation = 1.0;
        if ( candidate.linearPosition < participant.getLinearPosition() ) {
            evaluation =  1.0 - ( participant.getLinearPosition() - candidate.linearPosition  ) / routeLength;
            evaluation = Math.pow(evaluation,3.0);
        }

        getLog().debug("evaluateCandidateOnDistanceToPrevious: "+evaluation);
        return evaluation;
    }

    protected double evaluateCandidateOnDistanceToProcession(ProjectedLocation candidate) {
        if ( ! hasValidProcession() ) {
            getLog().debug("evaluateCandidateOnDistanceToProcession: no valid procession data available ("+processionTailPosition+"/"+processionHeadPosition+")");
            return 1.0;
        }

        getLog().debug("evaluateCandidateOnDistanceToProcession: procession position is known ( "+processionHeadPosition+" / " + processionTailPosition + " )");

        double processionLength = (processionHeadPosition - processionTailPosition);
        double processionMiddle = (processionTailPosition + processionHeadPosition)/2.0;
        double distanceToMiddle = Math.abs(processionMiddle - candidate.linearPosition);

        if ( distanceToMiddle < processionLength * 1.2)
            // Boost candidates that are in the procession or almost:
            return 2.0;

        final double referenceDistance = processionLength * 1.5;
        final double referenceEvaluation = 0.1;
        final double alpha = ( 1 - referenceEvaluation ) / ( referenceDistance * referenceEvaluation);
        getLog().debug("evaluateCandidateOnDistanceToProcession: alpha="+alpha);
        double evaluation = capEvaluation(1.0 / ( 1 + alpha * distanceToMiddle));
        getLog().debug("evaluateCandidateOnDistanceToProcession: "+evaluation);
        return evaluation;
    }

    public boolean hasValidProcession() {
        return processionHeadPosition > processionTailPosition;
    }

    public boolean hasValidRouteLength() {
        return routeLength > 0.0;
    }

    protected double capEvaluation(double evaluation) {
        if ( evaluation < 0.0 )
            return 0.0;
        else if ( evaluation > 1.0 )
            return 1.0;
        else
            return evaluation;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }


    private static Logger log;

    public static void setLog(Logger log) {
        ParticipantUpdater.log = log;
    }

    protected static Logger getLog() {
        if (log == null)
            log = LogManager.getLogger(ParticipantUpdater.class.getName());
        return log;
    }

    private Participant participant;
    private ParticipantInput participantInput;
    private List<ProjectedLocation> potentialLocations;
    private double processionTailPosition;
    private double processionHeadPosition;
    private double routeLength;
    private Clock clock = new SystemClock();

    // TODO simple but effective hack to bring the algorithm on track when history is lost (server restart)
    public static double minLinearPosition = 0.0;

}

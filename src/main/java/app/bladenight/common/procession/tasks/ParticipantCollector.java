package app.bladenight.common.procession.tasks;

import java.util.HashMap;
import java.util.Map;


import app.bladenight.common.keyvaluestore.KeyValueStore;
import app.bladenight.common.math.MedianFinder;
import app.bladenight.common.procession.Participant;
import app.bladenight.common.time.Clock;
import app.bladenight.common.time.SystemClock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ParticipantCollector implements Runnable {
    public ParticipantCollector(ParticipantCollectorClient procession) {
        this.procession = procession;
    }

    public void setMaxRelativeAgeFactor(double maxRelativeAgeFactor) {
        this.maxRelativeAgeFactor = maxRelativeAgeFactor;
    }

    public void setMaxAbsoluteAge(long maxAbsoluteAge) {
        this.maxAbsoluteAge = maxAbsoluteAge;
    }

    public boolean hasMaxRelativeAge() {
        return maxRelativeAgeFactor > 0.0;
    }

    public boolean hasMaxAbsoluteAge() {
        return maxAbsoluteAge > 0.0;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public boolean hasPeriod() {
        return period > 0;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }


    @Override
    public void run() {
        boolean cont = true;
        if (!hasPeriod())
            throw new IllegalStateException("Period has not been set");

        while (cont) {
            collect();
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                cont = false;
            }
        }
    }

    public void collect() {
        long meanAge = 0;

        long referenceTime = clock.currentTimeMillis();

        if (hasMaxRelativeAge()) {
            meanAge = getMeanParticipantUpdatePeriod();
            getLog().trace("meanAge=" + meanAge);
        }

        for (Participant p : procession.getParticipants()) {
            long age = getAge(p, referenceTime);
            if (hasMaxRelativeAge() && age > maxRelativeAgeFactor * meanAge) {
                getLog().info("Removing participant " + p.getDeviceId() + " : " + age + " > " + maxRelativeAgeFactor + " * " + meanAge);
                procession.removeParticipant(p.getDeviceId());
            }
            if (hasMaxAbsoluteAge() && age > maxAbsoluteAge) {
                getLog().info("Removing participant " + p.getDeviceId() + " : " + age + " > " + maxAbsoluteAge);
                procession.removeParticipant(p.getDeviceId());
            }
        }
    }

    private long getAge(Participant p, long referenceTime) {
        long diff = referenceTime - p.getLastLifeSign();
        if (diff < 0)
            return 0;
        return diff;
    }

    private long getMeanParticipantUpdatePeriod() {
        Map<Long, Long> counter = new HashMap<Long, Long>();
        for (Participant p : procession.getParticipants()) {
            Long age = clock.currentTimeMillis() - p.getLastLifeSign();
            Long currentCount = counter.get(age);
            if (currentCount == null) {
                currentCount = 0L;
            }
            counter.put(age, currentCount + 1);
        }
        MedianFinder medianFinder = new MedianFinder();
        for (Long age : counter.keySet()) {
            medianFinder.addWeightedValue(age, counter.get(age));
        }
        if (medianFinder.sampleCount() == 0)
            return 0;
        return (long) medianFinder.findMedian();
    }


    private static Logger log;

    public static void setLog(Logger log) {
        ParticipantCollector.log = log;
    }

    protected static Logger getLog() {
        if (log == null)
            log = LogManager.getLogger(ParticipantCollector.class.getName());
        return log;
    }

    private double maxRelativeAgeFactor;
    private long maxAbsoluteAge;
    private ParticipantCollectorClient procession;
    private long period;
    private Clock clock = new SystemClock();

}

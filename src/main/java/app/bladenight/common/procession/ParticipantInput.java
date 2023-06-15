package app.bladenight.common.procession;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ParticipantInput {
    public ParticipantInput(String participantId, boolean isParticipating, double lat, double lon, double realSpeed) {
        this.participantId = participantId;
        this.isParticipating = isParticipating;
        this.latitude = lat;
        this.longitude = lon;
        this.realSpeed = realSpeed;
    }

    public ParticipantInput(String participantId, boolean isParticipating, double lat, double lon, int accuracy, double realSpeed) {
        this.participantId = participantId;
        this.isParticipating = isParticipating;
        this.latitude = lat;
        this.longitude = lon;
        this.accuracy = accuracy;
        this.realSpeed = realSpeed;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getRealSpeed() {
        return realSpeed;
    }

    public void setRealSpeed(double realSpeed) {
        this.realSpeed = realSpeed;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void isParticipating(boolean isParticipating) {
        this.isParticipating = isParticipating;
    }

    public boolean isParticipating() {
        return isParticipating;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    private double latitude, longitude;
    private String participantId;
    private boolean isParticipating;
    private double accuracy;
    private double realSpeed;
}

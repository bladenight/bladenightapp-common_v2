package app.bladenight.common.procession;

import app.bladenight.common.time.Clock;
import app.bladenight.common.time.SystemClock;
import org.apache.commons.lang3.builder.ToStringBuilder;

// Representation of an (optionally) moving point in the procession
public final class MovingPoint {
    private double latitude;
    private double longitude;
    private double accuracy;
    private boolean isOnRoute;
    private boolean isInProcession;
    private double linearPosition;
    private double realSpeed;
    private double linearSpeed;
    private long timestamp; // in ms
    private Clock clock;

    public MovingPoint() {
        clock = new SystemClock();
        init();
    }

    public MovingPoint(Clock clock) {
        this.clock = clock;
        init();
    }

    private void init() {
        isOnRoute = false;
        isInProcession = false;
        timestamp = clock.currentTimeMillis();
    }

    public void setLatLong(double lat, double lon) {
        latitude = lat;
        longitude = lon;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLinearPosition() {
        return linearPosition;
    }

    public void setLinearPosition(double linearPosition) {
        this.linearPosition = linearPosition;
    }

    public void update(double latitude, double longitude, double newLinearPosition) {
        long newTimestamp = clock.currentTimeMillis();
        updateLinearSpeed(newLinearPosition, newTimestamp);
        this.timestamp = newTimestamp;
        this.linearPosition = newLinearPosition;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLinearSpeed() {
        return linearSpeed;
    }

    public void setLinearSpeed(double speed) {
        this.linearSpeed = speed;
    }

    public double getRealSpeed() {
        return realSpeed;
    }

    public void setRealSpeed(double realSpeed) {
        this.realSpeed = realSpeed;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isOnRoute() {
        return isOnRoute;
    }

    public void isOnRoute(boolean isOnRoute) {
        this.isOnRoute = isOnRoute;
    }

    public boolean isInProcession() {
        return isInProcession;
    }

    public void isInProcession(boolean isInProcession) {
        this.isInProcession = isInProcession;
    }

    private void updateLinearSpeed(double newLinearPosition, long newTimestamp) {
        if (this.isOnRoute)
            linearSpeed = computeLinearSpeed(newLinearPosition, newTimestamp);
        else
            linearSpeed = 0.0;
    }

    public double computeLinearSpeed(double newLinearPosition, long newTimestamp) {
        double deltaT = (newTimestamp - timestamp) / (3600.0 * 1000.0); // in hours
        if (deltaT > 0) {
            double deltaP = (newLinearPosition - linearPosition) / 1000.0; // in km
            return deltaP / deltaT;
        }
        return 0.0;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public long getAge() {
        return clock.currentTimeMillis() - timestamp;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
package app.bladenight.common.procession;

import org.apache.commons.lang3.builder.ToStringBuilder;

import app.bladenight.common.time.Clock;
import app.bladenight.common.time.SystemClock;

import java.util.Objects;

public class Participant {
    public Participant() {
        clock = new SystemClock();
        lastLifeSign = clock.currentTimeMillis();
        lastKnownPoint = new MovingPoint();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public MovingPoint getLastKnownPoint() {
        return lastKnownPoint;
    }

    public void setLastKnownPoint(MovingPoint lastKnownPoint) {
        setLastLifeSign(clock.currentTimeMillis());
        this.lastKnownPoint = lastKnownPoint;
    }

    public long getLastLifeSign() {
        return lastLifeSign;
    }

    public void setLastLifeSign(long lastLifeSign) {
        this.lastLifeSign = lastLifeSign;
    }

    public double getLinearPosition() {
        return lastKnownPoint.getLinearPosition();
    }

    public double getLatitude() {
        return lastKnownPoint.getLatitude();
    }

    public double getLongitude() {
        return lastKnownPoint.getLongitude();
    }

    public void isOnRoute(boolean isOnRoute) {
        lastKnownPoint.isOnRoute(isOnRoute);
    }

    public boolean isOnRoute() {
        return lastKnownPoint.isOnRoute();
    }

    public double getLinearSpeed() {
        return lastKnownPoint.getLinearSpeed();
    }

    public double getRealSpeed() {
        return lastKnownPoint.getRealSpeed();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private String deviceId;
    private MovingPoint lastKnownPoint;
    private long lastLifeSign;
    private transient Clock clock;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(deviceId, that.deviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId);
    }
}

package app.bladenight.common.network.messages;

import org.apache.commons.lang3.builder.ToStringBuilder;

import app.bladenight.common.procession.MovingPoint;

public class MovingPointMessage {
    // We might use integers instead of double's to save bandwidth
    private long pos;
    private long spd;
    private double rsp; //real speed in km/h
    private long eta;
    private boolean ior;
    private boolean iip;
    private double lat;
    private double lon;
    private int acc;

    public MovingPointMessage() {
    }

    public MovingPointMessage(long linearPosition, long linearSpeed) {
        setPosition(linearPosition);
        isOnRoute(true);
        setSpeed(linearSpeed);
    }

    public MovingPointMessage(long linearPosition, long linearSpeed, boolean isInProcession) {
        setPosition(linearPosition);
        isOnRoute(true);
        setSpeed(linearSpeed);
        isInProcession(isInProcession);
    }

    public MovingPointMessage(MovingPoint mp) {
        copyFrom(mp);
    }

    public void copyFrom(MovingPoint mp) {
        setPosition((long) mp.getLinearPosition());
        setSpeed((long) mp.getLinearSpeed());
        setLatitude(mp.getLatitude());
        setLongitude(mp.getLongitude());
        isOnRoute(mp.isOnRoute());
        isInProcession(mp.isInProcession());
        setAccuracy((int) mp.getAccuracy());
        setRealSpeed(mp.getRealSpeed());
    }

    public long getPosition() {
        return pos;
    }

    public void setPosition(long pos) {
        this.pos = pos;
    }

    //calculated speed in m/s
    public long getSpeed() {
        return spd;
    }

    //calculated speed in m/s
    public void setSpeed(long spd) {
        this.spd = spd;
    }

    //gpsSpeed in km/h
    public double getRealSpeed() {
        return rsp;
    }

    //gpsSpeed in km/h
    public void setRealSpeed(double spd) {
        this.rsp = spd;
    }

    public void isOnRoute(boolean isOnRoute) {
        this.ior = isOnRoute;
    }

    public boolean isOnRoute() {
        return this.ior;
    }

    public void isInProcession(boolean isInProcession) {
        this.iip = isInProcession;
    }

    public boolean isInProcession() {
        return this.iip;
    }

    public double getLatitude() {
        return lat;
    }

    public void setLatitude(double lat) {
        this.lat = lat;
    }

    public double getLongitude() {
        return lon;
    }

    public long getEstimatedTimeToArrival() {
        return eta;
    }

    public void setEstimatedTimeToArrival(long eta) {
        this.eta = eta;
    }

    public void setLongitude(double lon) {
        this.lon = lon;
    }

    public int getAccuracy() {
        return acc;
    }

    public void setAccuracy(int acc) {
        this.acc = acc;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}

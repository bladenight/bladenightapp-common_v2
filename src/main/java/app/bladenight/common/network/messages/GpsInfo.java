package app.bladenight.common.network.messages;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class GpsInfo {
    public GpsInfo(String deviceId, boolean isParticipating, double lat, double lon,int accuracy, int specialFunction, double userSpeed, double realSpeed) {
        coo = new LatLong(lat, lon);
        this.par = isParticipating;
        this.did = deviceId;
        this.spf = specialFunction;
        this.spd = userSpeed;
        this.rsp = realSpeed;
        this.acc = accuracy;
    }

    public GpsInfo() {
        coo = new LatLong();
    }

    public GpsInfo(String deviceId, boolean isParticipating, double lat, double lon, double realSpeed) {
        coo = new LatLong(lat, lon);
        this.par = isParticipating;
        this.did = deviceId;
        this.rsp = realSpeed;
    }

    public double getLatitude() {
        return coo.getLatitude();
    }

    public void setLatitude(double latitude) {
        coo.setLatitude(latitude);
    }

    public double getLongitude() {
        return coo.getLongitude();
    }

    public void setLongitude(double longitude) {
        coo.setLongitude(longitude);
    }

    public String getDeviceId() {
        return did;
    }

    public void setDeviceId(String deviceId) {
        this.did = deviceId;
    }

    public boolean isParticipating() {
        return par;
    }

    public void isParticipating(boolean isParticipating) {
        this.par = isParticipating;
    }

    public int getAccuracy() {
        return acc;
    }

    public void setAccuracy(int acc) {
        this.acc = acc;
    }

    public int getSpecialFunction() {
        return spf;
    }

    public void setSpecialFunction(int spf) {
        this.spf = spf;
    }

    public double getUserSpeed() {
        return spd;
    }

    public void setUserSpeed(double spd) {
        this.spd = spd;
    }

    public double getRealUserSpeed() {
        return rsp;
    }

    public void setRealUserSpeed(double spd) {
        this.rsp = spd;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private LatLong coo;
    private String did;
    private boolean par;
    private int acc;
    private int spf;

    private double spd;
    private double rsp;
}

package app.bladenight.common.relationships;

import org.apache.commons.lang3.builder.ToStringBuilder;

import app.bladenight.common.persistence.ListItem;
import app.bladenight.common.time.Clock;
import app.bladenight.common.time.SystemClock;

public class Relationship implements ListItem {

    public Relationship() {
        this.clock = new SystemClock();
        setCreationTimestamp();
    }

    public Relationship(String deviceId1) {
        this.deviceId1 = deviceId1;
        this.clock = new SystemClock();
        setCreationTimestamp();
    }

    public Relationship(String deviceId1, Clock clock) {
        this.deviceId1 = deviceId1;
        this.clock = clock;
        setCreationTimestamp();
    }

    public Relationship(Clock clock) {
        this.clock = clock;
        setCreationTimestamp();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getDeviceId1() {
        return deviceId1;
    }

    public String getDeviceId2() {
        return deviceId2;
    }

    public void setDeviceId2(String deviceId2) {
        this.deviceId2 = deviceId2;
    }

    public int getFriendId1() {
        return friendId1;
    }

    public int getFriendId2() {
        return friendId2;
    }

    public void setFriendId1(int friendId) {
        this.friendId1 = friendId;
    }

    public void setFriendId2(int friendId) {
        this.friendId2 = friendId;
    }

    public boolean isPending() {
        return deviceId2 == null;
    }

    public long getAge() {
        return clock.currentTimeMillis() - creationTimestamp;
    }

    public boolean involves(String deviceId) {
        return deviceId.equals(deviceId1) || deviceId.equals(deviceId2);
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public void setCreationTimestamp() {
        this.creationTimestamp = clock.currentTimeMillis();
    }

    @Override
    public String getPersistenceId() {
        return Long.toString(id);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


    private long id;
    private long requestId;
    private String deviceId1;
    private int friendId1;
    private String deviceId2;
    private int friendId2;
    private long creationTimestamp;
    transient private Clock clock;
}

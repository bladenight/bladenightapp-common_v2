package app.bladenight.common.relationships;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class RelationshipMember {
    RelationshipMember(int friendId, String deviceId, long requestId) {
        this.friendId = friendId;
        this.deviceId = deviceId;
        this.requestId = requestId;
    }
    public int getFriendId() {
        return friendId;
    }
    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public long getRequestId() {
        return requestId;
    }
    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private int friendId;
    private String deviceId;
    private long requestId;
}
package app.bladenight.common.network.messages;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class FriendMessage extends MovingPointMessage {

    public long getRequestId() {
        return req;
    }

    public void setRequestId(long req) {
        this.req = req;
    }

    public int getFriendId() {
        return fid;
    }

    public void setFriendId(int fid) {
        this.fid = fid;
    }

    public int getFriendSpecialValue() {
        return spv;
    }

    public void setFriendSpecialValue(int spv) {
        this.spv = spv;
    }

    public boolean isOnline() {
        return onl;
    }

    public void isOnline(boolean onl) {
        this.onl = onl;
    }

    public boolean isRelationshipPending() {
        return req != 0;
    }

    private long req;
    private int fid;
    private boolean onl;

    //mark friend as special
    private int spv;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}

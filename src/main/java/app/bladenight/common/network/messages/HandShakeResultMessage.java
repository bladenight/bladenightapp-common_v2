package app.bladenight.common.network.messages;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class HandShakeResultMessage {
    public boolean sta; // node list
    public int mbu;

    public HandShakeResultMessage(boolean status, int minBuild) {
        mbu = minBuild;
        sta = status;
    }

    public boolean getStatus() {
        return sta;
    }


    public int getMinBuild() {
        return mbu;
    }

    public void setStatus(boolean status) {
        this.sta = status;
    }

    public void setMinBuild(int minBuild) {
        this.mbu = minBuild;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}

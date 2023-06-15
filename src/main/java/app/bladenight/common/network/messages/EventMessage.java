package app.bladenight.common.network.messages;

import app.bladenight.common.events.Event;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;

public class EventMessage implements Serializable {

    private static final long serialVersionUID = 7035627836216623223L;

    public enum EventStatus {
        PEN,
        CON,
        CAN,
        NOE,
        RUN,
        FIN,
        UKN;
    }

    public String sta;    // start time: "yyyy-MM-dd'T'HH:mm"
    public long dur;    // duration in minutes
    public String rou;    // route name
    public int par;    // number of participants
    public EventStatus sts;    // status
    public long len;    // length in meters
    public String stp;  //starting Point
    public long slo;//starting point longitude
    public long sla;//starting point latitude

    public EventMessage() {
        sts = EventStatus.PEN;
    }

    public EventMessage(Event e) {
        copyFromEvent(e);
    }

    /*
        public String getStartDate() {
            return sta;
        }

        public void setStartDate(String sta) {
            this.sta = sta;
        }

        public long getDuration() {
            return dur;
        }

        public void setDuration(long dur) {
            this.dur = dur;
        }

        public String getRouteName() {
            return rou;
        }

        public void setRouteName(String rou) {
            this.rou = rou;
        }

        public int getParticipantsCount() {
            return par;
        }

        public void setParticipantsCount(int par) {
            this.par = par;
        }

        public EventStatus getStatus() {
            return sts;
        }

        public void setStatus(EventStatus sts) {
            this.sts = sts;
        }

        public long getLength() {
            return len;
        }

        public void setLength(long length) {
            this.len = length;
        }

        public String getStartpoint() {
            return stp;
        }

        public void setStartpoint(String startpoint) {
            this.stp = startpoint;
        }

        public long getStpLatitude() {
            return sla;
        }

        public void setStpLatitude(long lat) {
            this.sla = lat;
        }

        public long getStpLongitude() {
            return slo;
        }

        public void setStpLongitude(long lon) {
            this.slo = lon;
        }
    */
    public void copyFromEvent(Event e) {
        sta = dateFormatter.print(e.getStartDate());
        dur = e.getDuration().getStandardMinutes();
        rou = e.getRouteName();
        par = e.getParticipants();
        sts = convertStatus(e.getStatus());
        len = e.getLength();
        stp = e.getStartPoint();
        sla = e.getStpLatitude();
        slo = e.getStpLongitude();
    }

    public static EventMessage newFromEvent(Event e) {
        EventMessage message = new EventMessage();
        message.copyFromEvent(e);
        return message;
    }

    public Event toEvent() {
        return new Event.Builder()
                .setStartDate(dateFormatter.parseDateTime(sta))
                .setDuration(new Duration(dur * 60 * 1000))
                .setRouteName(rou)
                .setParticipants(par)
                .setStatus(convertStatus(sts))
                .setLength(len)
                .setStartPoint(stp)
                .setStartLongitude(slo)
                .setStartLatitude(sla)
                .build();
    }

    static public EventStatus convertStatus(Event.EventStatus fromStatus) {
        switch (fromStatus) {
            case CANCELLED:
                return EventStatus.CAN;
            case CONFIRMED:
                return EventStatus.CON;
            case PENDING:
                return EventStatus.PEN;
            case RUNNING:
                return EventStatus.RUN;
            case FINISHED:
                return EventStatus.FIN;
            case NOEVENTPLANNED:
                return EventStatus.NOE;
            default:
                getLog().error("Unknown status: " + fromStatus);
                return EventStatus.UKN;
        }
    }

    static public Event.EventStatus convertStatus(EventStatus fromStatus) {
        switch (fromStatus) {
            case CAN:
                return Event.EventStatus.CANCELLED;
            case CON:
                return Event.EventStatus.CONFIRMED;
            case PEN:
                return Event.EventStatus.PENDING;
            case NOE:
                return Event.EventStatus.NOEVENTPLANNED;
            case RUN:
                return Event.EventStatus.RUNNING;
            case FIN:
                return Event.EventStatus.FINISHED;

            default:
                getLog().error("Unknown status: " + fromStatus);
                return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    protected final static DateTimeFormatter dateFormatter;

    static {
        dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm");
    }

    private static Logger log;

    public static void setLog(Logger log) {
        EventMessage.log = log;
    }

    protected static Logger getLog() {
        if (log == null)
            log = LogManager.getLogger(EventMessage.class.getName());
        return log;
    }
}

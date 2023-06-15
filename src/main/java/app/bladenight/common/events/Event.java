package app.bladenight.common.events;

import app.bladenight.common.persistence.ListItem;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;

public class Event implements ListItem, Serializable {
    private static final long serialVersionUID = -5865857910368371094L;

    public enum EventStatus {
        PENDING("P"),
        CONFIRMED("O"),
        CANCELLED("A"),
        NOEVENTPLANNED("N"),
        FINISHED("F"),
        RUNNING("R");

        private final String status;

        EventStatus(String s) {
            status = s;
        }

        public boolean equalsName(String otherName) {
            return status.equals(otherName);
        }

        @Override
        public String toString() {
            return "EventStatus." + status;
        }

        public String asString() {
            return status;
        }
    }

    public static class Builder {
        private final Event event;

        public Builder() {
            event = new Event();
        }

        public Builder setStartDate(String dateString) {
            event.startDate = dateFormatter.parseDateTime(dateString);
            return this;
        }

        public Builder setStartDate(DateTime date) {
            event.setStartDate(date);
            return this;
        }

        public Builder setDuration(Duration duration) {
            event.setDuration(duration);
            return this;
        }

        public Builder setDurationInMinutes(int minutes) {
            event.setDuration(new Duration((long) minutes * 60 * 1000));
            return this;
        }

        public Builder setRouteName(String routeName) {
            event.setRouteName(routeName);
            return this;
        }

        public Builder setParticipants(int participants) {
            event.setParticipants(participants);
            return this;
        }

        public Builder setStatus(EventStatus status) {
            event.setStatus(status);
            return this;
        }

        public Builder setLength(long length) {
            event.setLength(length);
            return this;
        }

        public Builder setStartPoint(String startPoint) {
            event.setStartpoint(startPoint);
            return this;
        }

        public Builder setStartLongitude(long startLongitude) {
            event.setStpLongitude(startLongitude);
            return this;
        }

        public Builder setStartLatitude(long startLatitude) {
            event.setStpLatitude(startLatitude);
            return this;
        }

        public Event build() {
            return event;
        }

    }

    public Event() {
        duration = new Duration(0);
        status = EventStatus.PENDING;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public long getLength() {
        return routeLength;
    }

    public void setLength(long length) {
        this.routeLength = length;
    }

    public void setStartDate(DateTime date) {
        this.startDate = date;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public String getStartDateAsString(String format) {
        DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(format);
        return dateFormatter.print(getStartDate());
    }

    public DateTime getEndDate() {
        return startDate.plus(duration);
    }

    public String getEndDateAsString(String format) {
        DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(format);
        return dateFormatter.print(getEndDate());
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartpoint(String startpoint) {
        this.startPoint = startpoint;
    }

    public long getStpLatitude() {
        return startLatidude;
    }

    public void setStpLatitude(long lat) {
        this.startLatidude = lat;
    }

    public long getStpLongitude() {
        return startLongitude;
    }

    public void setStpLongitude(long lon) {
        this.startLongitude = lon;
    }


    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }


    public boolean isActive() {
        boolean isCurrent = new Interval(getStartDate().minusMinutes(15), getEndDate().plusMinutes(15)).contains(new DateTime());
        boolean isConfirmed = getStatus() == EventStatus.CONFIRMED;
        return isCurrent && isConfirmed;
    }

    @Override
    public String getPersistenceId() {
        return getStartDateAsString("yyyy-MM-dd");
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, toStringStyle);
    }

    private static Logger log;

    public static void setLog(Logger log) {
        Event.log = log;
    }

    protected static Logger getLog() {
        if (log == null)
            log = LogManager.getLogger(Event.class.getName());
        return log;
    }

    protected DateTime startDate;
    protected Duration duration;
    protected String routeName;
    protected int participants;
    protected long routeLength;
    protected EventStatus status;
    protected String startPoint;
    protected long startLongitude;
    protected long startLatidude;
    protected final static DateTimeFormatter dateFormatter;
    protected final static ToStringStyle toStringStyle;

    static {
        dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm");
        toStringStyle = new ToStringStyle() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
                if (value instanceof DateTime) {
                    value = dateFormatter.print((DateTime) value);
                }
                buffer.append(value);
            }
        };
    }


}

package app.bladenight.common.events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import app.bladenight.common.keyvaluestore.KeyValueStore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

import app.bladenight.common.events.Event.EventStatus;
import app.bladenight.common.persistence.InconsistencyException;
import app.bladenight.common.persistence.ListPersistor;

public class EventList implements Iterable<Event> {

    public EventList() {
        events = new ArrayList<>();
    }

    public void read() throws IOException, InconsistencyException {
        persistor.read();
    }

    public void write() throws IOException {
        persistor.write();
    }

    public void setPersistor(ListPersistor<Event> persistor) {
        persistor.setGson(EventGsonHelper.getGson());
        persistor.setList(events);
        this.persistor = persistor;
    }


    public Event getNextEvent() {
        Event nextEvent = null;
        DateTime now = new DateTime();
        for (Event event : events) {
            if (now.isBefore(event.getEndDate())) {
                if (nextEvent == null || event.getStartDate().isBefore(nextEvent.getStartDate()))
                    nextEvent = event;
            }

        }
        if (nextEvent == null) {
            //server gives no response when null result
            nextEvent = new Event();
            nextEvent.setStatus(Event.EventStatus.NOEVENTPLANNED);
        }
        return nextEvent;
    }

    public boolean isLive(Event event) {
        if ( ! event.equals(getNextEvent()) )
            return false;
        if ( event.getStatus() != EventStatus.CONFIRMED )
            return false;

        if (event.getStartDate().isBeforeNow() && event.getEndDate().isBeforeNow())
            return true;

        DateTime now = new DateTime();
        Minutes minutesToStart = Minutes.minutesBetween(now, event.getStartDate());

        return minutesToStart.getMinutes() < CONSIDER_LIVE_MINUTES;
    }


    public void setNextRoute(String routeName) {
        Event event = getNextEvent();
        if ( event == null ) {
            getLog().error("setActiveRoute: No current event found");
            return;
        }
        event.setRouteName(routeName);
    }

    public void setStatusOfNextEvent(EventStatus newStatus) {
        Event event = getNextEvent();
        if ( event == null ) {
            getLog().error("setActiveStatus: No current event found");
            return;
        }
        event.setStatus(newStatus);
    }


    public void addEvent(Event event) {
        events.add(event);
    }

    public Event get(int pos) {
        return events.get(pos);
    }

    public void sortByStartDate() {
        Comparator<Event> comparator = new Comparator<Event>() {

            @Override
            public int compare(Event e1, Event e2) {
                return (e1.getStartDate().compareTo(e2.getStartDate()));
            }
        };
        Collections.sort(events, comparator);
    }

    public int size() {
        return events.size();
    }

    public int indexOf(Event event) {
        return events.indexOf(event);
    }

    @Override
    public Iterator<Event> iterator() {
        return events.iterator();
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private static Logger log;

    public static void setLog(Logger log) {
        EventList.log = log;
    }

    protected static Logger getLog() {
        if (log == null)
            log = LogManager.getLogger(EventList.class.getName());
        return log;
    }

    protected List<Event> events;
    // don't serialize the persistor (e.g. transient)
    private transient ListPersistor<Event> persistor;
    private static final int CONSIDER_LIVE_MINUTES = 30;


}

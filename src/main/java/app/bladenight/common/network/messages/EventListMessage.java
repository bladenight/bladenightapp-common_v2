package app.bladenight.common.network.messages;

import app.bladenight.common.keyvaluestore.KeyValueStore;
import org.apache.commons.lang3.builder.ToStringBuilder;


import app.bladenight.common.events.Event;
import app.bladenight.common.events.EventList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventListMessage {
    public EventMessage[] evt = new EventMessage[0];

    public static EventListMessage newFromEventsList(EventList list) {
        EventListMessage message = new EventListMessage();
        message.copyFromEventsList(list);
        return message;
    }

    public void copyFromEventsList(EventList list) {
        evt = new EventMessage[list.size()];
        int i = 0;
        for (Event e : list) {
            evt[i++] = EventMessage.newFromEvent(e);
        }
    }

    public EventList convertToEventsList() {
        EventList list = new EventList();
        for (int i=0; i<evt.length; i++) {
            list.addEvent(evt[i].toEvent());
        }
        return list;
    }

    public EventMessage[] getEvents() {
        return evt;
    }

    public void setEvents(EventMessage[] evt) {
        this.evt = evt;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    private static Logger log;

    public static void setLog(Logger log) {
        EventListMessage.log = log;
    }

    protected static Logger getLog() {
        if (log == null)
            log = LogManager.getLogger(EventListMessage.class.getName());
        return log;
    }

    public int size() {
        return evt.length;
    }

    public EventMessage get(int position) {
        return evt[position];
    }
}

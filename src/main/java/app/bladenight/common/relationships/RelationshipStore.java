package app.bladenight.common.relationships;

import app.bladenight.common.exceptions.BadStateException;
import app.bladenight.common.persistence.InconsistencyException;
import app.bladenight.common.persistence.ListPersistor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RelationshipStore {

    public RelationshipStore() {
        relationships = Collections.synchronizedList(new ArrayList<Relationship>());
        lock = new Object();
    }

    public void read() throws IOException, InconsistencyException {
        if (persistor != null)
            persistor.read();
        else
            getLog().error("No persistor set");
    }

    public void write() throws IOException {
        if (persistor != null)
            persistor.write();
        else
            getLog().error("No persistor set");
    }

    public void setPersistor(ListPersistor<Relationship> persistor) {
        this.persistor = persistor;
        persistor.setList(relationships);
    }

    public HandshakeInfo newRequest(String deviceId1, int friendId1) {
        synchronized (lock) {
            long requestId = generateRequestId();
            long id = generateRelationshipId();

            // Delete any relationship that the user may already have with the same ids
            deleteRelationship(deviceId1, friendId1);

            checkForDuplicateIds();

            Relationship relationship = new Relationship(deviceId1);
            relationship.setId(id);
            relationship.setFriendId1(friendId1);
            relationship.setRequestId(requestId);
            relationships.add(relationship);

            checkForDuplicateIds();

            getLog().info("Created new request: " + relationship);

            HandshakeInfo handshakeInfo = new HandshakeInfo();
            handshakeInfo.setRequestId(requestId);
            handshakeInfo.setFriendId(friendId1);

            checkForDuplicateIds();

            return handshakeInfo;
        }
    }

    public void checkForDuplicateIds() {
        List<Long> ids = new ArrayList<Long>();
        for (Relationship r : relationships) {
            if (ids.contains(r.getId())) {
                throw new NullPointerException("Error here " + r);
            }
            ids.add(r.getId());
        }
    }

    public synchronized HandshakeInfo finalize(long requestId, String deviceId2, int friendId2) throws BadStateException {
        Relationship rel = getRelationshipForRequestId(requestId);

        finalizeCheck(requestId, deviceId2, rel);

        deleteRelationship(deviceId2, friendId2);

        HandshakeInfo handshakeInfo = new HandshakeInfo();
        synchronized (lock) {
            handshakeInfo.setFriendId(friendId2);

            rel.setRequestId(0);
            rel.setDeviceId2(deviceId2);
            rel.setFriendId2(friendId2);

            getLog().info(rel.getDeviceId1() + " and " + rel.getDeviceId2() + " are now connected");
        }
        return handshakeInfo;
    }

    public Relationship getRelationshipForRequestId(long requestId) {
        for (Relationship rel : relationships) {
            if (rel.getRequestId() == requestId)
                return rel;
        }
        return null;
    }

    Relationship getRelationshipWithId(long id) {
        for (Relationship rel : relationships) {
            if (rel.getId() == id)
                return rel;
        }
        return null;
    }


    private void finalizeCheck(long requestId, String deviceId2, Relationship rel) throws BadStateException {
        if (rel == null) {
            String msg = "Not a valid pending relationship id: " + requestId;
            getLog().warn(msg);
            throw new BadStateException(msg);
        }
        if (rel.getDeviceId1().equals(deviceId2)) {
            String msg = "Relationship with self is not allowed: " + rel;
            getLog().warn(msg);
            throw new BadStateException(msg);
        }
        if (exists(rel.getDeviceId1(), deviceId2)) {
            String msg = "Devices are already connected: " + rel;
            getLog().warn(msg);
            throw new BadStateException(msg);
        }
    }

    public void setRelationshipIdLength(int digits) {
        relationshipIdLength = digits;
    }

    public void setRequestIdLength(int digits) {
        requestIdLength = digits;
    }

    synchronized long generateRequestId() {
        while (true) {
            long id = generateIdCandidate(requestIdLength);
            if (getRelationshipForRequestId(id) == null)
                return id;
        }
    }

    synchronized long generateRelationshipId() {
        while (true) {
            long id = generateIdCandidate(relationshipIdLength);
            if (getRelationshipWithId(id) == null)
                return id;
        }
    }


    private long generateIdCandidate(int length) {
        long min = pow(10, length - 1);
        long range = pow(10, length) - min;
        return min + (Math.abs(getRandom().nextLong()) % range);
    }

    long pow(int a, int b) {
        if (b <= 0)
            return 1;
        long result = a;
        for (int i = 0; i < b - 1; i++)
            result = result * a;
        return result;
    }

    public boolean exists(String deviceId1, String deviceId2) {
        for (Relationship relationship : relationships) {
            int match = 0;
            if (deviceId1.equals(relationship.getDeviceId1()))
                match++;
            if (deviceId1.equals(relationship.getDeviceId2()))
                match++;
            if (deviceId2.equals(relationship.getDeviceId1()))
                match++;
            if (deviceId2.equals(relationship.getDeviceId2()))
                match++;
            if (match == 2)
                return true;
        }
        return false;
    }

    public boolean isPendingRequestId(long requestId) {
        Relationship rel = getRelationshipForRequestId(requestId);
        return (rel != null);
    }

    /**
     * Returns the deviceId's that have a relationship to the given one
     */
    public List<RelationshipMember> getAllRelationships(String deviceId) {
        return getRelationships(deviceId, true);
    }

    public List<RelationshipMember> getFinalizedRelationships(String deviceId) {
        return getRelationships(deviceId, false);
    }

    private List<RelationshipMember> getRelationships(String deviceId, boolean includeAll) {
        List<RelationshipMember> list = new ArrayList<RelationshipMember>();
        for (Relationship relationship : relationships) {
            if (includeAll || !relationship.isPending()) {
                if (deviceId.equals(relationship.getDeviceId1()))
                    list.add(new RelationshipMember(relationship.getFriendId1(), relationship.getDeviceId2(), relationship.getRequestId()));
                else if (deviceId.equals(relationship.getDeviceId2()))
                    list.add(new RelationshipMember(relationship.getFriendId2(), relationship.getDeviceId1(), relationship.getRequestId()));
            }
        }
        return list;
    }

    public int deleteRelationship(String deviceId, long friendId) {
        int hits = 0;
        List<Relationship> toRemove = new ArrayList<Relationship>();
        for (Relationship relationship : relationships) {
            if (doesRelationConcern(relationship, deviceId, friendId)) {
                getLog().info("Removing relation: " + relationship);
                toRemove.add(relationship);
            }
        }
        deleteRelationships(toRemove);
        return hits;
    }

    public void deleteRelationships(List<Relationship> list) {
        synchronized (lock) {
            while (list.size() > 0) {
                relationships.remove(list.remove(0));
            }
        }
    }

    public int removePendingRelationshipsOlderThan(long age) {
        List<Relationship> toBeRemoved = new ArrayList<Relationship>();
        for (Relationship relationship : relationships) {
            if (relationship.isPending() && relationship.getAge() > age)
                toBeRemoved.add(relationship);
        }
        int hits = toBeRemoved.size();
        if (hits > 0)
            getLog().info("Removing " + hits + " relationship(s)");
        deleteRelationships(toBeRemoved);
        return hits;
    }

    void addRelationship(Relationship relationship) {
        relationships.add(relationship);
    }

    private Random getRandom() {
        if (random == null)
            random = new Random();
        return random;
    }

    private boolean doesRelationConcern(Relationship relationship, String deviceId, long friendId) {
        if (deviceId.equals(relationship.getDeviceId1()) && friendId == relationship.getFriendId1())
            return true;
        if (deviceId.equals(relationship.getDeviceId2()) && friendId == relationship.getFriendId2())
            return true;
        return false;
    }

    public long size() {
        return relationships.size();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private static Logger log;

    public static void setLog(Logger log) {
        RelationshipStore.log = log;
    }

    protected static Logger getLog() {
        if (log == null)
            log = LogManager.getLogger(RelationshipStore.class.getName());
        return log;
    }

    private List<Relationship> relationships;
    private transient Random random = null;
    private int requestIdLength = 6;
    private int relationshipIdLength = 12;
    private Object lock;
    private ListPersistor<Relationship> persistor;

}

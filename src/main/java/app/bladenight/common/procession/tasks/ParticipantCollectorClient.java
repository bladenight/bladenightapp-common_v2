package app.bladenight.common.procession.tasks;

import java.util.List;

import app.bladenight.common.procession.Participant;

public interface ParticipantCollectorClient {
    public List<Participant> getParticipants();
    public void removeParticipant(String deviceId);
}

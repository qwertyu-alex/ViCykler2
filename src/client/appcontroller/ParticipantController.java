package client.appcontroller;

import client.ui.Content;
import shared.DTO.Participant;

public class ParticipantController {

    private Content content;
    private Participant currentParticipant;

    public ParticipantController(Content content, Participant currentParticipant) {
        this.content = content;
        this.currentParticipant = currentParticipant;

    }
}

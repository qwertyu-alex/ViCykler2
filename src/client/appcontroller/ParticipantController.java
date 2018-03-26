package client.appcontroller;

import client.ui.Content;
import server.withoutDB.Data;

public class ParticipantController {

    private Content content;
    private Data data;

    public ParticipantController(Content content, Data data) {
        this.content = content;
        this.data = data;
    }
}

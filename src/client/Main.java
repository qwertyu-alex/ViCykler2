package client;

import client.appcontroller.AdminController;
import client.appcontroller.GuestController;
import client.appcontroller.ParticipantController;
import client.ui.Content;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;
import server.withoutDB.Data;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class Main implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        RootPanel.get().clear(true);
        Content content = new Content();
        Data data = new Data();


        RootLayoutPanel.get().add(content);

        new GuestController(content, data);
        new ParticipantController(content, data);
        new AdminController(content, data);

    }

}

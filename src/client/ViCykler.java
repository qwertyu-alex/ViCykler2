package client;

import client.appcontroller.GuestController;
import client.ui.Content;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;

public class ViCykler implements EntryPoint {
    public void onModuleLoad() {
        RootPanel.get().clear(true);
        Content content = new Content();

        RootLayoutPanel.get().add(content);

        GuestController guestController = new GuestController(content);

    }
}

package client;

import client.appcontroller.*;
import client.ui.Content;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import rpc.ApplicationService;
import rpc.ApplicationServiceAsync;
//import server.withoutDB.Data;

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
//        Data data = new Data();

        ///Proxy klasse, som ligger på serveren andet sted. Denne linje kører koden på serveren og ikke klienten,
        // men den gør det muligt for klienten at kunne benytte interfacene fra rpc modulet/
        ApplicationServiceAsync rpcService= GWT.create(ApplicationService.class);

//      Laver vores UI:
        RootLayoutPanel.get().add(content);

        new GuestController(content, rpcService);

    }

}

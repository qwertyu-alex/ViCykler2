package client;

import client.appcontroller.*;
import client.ui.Content;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import client.rpc.ApplicationService;
import client.rpc.ApplicationServiceAsync;

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

        /**Proxy klasse, som ligger på serveren andet sted. Denne linje kører koden på serveren og ikke klienten,
         // men den gør det muligt for klienten at kunne benytte interfacene fra client.rpc modulet/
         // AKA the magic call*/
        ApplicationServiceAsync rpcService = GWT.create(ApplicationService.class);

//      Laver vores UI:
        RootLayoutPanel.get().add(content);

        new GuestController(content, rpcService);

    }

}

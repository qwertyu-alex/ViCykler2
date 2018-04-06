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

        ///--------------///
        ApplicationServiceAsync rpcService= GWT.create(ApplicationService.class);

//      Laver vores UI:
        RootLayoutPanel.get().add(content);

        new GuestController(content, rpcService);

    }

}
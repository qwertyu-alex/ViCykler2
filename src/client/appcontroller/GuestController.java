package client.appcontroller;

import client.ui.Content;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import rpc.ApplicationService;
import rpc.ApplicationServiceAsync;
//import server.withoutDB.Data;
import shared.DTO.Participant;

import java.util.ArrayList;

public class GuestController {

    private Content content;
    private ListDataProvider<Participant> participantListDataProvider;
//    private Data data;
    private ArrayList<Participant> participants;
    private ApplicationServiceAsync rpcService;


    public GuestController(Content content, ApplicationServiceAsync rpcService){
        this.content = content;
        this.participantListDataProvider = new ListDataProvider<>();
//        this.data = data;
        this.rpcService = rpcService;

        //Test. Slet inden roll-out
        rpcService.returnPersons(new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                Window.alert(result);
            }
        });

//        Tilføjer clickhandlers til forskellige elementer på siden
        addClickHandlers();



//        Opretter tabellen
        createTable();
    }

    /**
     * En clickhandler klasse som indeholder en onclick metode.
     */
    class GuestClickHandlers implements ClickHandler{

        @Override
        public void onClick(ClickEvent event) {


            if (event.getSource() == content.getGuestView().getLogindBtn()){
                content.getGuestView().changeView(0);
            } else if (event.getSource() == content.getGuestView().getTilmeldingBtn()){
                content.getGuestView().changeView(1);
            } else if (event.getSource() == content.getGuestView().getLogoImg()){
                content.getGuestView().changeView(content.getGuestView().getStartView());
            } else if (event.getSource() == content.getGuestView().getStatistiskBtn()){
                content.getGuestView().changeView(2);
//                Dette kald skal være her for at tabellen viser sine data.
                participantListDataProvider.refresh();
                //Skifter til Participant view
            } else if (event.getSource() == content.getGuestView().getLoginView().getLoginBtn()){
                content.switchToParticipantView();
            }
        }
    }

    class ShowInfoHandler implements ActionCell.Delegate<Participant>{
        @Override
        public void execute(Participant object) {
            Window.alert("Hello");
        }
    }

    private void addClickHandlers(){
        content.getGuestView().addClickHandlers(new GuestClickHandlers());
        content.getGuestView().getStatisticView().addClickHandler(new ShowInfoHandler());
    }

    private void createTable(){

        rpcService.getAllParticipants(new AsyncCallback<ArrayList<Participant>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(ArrayList<Participant> result) {
                content.getGuestView().getStatisticView().initTable(participantListDataProvider);
                participantListDataProvider.getList().addAll(result);
            }
        });

    }
}

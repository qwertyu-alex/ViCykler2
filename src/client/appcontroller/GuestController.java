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
import server.withoutDB.Data;
import shared.DTO.Participant;

import java.util.ArrayList;

public class GuestController {

    private Content content;
    private ListDataProvider<Participant> participantListDataProvider;
    private Data data;
    private ArrayList<Participant> participants;
    private ApplicationServiceAsync rpcService;


    public GuestController(Content content, Data data, ApplicationServiceAsync rpcService){
        this.content = content;
        this.participantListDataProvider = new ListDataProvider<>();
        this.data = data;
        this.rpcService = rpcService;

        rpcService.authorizePerson("Name", "Pass", new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Err");
            }

            @Override
            public void onSuccess(Boolean result) {
                Window.alert(Boolean.toString(result));
            }
        });


//        Tilføjer clickhandlers til forskellige elementer på siden
        content.getGuestView().addClickHandlers(new GuestClickHandlers());
        content.getGuestView().getStatisticView().addClickHandler(new ShowInfoHandler());

//        Opretter tabellen
        content.getGuestView().getStatisticView().initTable(participantListDataProvider);
        participantListDataProvider.getList().addAll(data.getParticipants());

//        Window.alert(participantListDataProvider.getList().get(0).getName());

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
}

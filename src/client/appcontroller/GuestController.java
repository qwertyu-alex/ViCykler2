package client.appcontroller;

import client.ui.Content;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.ListDataProvider;
import server.withoutDB.Data;
import shared.Participant;

import java.util.ArrayList;

public class GuestController {

    private Content content;
    private ListDataProvider<Participant> participantListDataProvider;
    private Data data;
    private ArrayList<Participant> participants;


    public GuestController(Content content, Data data){
        this.content = content;
        this.participantListDataProvider = new ListDataProvider<>();
        this.data = data;



//        Tilføjer clickhandlers til forskellige elementer på siden
        content.getGuestView().addClickHandlers(new GuestClickHandlers());
        content.getGuestView().getStatistikView().addClickHandler(new ShowInfoHandler());

//        Opretter tabellen
        content.getGuestView().getStatistikView().initTable(participantListDataProvider);
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
                content.getGuestView().changeView(0);
            } else if (event.getSource() == content.getGuestView().getStatistiskBtn()){
                content.getGuestView().changeView(2);

//                Dette kald skal være her for at tabellen viser sine data.
                participantListDataProvider.refresh();
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

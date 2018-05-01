package client.ui;

import client.ui.admin.AdminView;
import client.ui.guest.GuestView;
import client.ui.participant.ParticipantView;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.HTML;

public class Content extends Composite{
    private DeckLayoutPanel mainDeck;
    private GuestView guestView;

    public Content(){
//        DeckLayoutPanel:
        mainDeck = new DeckLayoutPanel();

//        Views:
        guestView = new GuestView();

//        Tilføjer alle mine views til decket:
        mainDeck.add(guestView);

        mainDeck.showWidget(guestView);

        initWidget(mainDeck);
    }

    public void switchToGuestView (){
        mainDeck.showWidget(guestView);
    }

    public GuestView getGuestView() {
        return guestView;
    }

    public DeckLayoutPanel getMainDeck() {
        return mainDeck;
    }
}

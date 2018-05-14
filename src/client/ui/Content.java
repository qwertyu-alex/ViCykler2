package client.ui;

import client.ui.guest.GuestView;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckLayoutPanel;

public class Content extends Composite{
    private DeckLayoutPanel mainDeck;
    private GuestView guestView;

    public Content(){
        //DeckLayoutPanel:
        mainDeck = new DeckLayoutPanel();
        //Views:
        guestView = new GuestView();
        //Tilføjer alle mine views til decket:
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

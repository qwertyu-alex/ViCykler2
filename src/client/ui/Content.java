package client.ui;

import client.ui.guest.GuestView;
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

        mainDeck.add(guestView);

        mainDeck.showWidget(0);

        initWidget(mainDeck);
    }

    public GuestView getGuestView() {
        return guestView;
    }
}

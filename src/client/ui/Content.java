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
    private AdminView adminView;
    private ParticipantView participantView;


    public Content(){
//        DeckLayoutPanel:
        mainDeck = new DeckLayoutPanel();

//        Views:
        guestView = new GuestView();
        adminView = new AdminView();
        participantView = new ParticipantView();
//        Tilføjer alle mine views til decket:
        mainDeck.add(guestView);
        mainDeck.add(adminView);
        mainDeck.add(participantView);

        mainDeck.showWidget(guestView);

        initWidget(mainDeck);
    }

    public void switchToGuestView (){
        mainDeck.showWidget(guestView);
    }

    public void switchToAdminView (){
        mainDeck.showWidget(adminView);
    }

    public void switchToParticipantView (){
        mainDeck.showWidget(participantView);
    }

    public GuestView getGuestView() {
        return guestView;
    }

    public AdminView getAdminView() {
        return adminView;
    }

    public ParticipantView getParticipantView() {
        return participantView;
    }

    public void setAdminView(AdminView adminView) {
        this.adminView = adminView;
    }

    public void setParticipantView(ParticipantView participantView) {
        this.participantView = participantView;
    }

    public DeckLayoutPanel getMainDeck() {
        return mainDeck;
    }
}

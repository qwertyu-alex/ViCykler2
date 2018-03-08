package client.ui.guest;

import client.ui.guest.widgets.LoginView;
import client.ui.guest.widgets.TilmeldingView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class GuestView extends Composite {
    interface guestViewUiBinder extends UiBinder<HTMLPanel, GuestView> {
    }

    private static guestViewUiBinder ourUiBinder = GWT.create(guestViewUiBinder.class);

    @UiField
    HTMLPanel southPanel, centerPanel, northPanel;

    @UiField
    DeckLayoutPanel centerDeck;

    @UiField
    Button tilmeldingBtn, statistiskBtn, logindBtn;

    @UiField
    Image logoImg;


    private LoginView loginView;
    private TilmeldingView tilmeldingView;


    public GuestView() {
        initWidget(ourUiBinder.createAndBindUi(this));

        loginView = new LoginView();
        tilmeldingView = new TilmeldingView();

//        Add DeckLayoutPanel to centerPanel
        centerPanel.add(centerDeck);

//        Add different views to the DeckLayoutPanel
        centerDeck.add(loginView);
        centerDeck.add(tilmeldingView);

        centerDeck.showWidget(0);
    }

    public void addClickHandlers (ClickHandler clickHandler){
        tilmeldingBtn.addClickHandler(clickHandler);
        statistiskBtn.addClickHandler(clickHandler);
        logindBtn.addClickHandler(clickHandler);
        logoImg.addClickHandler(clickHandler);
    }

    public Button getTilmeldingBtn() {
        return tilmeldingBtn;
    }

    public Button getStatistiskBtn() {
        return statistiskBtn;
    }

    public Button getLogindBtn() {
        return logindBtn;
    }

    public Image getLogoImg() {
        return logoImg;
    }

    public void changeView(int index){
        centerDeck.showWidget(index);
    }
}
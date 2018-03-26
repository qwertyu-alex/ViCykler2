package client.ui.guest;

import client.ui.guest.widgets.LoginView;
import client.ui.guest.widgets.StartView;
import client.ui.guest.widgets.StatisticView;
import client.ui.guest.widgets.SignUpView;
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
    private StatisticView statisticView;
    private SignUpView signUpView;
    private StartView startView;


    public GuestView() {
        initWidget(ourUiBinder.createAndBindUi(this));

        loginView = new LoginView();
        signUpView = new SignUpView();
        statisticView = new StatisticView();
        startView = new StartView();


//        Add DeckLayoutPanel to centerPanel
        centerPanel.add(centerDeck);

//        Add different views to the DeckLayoutPanel
        centerDeck.add(loginView);
        centerDeck.add(signUpView);
        centerDeck.add(statisticView);
        centerDeck.add(startView);

        centerDeck.showWidget(startView);
    }

    public void addClickHandlers (ClickHandler clickHandler){
        tilmeldingBtn.addClickHandler(clickHandler);
        statistiskBtn.addClickHandler(clickHandler);
        logindBtn.addClickHandler(clickHandler);
        logoImg.addClickHandler(clickHandler);
        loginView.getLoginBtn().addClickHandler(clickHandler);
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

//    Overload
    public void changeView(Widget widget){
        centerDeck.showWidget(widget);
    }

    public LoginView getLoginView() {
        return loginView;
    }

    public SignUpView getSignUpView() {
        return signUpView;
    }

    public StatisticView getStatisticView() {
        return statisticView;
    }

    public StartView getStartView() {
        return startView;
    }
}
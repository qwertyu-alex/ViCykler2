package client.ui.participant;

import client.ui.participant.widgets.CreateTeamView;
import client.ui.participant.widgets.MyProfileView;
import client.ui.participant.widgets.MyTeamView;
import client.ui.participant.widgets.StartView;
import client.ui.sharedWidgets.StatisticView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class ParticipantView extends Composite {
    interface ParticipantViewUiBinder extends UiBinder<HTMLPanel, ParticipantView> {
    }

    private static ParticipantViewUiBinder ourUiBinder = GWT.create(ParticipantViewUiBinder.class);


    @UiField
    DeckLayoutPanel centerDeck;

    @UiField
    Button myProfileBtn, statistiskBtn, logoutBtn, myTeamBtn, createTeamBtn;

    @UiField
    HorizontalPanel myTeamBox, createTeamBox;

    StartView startView;
    CreateTeamView createTeamView;
    MyProfileView myProfileView;
    StatisticView statisticView;
    MyTeamView myTeamView;

    public ParticipantView() {
        initWidget(ourUiBinder.createAndBindUi(this));

        startView = new StartView();
        myProfileView = new MyProfileView();
        createTeamView = new CreateTeamView();
        myTeamView = new MyTeamView();

        centerDeck.add(startView);
        centerDeck.add(myProfileView);
        centerDeck.add(createTeamView);
        centerDeck.add(myTeamView);

        centerDeck.showWidget(startView);
    }

    public void changeView(Widget widget){
        centerDeck.showWidget(widget);
    }

    public void addClickHandlers(ClickHandler clickHandler){
        myProfileBtn.addClickHandler(clickHandler);
        statistiskBtn.addClickHandler(clickHandler);
        logoutBtn.addClickHandler(clickHandler);
        myTeamBtn.addClickHandler(clickHandler);
        createTeamBtn.addClickHandler(clickHandler);
    }

    public DeckLayoutPanel getCenterDeck() {
        return centerDeck;
    }

    public Button getMyProfileBtn() {
        return myProfileBtn;
    }

    public Button getStatistiskBtn() {
        return statistiskBtn;
    }

    public Button getLogoutBtn() {
        return logoutBtn;
    }

    public StartView getStartView() {
        return startView;
    }

    public Button getMyTeamBtn() {
        return myTeamBtn;
    }

    public Button getCreateTeamBtn() {
        return createTeamBtn;
    }

    public HorizontalPanel getMyTeamBox() {
        return myTeamBox;
    }

    public HorizontalPanel getCreateTeamBox() {
        return createTeamBox;
    }

    public CreateTeamView getCreateTeamView() {
        return createTeamView;
    }

    public StatisticView getStatisticView() {
        return statisticView;
    }

    public MyTeamView getMyTeamView() {
        return myTeamView;
    }

    public MyProfileView getMyProfileView() {
        return myProfileView;
    }
}
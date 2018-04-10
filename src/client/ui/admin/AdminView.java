package client.ui.admin;

import client.ui.admin.widgets.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class AdminView extends Composite {
    interface AdminViewUiBinder extends UiBinder<HTMLPanel, AdminView> {
    }

    @UiField
    DeckLayoutPanel centerDeck;

    @UiField
    Button participantsBtn, teamsBtn, firmsBtn, logoutBtn;

    ShowParticipantsView showParticipantsView;
    ShowFirmsView showFirmsView;
    ShowTeamsView showTeamsView;
    ChangeParticipantView changeParticipantView;
    ChangeTeamView changeTeamView;
    ChangeFirmView changeFirmView;

    private static AdminViewUiBinder ourUiBinder = GWT.create(AdminViewUiBinder.class);

    public AdminView() {
        initWidget(ourUiBinder.createAndBindUi(this));
        showParticipantsView = new ShowParticipantsView();
        showFirmsView = new ShowFirmsView();
        showTeamsView = new ShowTeamsView();
        changeParticipantView = new ChangeParticipantView();
        changeTeamView = new ChangeTeamView();
        changeFirmView = new ChangeFirmView();


        centerDeck.add(showParticipantsView);
        centerDeck.add(showFirmsView);
        centerDeck.add(showTeamsView);
        centerDeck.add(changeParticipantView);
        centerDeck.add(changeTeamView);
        centerDeck.add(changeFirmView);
    }

    public void addClickHandlers(ClickHandler clickHandler){
        participantsBtn.addClickHandler(clickHandler);
        teamsBtn.addClickHandler(clickHandler);
        firmsBtn.addClickHandler(clickHandler);
        logoutBtn.addClickHandler(clickHandler);
    }

    public void changeView(Widget widget){
        centerDeck.showWidget(widget);
    }

    public DeckLayoutPanel getCenterDeck() {
        return centerDeck;
    }

    public Button getParticipantsBtn() {
        return participantsBtn;
    }

    public Button getTeamsBtn() {
        return teamsBtn;
    }

    public Button getFirmsBtn() {
        return firmsBtn;
    }

    public Button getLogoutBtn() {
        return logoutBtn;
    }

    public ShowParticipantsView getShowParticipantsView() {
        return showParticipantsView;
    }

    public ShowFirmsView getShowFirmsView() {
        return showFirmsView;
    }

    public ShowTeamsView getShowTeamsView() {
        return showTeamsView;
    }

    public ChangeParticipantView getChangeParticipantView() {
        return changeParticipantView;
    }

    public ChangeTeamView getChangeTeamView() {
        return changeTeamView;
    }

    public ChangeFirmView getChangeFirmView() {
        return changeFirmView;
    }
}
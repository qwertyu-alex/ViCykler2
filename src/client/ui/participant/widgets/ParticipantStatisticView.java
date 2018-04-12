package client.ui.participant.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class ParticipantStatisticView extends Composite {
    interface ParticipantStatisticViewUiBinder extends UiBinder<HTMLPanel, ParticipantStatisticView> {
    }

    @UiField
    VerticalPanel statisticPanel, resFirmPanel, resTeamPanel, resParticipantPanel;

    @UiField
    DeckPanel firmSearchDeck, teamSearchDeck, participantSearchDeck;

    @UiField ListBox firmsList1, firmsList2, teamList;

    @UiField TextBox participantEmailField;

    @UiField Button submitFirmBtn, submitTeamBtn, submitParticipantBtn;

    @UiField Label searchParticipantPanelErrLabel;



    private static ParticipantStatisticViewUiBinder ourUiBinder = GWT.create(ParticipantStatisticViewUiBinder.class);

    public ParticipantStatisticView() {
        initWidget(ourUiBinder.createAndBindUi(this));

        firmSearchDeck.showWidget(0);
        teamSearchDeck.showWidget(0);
        participantSearchDeck.showWidget(0);

    }

    public void addClickHandlers(ClickHandler clickHandler){
        submitFirmBtn.addClickHandler(clickHandler);
        submitTeamBtn.addClickHandler(clickHandler);
        submitParticipantBtn.addClickHandler(clickHandler);
    }

    public VerticalPanel getStatisticPanel() {
        return statisticPanel;
    }

    public DeckPanel getFirmSearchDeck() {
        return firmSearchDeck;
    }

    public DeckPanel getTeamSearchDeck() {
        return teamSearchDeck;
    }

    public DeckPanel getParticipantSearchDeck() {
        return participantSearchDeck;
    }

    public VerticalPanel getResFirmPanel() {
        return resFirmPanel;
    }

    public VerticalPanel getResTeamPanel() {
        return resTeamPanel;
    }

    public VerticalPanel getResParticipantPanel() {
        return resParticipantPanel;
    }

    public ListBox getFirmsList1() {
        return firmsList1;
    }

    public ListBox getFirmsList2() {
        return firmsList2;
    }

    public ListBox getTeamList() {
        return teamList;
    }

    public TextBox getParticipantEmailField() {
        return participantEmailField;
    }

    public Button getSubmitFirmBtn() {
        return submitFirmBtn;
    }

    public Button getSubmitTeamBtn() {
        return submitTeamBtn;
    }

    public Button getSubmitParticipantBtn() {
        return submitParticipantBtn;
    }

    public Label getSearchParticipantPanelErrLabel() {
        return searchParticipantPanelErrLabel;
    }
}
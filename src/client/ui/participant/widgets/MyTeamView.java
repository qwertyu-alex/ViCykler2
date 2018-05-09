package client.ui.participant.widgets;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.*;
import shared.DTO.Participant;

public class MyTeamView extends Composite {
    interface MyTeamViewUiBinder extends UiBinder<HTMLPanel, MyTeamView> {
    }

    @UiField
    Label teamIDLabel, teamNameLabel, numberOfParticipantsLabel, firmNameLabel;

    @UiField
    CellTable<Participant> cellTable;

    @UiField
    SimplePager simplePager;

    @UiField
    VerticalPanel changeTeam;

    @UiField TextBox addParticipantField, changeTeamNameField;

    @UiField Button submitBtn, deleteTeamBtn;

    private ActionCell.Delegate<Participant> delegate;

    private static MyTeamViewUiBinder ourUiBinder = GWT.create(MyTeamViewUiBinder.class);

    public MyTeamView() {
        initWidget(ourUiBinder.createAndBindUi(this));
        cellTable.setVisibleRange(0,25);
        simplePager.setDisplay(cellTable);
        simplePager.setPageSize(25);
    }

    public void addTeamCaptainClickHandler(ClickHandler clickHandler){
        submitBtn.addClickHandler(clickHandler);
        deleteTeamBtn.addClickHandler(clickHandler);
    }

    public Label getTeamIDLabel() {
        return teamIDLabel;
    }

    public Label getTeamNameLabel() {
        return teamNameLabel;
    }

    public Label getNumberOfParticipantsLabel() {
        return numberOfParticipantsLabel;
    }

    public CellTable<Participant> getCellTable() {
        return cellTable;
    }

    public Label getFirmNameLabel() {
        return firmNameLabel;
    }

    public ActionCell.Delegate<Participant> getDelegate() {
        return delegate;
    }

    public void setDelegate(ActionCell.Delegate<Participant> delegate) {
        this.delegate = delegate;
    }

    public VerticalPanel getChangeTeam() {
        return changeTeam;
    }

    public TextBox getAddParticipantField() {
        return addParticipantField;
    }

    public TextBox getChangeTeamNameField() {
        return changeTeamNameField;
    }

    public Button getSubmitBtn() {
        return submitBtn;
    }
    /**
     * Lav en ekstra kolonne hvis det er en holdkaptain
     */

    public Button getDeleteTeamBtn() {
        return deleteTeamBtn;
    }
}
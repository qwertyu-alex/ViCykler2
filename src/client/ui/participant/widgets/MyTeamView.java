package client.ui.participant.widgets;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ListDataProvider;
import shared.DTO.Participant;

public class MyTeamView extends Composite {
    interface MyTeamViewUiBinder extends UiBinder<HTMLPanel, MyTeamView> {
    }

    ListDataProvider<Participant> participantListDataProvider;

    @UiField
    Label teamIDLabel, teamNameLabel, numberOfParticipantsLabel, firmNameLabel;

    @UiField
    CellTable<Participant> cellTable;

    @UiField
    SimplePager simplePager;

    private ActionCell.Delegate<Participant> delegate;
    private boolean tableIsMade;

    private static MyTeamViewUiBinder ourUiBinder = GWT.create(MyTeamViewUiBinder.class);

    public MyTeamView() {
        initWidget(ourUiBinder.createAndBindUi(this));
        cellTable.setVisibleRange(0,25);
        simplePager.setDisplay(cellTable);
        simplePager.setPageSize(25);
    }

    public void initTable(ListDataProvider<Participant> participantListDataProvider){
        this.participantListDataProvider = participantListDataProvider;
        participantListDataProvider.addDataDisplay(cellTable);

        if (!tableIsMade){
            TextColumn<Participant> participantNameCol = new TextColumn<Participant>() {
                @Override
                public String getValue(Participant object) {
                    return object.getName();
                }
            };

            TextColumn<Participant> emailCol = new TextColumn<Participant>() {
                @Override
                public String getValue(Participant object) {
                    return object.getEmail();
                }
            };

            Column<Participant, Participant> removeParticipantCol = new Column<Participant, Participant>(new ActionCell<>("Fjern fra hold", delegate )) {
                @Override
                public Participant getValue(Participant object) {
                    return object;
                }
            };

            cellTable.addColumn(participantNameCol, "Deltager");
            cellTable.addColumn(emailCol, "Email");
            cellTable.addColumn(removeParticipantCol);

            tableIsMade = true;
        }
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

    public SimplePager getSimplePager() {
        return simplePager;
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
}
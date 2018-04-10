package client.ui.admin.widgets;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.ListDataProvider;
import shared.DTO.Team;

public class ShowTeamsView extends Composite {
    interface ShowTeamsViewUiBinder extends UiBinder<HTMLPanel, ShowTeamsView> {
    }

    private static ShowTeamsViewUiBinder ourUiBinder = GWT.create(ShowTeamsViewUiBinder.class);
    private ListDataProvider<Team> teamListDataProvider;
    private boolean tableIsMade;

    private TextColumn<Team> teamName, firmName;
    private Column<Team, Number> teamID, numberOfParticipants;
    private Column<Team, Team> changeTeam;

    private ActionCell.Delegate<Team> delegate;

    @UiField
    CellTable<Team> cellTable;

    public ShowTeamsView() {
        initWidget(ourUiBinder.createAndBindUi(this));
        cellTable.setVisibleRange(0, 1000);

    }

    public void initTable(ListDataProvider<Team> teamListDataProvider){
        this.teamListDataProvider = teamListDataProvider;
        this.teamListDataProvider.addDataDisplay(cellTable);

        if (!tableIsMade){

            teamID = new Column<Team, Number>(new NumberCell()) {
                @Override
                public Integer getValue(Team object) {
                    return  object.getTeamID();
                }
            };


            teamName = new TextColumn<Team>() {
                @Override
                public String getValue(Team object) {
                    return object.getTeamName();
                }
            };

            firmName = new TextColumn<Team>() {
                @Override
                public String getValue(Team object) {
                    return object.getFirmName();
                }
            };
//
            numberOfParticipants = new Column<Team, Number>(new NumberCell()) {
                @Override
                public Number getValue(Team object) {
                    return object.getParticipants().size();
                }
            };

            changeTeam = new Column<Team, Team>(new ActionCell<Team>("Rediger", delegate)) {
                @Override
                public Team getValue(Team object) {
                    return object;
                }
            };

            cellTable.addColumn(teamID, "Hold ID");
            cellTable.addColumn(teamName, "Holdnavn");
            cellTable.addColumn(firmName, "Firma");
            cellTable.addColumn(numberOfParticipants, "Antal deltagere");
            cellTable.addColumn(changeTeam);

            tableIsMade = true;
        }
    }

    public ListDataProvider<Team> getTeamListDataProvider() {
        return teamListDataProvider;
    }

    public boolean isTableIsMade() {
        return tableIsMade;
    }

    public TextColumn<Team> getTeamName() {
        return teamName;
    }

    public TextColumn<Team> getFirmName() {
        return firmName;
    }

    public Column<Team, Number> getTeamID() {
        return teamID;
    }

    public Column<Team, Number> getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public Column<Team, Team> getChangeTeam() {
        return changeTeam;
    }

    public ActionCell.Delegate<Team> getDelegate() {
        return delegate;
    }

    public CellTable<Team> getCellTable() {
        return cellTable;
    }

    public void setDelegate(ActionCell.Delegate<Team> delegate) {
        this.delegate = delegate;
    }
}
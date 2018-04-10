package client.ui.admin.widgets;

import com.google.gwt.cell.client.*;
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
import shared.DTO.Participant;
import shared.DTO.Person;

public class ShowParticipantsView extends Composite {
    interface ShowParticipantsViewUiBinder extends UiBinder<HTMLPanel, ShowParticipantsView> {
    }

    private static ShowParticipantsViewUiBinder ourUiBinder = GWT.create(ShowParticipantsViewUiBinder.class);
    private ListDataProvider<Participant> participantListDataProvider;

    private ClickableTextCell clickableTextCell;
    private TextColumn<Participant> nameCol, emailCol, personTypeCol, cyclistTypeCol, firmNameCol, teamNameCol;
    private Column<Participant, Participant> changeParticipant;
    private Column<Participant, String> passCol;
    private Column<Participant, Number> teamIDCol;
    private boolean tableIsMade;

    private ActionCell.Delegate<Participant> delegate;

    @UiField
    CellTable<Participant> cellTable;

    public ShowParticipantsView() {
        initWidget(ourUiBinder.createAndBindUi(this));
        clickableTextCell = new ClickableTextCell();
        cellTable.setVisibleRange(0,1000);
    }

    public void initTable(ListDataProvider<Participant> participantListDataProvider){
        this.participantListDataProvider = participantListDataProvider;
        participantListDataProvider.addDataDisplay(cellTable);
        //http://www.gwtproject.org/doc/latest/DevGuideUiCellWidgets.html


        /***
         * Koden forneden skal kun køre hvis tabellen ikke allerede er lavet.
         * Dette er for at forhindre, at den tilføjer nye kolonner hver gang admin logger ind
         */
        if (!tableIsMade) {
            nameCol = new TextColumn<Participant>() {
                @Override
                public String getValue(Participant object) {
                    return object.getName();
                }
            };

            emailCol = new TextColumn<Participant>() {
                @Override
                public String getValue(Participant object) {
                    return object.getEmail();
                }
            };

            passCol = new Column<Participant, String>(clickableTextCell) {
                @Override
                public String getValue(Participant object) {
                    return object.getPassword();
                }
            };

            personTypeCol = new TextColumn<Participant>() {
                @Override
                public String getValue(Participant object) {
                    return object.getPersonType();
                }
            };

            cyclistTypeCol = new TextColumn<Participant>() {
                @Override
                public String getValue(Participant object) {
                    return object.getCyclistType();
                }
            };

            firmNameCol = new TextColumn<Participant>() {
                @Override
                public String getValue(Participant object) {
                    return object.getFirmName();
                }
            };

            teamIDCol = new Column<Participant, Number>(new NumberCell()) {
                @Override
                public Integer getValue(Participant object) {
                    return object.getTeamID();
                }
            };

            teamNameCol = new TextColumn<Participant>() {
                @Override
                public String getValue(Participant object) {
                    return object.getTeamName();
                }
            };

            changeParticipant = new Column<Participant, Participant>(new ActionCell<>("Rediger", delegate)) {
                @Override
                public Participant getValue(Participant object) {
                    return object;
                }
            };

            cellTable.addColumn(nameCol, "Navn");
            cellTable.addColumn(emailCol, "Email");
            cellTable.addColumn(personTypeCol, "Person-type");
            cellTable.addColumn(cyclistTypeCol, "Cyclist-type");
            cellTable.addColumn(passCol, "Password");
            cellTable.addColumn(firmNameCol, "Firma");
            cellTable.addColumn(teamIDCol, "Hold ID");
            cellTable.addColumn(teamNameCol, "Holdnavn");
            cellTable.addColumn(changeParticipant);

            tableIsMade = true;
        }
    }

    public ListDataProvider<Participant> getParticipantListDataProvider() {
        return participantListDataProvider;
    }

    public ClickableTextCell getClickableTextCell() {
        return clickableTextCell;
    }

    public TextColumn<Participant> getNameCol() {
        return nameCol;
    }

    public TextColumn<Participant> getEmailCol() {
        return emailCol;
    }

    public TextColumn<Participant> getPersonTypeCol() {
        return personTypeCol;
    }

    public TextColumn<Participant> getCyclistTypeCol() {
        return cyclistTypeCol;
    }

    public TextColumn<Participant> getFirmNameCol() {
        return firmNameCol;
    }

    public TextColumn<Participant> getTeamNameCol() {
        return teamNameCol;
    }

    public Column<Participant, String> getPassCol() {
        return passCol;
    }

    public Column<Participant, Number> getTeamIDCol() {
        return teamIDCol;
    }

    public CellTable<Participant> getCellTable() {
        return cellTable;
    }

    public void setDelegate(ActionCell.Delegate<Participant> delegate) {
        this.delegate = delegate;
    }
}
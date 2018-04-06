package client.ui.admin.widgets;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
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


    @UiField
    CellTable<Participant> cellTable;

    public ShowParticipantsView() {
        initWidget(ourUiBinder.createAndBindUi(this));
        cellTable.setVisibleRange(0,100);
    }

    public void initTable(ListDataProvider<Participant> participantListDataProvider){
        this.participantListDataProvider = participantListDataProvider;
        participantListDataProvider.addDataDisplay(cellTable);
        //http://www.gwtproject.org/doc/latest/DevGuideUiCellWidgets.html
        TextColumn<Participant> nameCol = new TextColumn<Participant>() {
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

        Column<Participant, String> passCol = new Column<Participant, String>(new ClickableTextCell()) {
            @Override
            public String getValue(Participant object) {
                return "****";
            }
        };

        //https://stackoverflow.com/questions/7854147/gwt-clickabletextcell
        passCol.setFieldUpdater(new FieldUpdater<Participant, String>() {
            @Override
            public void update(int index, Participant object, String value) {

            }
        });

        TextColumn<Participant> personTypeCol = new TextColumn<Participant>() {
            @Override
            public String getValue(Participant object) {
                return object.getPersonType();
            }
        };

        TextColumn<Participant> cyclistTypeCol = new TextColumn<Participant>() {
            @Override
            public String getValue(Participant object) {
                return object.getCyclistType();
            }
        };

        TextColumn<Participant> firmNameCol = new TextColumn<Participant>() {
            @Override
            public String getValue(Participant object) {
                return object.getFirmName();
            }
        };

        Column<Participant, Number> teamIDCol = new Column<Participant, Number>(new NumberCell()) {
            @Override
            public Integer getValue(Participant object) {
                return object.getTeamID();
            }
        };

        TextColumn<Participant> teamNameCol = new TextColumn<Participant>() {
            @Override
            public String getValue(Participant object) {
                return object.getTeamName();
            }
        };

        cellTable.addColumn(nameCol, "Navn");
        cellTable.addColumn(emailCol, "Email");
        cellTable.addColumn(personTypeCol, "Person");
        cellTable.addColumn(cyclistTypeCol, "Cyclist-type");
        cellTable.addColumn(passCol, "Password");
        cellTable.addColumn(firmNameCol, "Firma");
        cellTable.addColumn(teamIDCol, "Hold ID");
        cellTable.addColumn(teamNameCol, "Holdnavn");

    }

}
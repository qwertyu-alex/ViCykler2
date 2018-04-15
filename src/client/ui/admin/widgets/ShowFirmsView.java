package client.ui.admin.widgets;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;
import shared.DTO.Firm;

public class ShowFirmsView extends Composite {
    interface ShowFirmsViewUiBinder extends UiBinder<HTMLPanel, ShowFirmsView> {
    }

    private static ShowFirmsViewUiBinder ourUiBinder = GWT.create(ShowFirmsViewUiBinder.class);
    @UiField
    CellTable<Firm> cellTable;

    @UiField
    Button createFirmBtn;

    @UiField
    TextBox firmNameField;

    @UiField
    Label errField;

    ListDataProvider<Firm> firmListDataProvider;

    private TextColumn<Firm> firmName;
    private Column<Firm, Number> numberOfTeams, numberOfParticipants;
    private Column<Firm, Firm> changeFirm;

    private boolean isTableMade;

    private ActionCell.Delegate<Firm> delegate;

    //Firma navn, antal hold, antal deltagere,

    public ShowFirmsView() {
        initWidget(ourUiBinder.createAndBindUi(this));
        cellTable.setVisibleRange(0, 1000);
    }

    public void addClickHandler(ClickHandler clickHandler){
        createFirmBtn.addClickHandler(clickHandler);
    }

    public void initTable(ListDataProvider<Firm> firmListDataProvider) throws Exception{
        this.firmListDataProvider = firmListDataProvider;
        firmListDataProvider.addDataDisplay(cellTable);

//        Window.alert("Navn " + firmListDataProvider.getList().get(0).getParticipants().get(0));
//        Window.alert(Integer.toString((int)firmListDataProvider.getList().get(0).getParticipants().size()));

        if (!isTableMade){
            firmName = new TextColumn<Firm>() {
                @Override
                public String getValue(Firm object) {
                    return object.getFirmName();
                }
            };

            numberOfParticipants = new Column<Firm, Number>(new NumberCell()) {
                @Override
                public Integer getValue(Firm object) {
                    return object.getParticipants().size();
                }
            };

            numberOfTeams = new Column<Firm, Number>(new NumberCell()) {
                @Override
                public Number getValue(Firm object) {
                    return object.getTeams().size();
                }
            };

            changeFirm = new Column<Firm, Firm>(new ActionCell<Firm>("Rediger", delegate)) {
                @Override
                public Firm getValue(Firm object) {
                    return object;
                }
            };

            cellTable.addColumn(firmName, "Firmanavn");
            cellTable.addColumn(numberOfParticipants, "Antal deltagere");
            cellTable.addColumn(numberOfTeams, "Antal hold");
            cellTable.addColumn(changeFirm);

            isTableMade = true;
        }
    }

    public void setDelegate(ActionCell.Delegate<Firm> delegate) {
        this.delegate = delegate;
    }

    public CellTable<Firm> getCellTable() {
        return cellTable;
    }

    public TextColumn<Firm> getFirmName() {
        return firmName;
    }

    public Column<Firm, Number> getNumberOfTeams() {
        return numberOfTeams;
    }

    public Column<Firm, Number> getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public Column<Firm, Firm> getChangeFirm() {
        return changeFirm;
    }

    public TextBox getFirmNameField() {
        return firmNameField;
    }

    public Label getErrField() {
        return errField;
    }
}
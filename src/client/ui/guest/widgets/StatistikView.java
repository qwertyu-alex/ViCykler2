package client.ui.guest.widgets;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.ListDataProvider;
import shared.ParticipantDTO;

public class StatistikView extends Composite {
    interface StatistikViewUiBinder extends UiBinder<HTMLPanel, StatistikView> {
    }

    private static StatistikViewUiBinder ourUiBinder = GWT.create(StatistikViewUiBinder.class);

    @UiField
    DataGrid<ParticipantDTO> dataGrid;

    @UiField
    SimplePager pager;

    public StatistikView() {
        initWidget(ourUiBinder.createAndBindUi(this));

        pager.setDisplay(dataGrid);
        pager.setPage(25);
    }

    public void initTable(ListDataProvider<ParticipantDTO> dataProvider){
        dataProvider.addDataDisplay(dataGrid);
        initColums();
    }

    public void initColums(){
        Column<ParticipantDTO, String> emailColumn = new Column<ParticipantDTO, String>(new TextCell()) {
            @Override
            public String getValue(ParticipantDTO object) {
                return object.getEmail();
            }
        };

        Column<ParticipantDTO, String> typeColumn = new Column<ParticipantDTO, String>(new TextCell()) {
            @Override
            public String getValue(ParticipantDTO object) {
                return object.getType();
            }
        };

        Column<ParticipantDTO, String> genderColumn = new Column<ParticipantDTO, String>(new TextCell()) {
            @Override
            public String getValue(ParticipantDTO object) {
                if (object.getGender() == 'm'){
                    return "Male";
                } else if (object.getGender() == 'f'){
                    return "Female";
                } else {
                    return "null";
                }

            }
        };

        Column<ParticipantDTO, String> firstNameColumn = new Column<ParticipantDTO, String>(new TextCell()) {
            @Override
            public String getValue(ParticipantDTO object) {
                return object.getFirstName();
            }
        };

        Column<ParticipantDTO, String> lastNameColumn = new Column<ParticipantDTO, String>(new TextCell()) {
            @Override
            public String getValue(ParticipantDTO object) {
                return object.getLastName();
            }
        };

        Column<ParticipantDTO, Number> ageColumn = new Column<ParticipantDTO, Number>(new NumberCell()) {
            @Override
            public Number getValue(ParticipantDTO object) {
                return object.getAge();
            }
        };

        dataGrid.addColumn(emailColumn);
        dataGrid.setColumnWidth(emailColumn, 7, Style.Unit.PX);
        dataGrid.addColumn(typeColumn);
        dataGrid.setColumnWidth(typeColumn, 7, Style.Unit.PX);
        dataGrid.addColumn(genderColumn);
        dataGrid.setColumnWidth(genderColumn, 7, Style.Unit.PX);
        dataGrid.addColumn(firstNameColumn);
        dataGrid.setColumnWidth(firstNameColumn, 7, Style.Unit.PX);
        dataGrid.addColumn(lastNameColumn);
        dataGrid.setColumnWidth(lastNameColumn, 7, Style.Unit.PX);
        dataGrid.addColumn(ageColumn);
        dataGrid.setColumnWidth(ageColumn, 7, Style.Unit.PX);
    }


}
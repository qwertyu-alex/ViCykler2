package client.ui.guest.widgets;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.ListDataProvider;
import shared.Participant;

import javax.servlet.http.Part;
import java.util.Comparator;

public class StatistikView extends Composite {
    interface StatistikViewUiBinder extends UiBinder<HTMLPanel, StatistikView> {
    }

    private static StatistikViewUiBinder ourUiBinder = GWT.create(StatistikViewUiBinder.class);
    private ActionCell.Delegate<Participant> actionCell;

    @UiField
    DataGrid<Participant> dataGrid;
    @UiField
    SimplePager pager;

    public StatistikView() {
        initWidget(ourUiBinder.createAndBindUi(this));

        pager.setDisplay(dataGrid);
        pager.setPage(25);
    }

    public void initTable(ListDataProvider<Participant> dataProvider){
        dataProvider.addDataDisplay(dataGrid);

//        Laver et sortevent som sorterer kolonnerne i tabellen.
        ColumnSortEvent.ListHandler<Participant> sortHandler = new ColumnSortEvent.ListHandler<>(dataProvider.getList());
        initColums(sortHandler);

        dataGrid.addColumnSortHandler(sortHandler);
    }

    public void initColums(ColumnSortEvent.ListHandler<Participant> sortHandler){
        Column<Participant, String> emailColumn = new Column<Participant, String>(new TextCell()) {
            @Override
            public String getValue(Participant object) {
                return object.getEmail();
            }
        };

        Column<Participant, String> typeColumn = new Column<Participant, String>(new TextCell()) {
            @Override
            public String getValue(Participant object) {
                return object.getCyclistType();
            }
        };


        Column<Participant, String> firstNameColumn = new Column<Participant, String>(new TextCell()) {
            @Override
            public String getValue(Participant object) {
                return object.getName();
            }
        };

        dataGrid.addColumn(emailColumn, "Email");
        dataGrid.setColumnWidth(emailColumn, 7, Style.Unit.PX);
        emailColumn.setSortable(true);
        sortHandler.setComparator(emailColumn, Comparator.comparing(Participant::getEmail));

        dataGrid.addColumn(typeColumn, "Type");
        dataGrid.setColumnWidth(typeColumn, 7, Style.Unit.PX);
        typeColumn.setSortable(true);
        sortHandler.setComparator(typeColumn, Comparator.comparing(Participant::getCyclistType));

        dataGrid.addColumn(firstNameColumn, "Navn");
        dataGrid.setColumnWidth(firstNameColumn, 7, Style.Unit.PX);
        firstNameColumn.setSortable(true);
        sortHandler.setComparator(firstNameColumn, Comparator.comparing(Participant::getName));
    }

    public void addClickHandler(ActionCell.Delegate<Participant> button){
        this.actionCell = button;
    }


}
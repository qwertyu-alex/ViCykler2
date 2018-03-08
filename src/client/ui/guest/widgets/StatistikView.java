package client.ui.guest.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import shared.UserDTO;

public class StatistikView extends Composite {
    interface StatistikViewUiBinder extends UiBinder<HTMLPanel, StatistikView> {
    }

    private static StatistikViewUiBinder ourUiBinder = GWT.create(StatistikViewUiBinder.class);

    @UiField
    DataGrid<UserDTO> dataGrid;

    @UiField
    SimplePager pager;

    public StatistikView() {
        initWidget(ourUiBinder.createAndBindUi(this));

        pager.setDisplay(dataGrid);
        pager.setPage(25);
    }


}
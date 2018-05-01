package client.ui.admin.widgets;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import shared.DTO.Team;

public class ShowTeamsView extends Composite {
    interface ShowTeamsViewUiBinder extends UiBinder<HTMLPanel, ShowTeamsView> {
    }

    private static ShowTeamsViewUiBinder ourUiBinder = GWT.create(ShowTeamsViewUiBinder.class);

    private ActionCell.Delegate<Team> delegate;

    @UiField
    CellTable<Team> cellTable;

    public ShowTeamsView() {
        initWidget(ourUiBinder.createAndBindUi(this));
        cellTable.setVisibleRange(0, 1000);

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
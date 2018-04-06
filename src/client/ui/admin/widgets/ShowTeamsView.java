package client.ui.admin.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class ShowTeamsView extends Composite {
    interface ShowTeamsViewUiBinder extends UiBinder<HTMLPanel, ShowTeamsView> {
    }

    private static ShowTeamsViewUiBinder ourUiBinder = GWT.create(ShowTeamsViewUiBinder.class);

    public ShowTeamsView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
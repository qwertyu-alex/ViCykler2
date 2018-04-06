package client.ui.admin.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class ShowFirmsView extends Composite {
    interface ShowFirmsViewUiBinder extends UiBinder<HTMLPanel, ShowFirmsView> {
    }

    private static ShowFirmsViewUiBinder ourUiBinder = GWT.create(ShowFirmsViewUiBinder.class);

    public ShowFirmsView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
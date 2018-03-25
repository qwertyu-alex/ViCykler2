package client.ui.guest.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class StartView extends Composite {
    interface StartViewUiBinder extends UiBinder<HTMLPanel, StartView> {
    }

    private static StartViewUiBinder ourUiBinder = GWT.create(StartViewUiBinder.class);

    public StartView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
package client.ui.participant.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;

public class StartView extends Composite {
    interface StartViewUiBinder extends UiBinder<HTMLPanel, StartView> {
    }

    private static StartViewUiBinder ourUiBinder = GWT.create(StartViewUiBinder.class);

    @UiField HTMLPanel Panel;
    @UiField Label introMessage;

    public StartView() {
        initWidget(ourUiBinder.createAndBindUi(this));

    }

}
package client.ui.participant.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class ChangeTeamView extends Composite {
    interface ChangeTeamViewUiBinder extends UiBinder<HTMLPanel, ChangeTeamView> {
    }

    private static ChangeTeamViewUiBinder ourUiBinder = GWT.create(ChangeTeamViewUiBinder.class);

    public ChangeTeamView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
package client.ui.participant.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class CreateTeamView extends Composite {
    interface CreateTeamViewUiBinder extends UiBinder<HTMLPanel, CreateTeamView> {
    }

    private static CreateTeamViewUiBinder ourUiBinder = GWT.create(CreateTeamViewUiBinder.class);

    public CreateTeamView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
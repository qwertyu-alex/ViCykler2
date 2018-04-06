package client.ui.participant.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class MyTeamView extends Composite {
    interface MyTeamViewUiBinder extends UiBinder<HTMLPanel, MyTeamView> {
    }

    private static MyTeamViewUiBinder ourUiBinder = GWT.create(MyTeamViewUiBinder.class);

    public MyTeamView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
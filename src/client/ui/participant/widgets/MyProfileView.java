package client.ui.participant.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class MyProfileView extends Composite {
    interface MyProfileViewUiBinder extends UiBinder<HTMLPanel, MyProfileView> {
    }

    private static MyProfileViewUiBinder ourUiBinder = GWT.create(MyProfileViewUiBinder.class);

    @UiField Label nameLabel, emailLabel, cyclistTypeLabel, firmLabel, teamLabel;

    public MyProfileView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public Label getNameLabel() {
        return nameLabel;
    }

    public Label getEmailLabel() {
        return emailLabel;
    }

    public Label getCyclistTypeLabel() {
        return cyclistTypeLabel;
    }

    public Label getFirmLabel() {
        return firmLabel;
    }

    public Label getTeamLabel() {
        return teamLabel;
    }
}
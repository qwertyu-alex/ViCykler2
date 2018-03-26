package client.ui.participant.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.*;

public class MyProfileView extends Composite {
    interface MyProfileViewUiBinder extends UiBinder<HTMLPanel, MyProfileView> {
    }

    private static MyProfileViewUiBinder ourUiBinder = GWT.create(MyProfileViewUiBinder.class);

    private VerticalPanel verticalPanel;
    private HorizontalPanel horizontalPanel;
    private Label usernameLabel, emailLabel, cyclistTypeLabel, firmLabel, teamLabel;

    public MyProfileView() {
        initWidget(ourUiBinder.createAndBindUi(this));




    }
}
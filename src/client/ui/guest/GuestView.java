package client.ui.guest;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class GuestView extends Composite {
    interface guestViewUiBinder extends UiBinder<HTMLPanel, GuestView> {
    }

    private static guestViewUiBinder ourUiBinder = GWT.create(guestViewUiBinder.class);

    @UiField
    HTMLPanel southPanel, centerPanel, northPanel;

    private LoginView loginView;

    public GuestView() {
        initWidget(ourUiBinder.createAndBindUi(this));

        loginView = new LoginView();

        centerPanel.add(loginView);
    }
}
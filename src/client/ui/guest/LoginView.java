package client.ui.guest;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;

public class loginView extends Composite {
    interface loginViewUiBinder extends UiBinder<com.google.gwt.user.client.ui.HTMLPanel, client.ui.guest.loginView> {
    }

    private static loginViewUiBinder ourUiBinder = GWT.create(loginViewUiBinder.class);

    public loginView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
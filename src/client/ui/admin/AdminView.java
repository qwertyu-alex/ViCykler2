package client.ui.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class AdminView extends Composite {
    interface AdminViewUiBinder extends UiBinder<HTMLPanel, AdminView> {
    }

    private static AdminViewUiBinder ourUiBinder = GWT.create(AdminViewUiBinder.class);

    public AdminView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
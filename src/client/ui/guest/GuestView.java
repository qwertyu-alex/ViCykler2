package client.ui.guest;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;

public class guestView extends Composite {
    interface guestViewUiBinder extends UiBinder<com.google.gwt.user.client.ui.HTMLPanel, client.ui.guest.guestView> {
    }

    private static guestViewUiBinder ourUiBinder = GWT.create(guestViewUiBinder.class);

    public guestView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
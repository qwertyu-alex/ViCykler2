package client.ui.sharedWidgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;

public class Footer extends Composite {
    interface FooterUiBinder extends UiBinder<com.google.gwt.user.client.ui.HTMLPanel, client.ui.sharedWidgets.Footer> {
    }

    private static FooterUiBinder ourUiBinder = GWT.create(FooterUiBinder.class);

    public Footer() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
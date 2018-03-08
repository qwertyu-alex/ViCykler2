package client.ui.sharedWidgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class Footer extends Composite {
    interface FooterUiBinder extends UiBinder<HTMLPanel, Footer> {
    }

    private static FooterUiBinder ourUiBinder = GWT.create(FooterUiBinder.class);

    public Footer() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
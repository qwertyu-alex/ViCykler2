package client.ui.sharedWidgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;

public class Header extends Composite {
    interface HeaderUiBinder extends UiBinder<com.google.gwt.user.client.ui.HTMLPanel, client.ui.sharedWidgets.Header> {
    }

    private static HeaderUiBinder ourUiBinder = GWT.create(HeaderUiBinder.class);

    public Header() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
package client.ui.guest.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class GuestStatisticView extends Composite {
    interface GuestStatisticViewUiBinder extends UiBinder<HTMLPanel, GuestStatisticView> {
    }

    private static GuestStatisticViewUiBinder ourUiBinder = GWT.create(GuestStatisticViewUiBinder.class);

    public GuestStatisticView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
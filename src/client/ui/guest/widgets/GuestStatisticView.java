package client.ui.guest.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GuestStatisticView extends Composite {
    interface GuestStatisticViewUiBinder extends UiBinder<HTMLPanel, GuestStatisticView> {
    }

    @UiField
    VerticalPanel statisticPanel;

    private static GuestStatisticViewUiBinder ourUiBinder = GWT.create(GuestStatisticViewUiBinder.class);

    public GuestStatisticView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public VerticalPanel getStatisticPanel() {
        return statisticPanel;
    }
}
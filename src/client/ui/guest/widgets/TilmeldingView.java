package client.ui.guest.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class TilmeldingView extends Composite {
    interface TilmeldingUiBinder extends UiBinder<HTMLPanel, TilmeldingView> {
    }

    private static TilmeldingUiBinder ourUiBinder = GWT.create(TilmeldingUiBinder.class);

    public TilmeldingView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
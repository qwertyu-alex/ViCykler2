package client.ui.participant;

import client.ui.participant.widgets.StartView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;

public class ParticipantView extends Composite {
    interface ParticipantViewUiBinder extends UiBinder<HTMLPanel, ParticipantView> {
    }

    private static ParticipantViewUiBinder ourUiBinder = GWT.create(ParticipantViewUiBinder.class);


    @UiField
    DeckLayoutPanel centerDeck;

    @UiField
    Button myProfileBtn, statistiskBtn, logoutBtn;

    StartView startView;

    public ParticipantView() {
        initWidget(ourUiBinder.createAndBindUi(this));

        startView = new StartView();

        centerDeck.add(startView);

        centerDeck.showWidget(startView);
    }
}
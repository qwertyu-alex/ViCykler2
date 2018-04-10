package client.ui.participant.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class CreateTeamView extends Composite {
    interface CreateTeamViewUiBinder extends UiBinder<HTMLPanel, CreateTeamView> {
    }

    @UiField
    TextBox teamNameField;

    @UiField
    TextArea teamMembersField;

    @UiField Button submitBtn;

    private static CreateTeamViewUiBinder ourUiBinder = GWT.create(CreateTeamViewUiBinder.class);

    public CreateTeamView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void addClickHandlers(ClickHandler clickHandler){
        submitBtn.addClickHandler(clickHandler);
    }

    public TextBox getTeamNameField() {
        return teamNameField;
    }

    public TextArea getTeamMembersField() {
        return teamMembersField;
    }

    public Button getSubmitBtn() {
        return submitBtn;
    }
}
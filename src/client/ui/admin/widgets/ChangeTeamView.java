package client.ui.admin.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;


public class ChangeTeamView extends Composite {
    interface ChangeTeamViewUiBinder extends UiBinder<HTMLPanel, ChangeTeamView> {
    }

    @UiField
    TextBox teamNameField;

    @UiField
    Label idLabel;

    @UiField
    Button submitBtn, returnBtn, deleteBtn;

    private static ChangeTeamViewUiBinder ourUiBinder = GWT.create(ChangeTeamViewUiBinder.class);

    public ChangeTeamView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void addClickhandlers(ClickHandler clickHandler){
        submitBtn.addClickHandler(clickHandler);
        returnBtn.addClickHandler(clickHandler);
        deleteBtn.addClickHandler(clickHandler);

    }

    public TextBox getTeamNameField() {
        return teamNameField;
    }

    public Button getSubmitBtn() {
        return submitBtn;
    }

    public Button getReturnBtn() {
        return returnBtn;
    }

    public Button getDeleteBtn() {
        return deleteBtn;
    }

    public Label getIdLabel() {
        return idLabel;
    }
}
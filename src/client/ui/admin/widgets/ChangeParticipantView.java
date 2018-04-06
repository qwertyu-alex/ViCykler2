package client.ui.admin.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class ChangeParticipantView extends Composite {
    interface ChangeParticipantViewUiBinder extends UiBinder<HTMLPanel, ChangeParticipantView> {
    }

    private static ChangeParticipantViewUiBinder ourUiBinder = GWT.create(ChangeParticipantViewUiBinder.class);

    @UiField
    Button returnBtn, submitBtn;

    @UiField
    TextBox nameField, emailField, personTypeField, cyclistTypeField, firmNameField, teamNameField;

    @UiField PasswordTextBox passField;

    public ChangeParticipantView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void addClickHandlers(ClickHandler clickHandler){
        submitBtn.addClickHandler(clickHandler);
        returnBtn.addClickHandler(clickHandler);
    }

    public Button getReturnBtn() {
        return returnBtn;
    }

    public Button getSubmitBtn() {
        return submitBtn;
    }

    public TextBox getNameField() {
        return nameField;
    }

    public TextBox getEmailField() {
        return emailField;
    }

    public TextBox getPersonTypeField() {
        return personTypeField;
    }

    public TextBox getCyclistTypeField() {
        return cyclistTypeField;
    }

    public TextBox getFirmNameField() {
        return firmNameField;
    }

    public TextBox getTeamNameField() {
        return teamNameField;
    }

    public PasswordTextBox getPassField() {
        return passField;
    }
}
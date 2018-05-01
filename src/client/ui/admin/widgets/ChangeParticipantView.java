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
    Button returnBtn, submitBtn, deleteBtn;

    @UiField
    TextBox nameField, emailField, firmNameField, teamNameField;

    @UiField ListBox cyclistTypeList, personTypeList;

    @UiField PasswordTextBox passField;

    @UiField Label idLabel;

    public ChangeParticipantView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void addClickHandlers(ClickHandler clickHandler){
        submitBtn.addClickHandler(clickHandler);
        returnBtn.addClickHandler(clickHandler);
        deleteBtn.addClickHandler(clickHandler);
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


    public TextBox getFirmNameField() {
        return firmNameField;
    }

    public TextBox getTeamNameField() {
        return teamNameField;
    }

    public PasswordTextBox getPassField() {
        return passField;
    }

    public Label getIdLabel() {
        return idLabel;
    }

    public Button getDeleteBtn() {
        return deleteBtn;
    }

    public ListBox getCyclistTypeList() {
        return cyclistTypeList;
    }

    public ListBox getPersonTypeList() {
        return personTypeList;
    }
}
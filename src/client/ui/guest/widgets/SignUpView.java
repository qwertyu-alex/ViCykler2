package client.ui.guest.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class SignUpView extends Composite {
    interface TilmeldingUiBinder extends UiBinder<HTMLPanel, SignUpView> {
    }

    @UiField Button signUpBtn;

    @UiField
    TextBox emailField, nameField;

    @UiField
    ListBox cyclistTypeList;

    @UiField PasswordTextBox passwordField, passwordCheckField;

    @UiField HTML errorMessageLabel;

    private static TilmeldingUiBinder ourUiBinder = GWT.create(TilmeldingUiBinder.class);


    public void addClickHandlers(ClickHandler clickHandler){
        signUpBtn.addClickHandler(clickHandler);
    }

    public Button getSignUpBtn() {
        return signUpBtn;
    }

    public SignUpView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public TextBox getEmailField() {
        return emailField;
    }

    public TextBox getNameField() {
        return nameField;
    }

    public ListBox getCyclistTypeList() {
        return cyclistTypeList;
    }

    public PasswordTextBox getPasswordField() {
        return passwordField;
    }

    public PasswordTextBox getPasswordCheckField() {
        return passwordCheckField;
    }

    public HTML getErrorMessageLabel() {
        return errorMessageLabel;
    }
}
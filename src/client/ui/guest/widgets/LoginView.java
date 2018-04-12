package client.ui.guest.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class LoginView extends Composite {
    interface loginViewUiBinder extends UiBinder<HTMLPanel, LoginView> {
    }

    private static loginViewUiBinder ourUiBinder = GWT.create(loginViewUiBinder.class);

    private Label email, password, errMessageLabel;
    private TextBox usernameTB, passwordTB;
    private Button loginBtn;
    private VerticalPanel vertPanel, errPanel;


    @UiField HTMLPanel HTMLPanel;

    public LoginView() {
        initWidget(ourUiBinder.createAndBindUi(this));

        vertPanel = new VerticalPanel();
        errPanel = new VerticalPanel();

        email = new Label("email");
        password = new Label("Password");
        usernameTB = new TextBox();
        passwordTB = new PasswordTextBox();
        loginBtn = new Button("Login");
        errMessageLabel = new Label();

        vertPanel.add(email);
        vertPanel.add(usernameTB);
        vertPanel.add(password);
        vertPanel.add(passwordTB);
        vertPanel.add(loginBtn);

        errPanel.add(errMessageLabel);

        vertPanel.addStyleName("center fakefakefakeBtn margintop");
        errPanel.addStyleName("center");

        HTMLPanel.add(vertPanel);
        HTMLPanel.add(errPanel);
    }

    public static loginViewUiBinder getOurUiBinder() {
        return ourUiBinder;
    }

    public void addClickHandler(ClickHandler clickHandler){
        loginBtn.addClickHandler(clickHandler);
    }

    public Label getEmail() {
        return email;
    }

    public Label getPassword() {
        return password;
    }

    public TextBox getUsernameTB() {
        return usernameTB;
    }

    public TextBox getPasswordTB() {
        return passwordTB;
    }

    public Button getLoginBtn() {
        return loginBtn;
    }

    public VerticalPanel getVertPanel() {
        return vertPanel;
    }

    public com.google.gwt.user.client.ui.HTMLPanel getHTMLPanel() {
        return HTMLPanel;
    }

    public Label getErrMessageLabel() {
        return errMessageLabel;
    }
}
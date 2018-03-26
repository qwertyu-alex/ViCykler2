package client.ui.guest.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class LoginView extends Composite {
    interface loginViewUiBinder extends UiBinder<HTMLPanel, LoginView> {
    }

    private static loginViewUiBinder ourUiBinder = GWT.create(loginViewUiBinder.class);

    private Label username, password;
    private TextBox usernameTB, passwordTB;
    private Button loginBtn;
    private VerticalPanel vertPanel;

    @UiField HTMLPanel HTMLPanel;

    public LoginView() {
        initWidget(ourUiBinder.createAndBindUi(this));

        vertPanel = new VerticalPanel();

        username = new Label("Username");
        password = new Label("Password");
        usernameTB = new TextBox();
        passwordTB = new TextBox();
        loginBtn = new Button("Login");

        vertPanel.add(username);
        vertPanel.add(usernameTB);
        vertPanel.add(password);
        vertPanel.add(passwordTB);
        vertPanel.add(loginBtn);

        vertPanel.addStyleName("center");

        HTMLPanel.add(vertPanel);
    }

    public static loginViewUiBinder getOurUiBinder() {
        return ourUiBinder;
    }

    public Label getUsername() {
        return username;
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
}
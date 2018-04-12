package client.ui.admin.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class ChangeFirmView extends Composite {
    interface ChangeFirmViewUiBinder extends UiBinder<HTMLPanel, ChangeFirmView> {
    }

    private static ChangeFirmViewUiBinder ourUiBinder = GWT.create(ChangeFirmViewUiBinder.class);

    @UiField
    Label firmIDLabel;
    @UiField
    TextBox firmNameField;
    @UiField
    Button submitBtn, returnBtn, deleteBtn;

    public ChangeFirmView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void addClickHandlers(ClickHandler clickHandler){
        submitBtn.addClickHandler(clickHandler);
        returnBtn.addClickHandler(clickHandler);
        deleteBtn.addClickHandler(clickHandler);
    }

    public Label getFirmIDLabel() {
        return firmIDLabel;
    }

    public TextBox getFirmNameField() {
        return firmNameField;
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
}
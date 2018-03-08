package client.appcontroller;

import client.ui.Content;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class GuestController {

    private Content content;

    public GuestController(Content content){
        this.content = content;


        content.getGuestView().addClickHandlers(new GuestClickHandlers());
    }

    class GuestClickHandlers implements ClickHandler{

        @Override
        public void onClick(ClickEvent event) {
            if (event.getSource() == content.getGuestView().getLogindBtn()){

            } else if (event.getSource() == content.getGuestView().getTilmeldingBtn()){
                content.getGuestView().changeView(1);
            } else if (event.getSource() == content.getGuestView().getLogoImg()){
                content.getGuestView().changeView(0);
            }
        }
    }
}

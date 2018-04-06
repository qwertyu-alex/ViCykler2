package client.appcontroller;

import client.ui.Content;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import rpc.ApplicationService;
import rpc.ApplicationServiceAsync;
import shared.DTO.Participant;

import java.util.ArrayList;
//import server.withoutDB.Data;

public class AdminController {

    private Content content;
    private ApplicationServiceAsync rpcService;

//    private Data data;

    public AdminController(Content content, ApplicationServiceAsync rpcService){
        this.content = content;
        this.rpcService = rpcService;
//        this.data = data;
        addClickhandlers();
        createParticipantsTable();
    }

    class AdminClickHandler implements ClickHandler{
        @Override
        public void onClick(ClickEvent event) {
            if (event.getSource() == content.getAdminView().getParticipantsBtn()){
                content.getAdminView().changeView(content.getAdminView().getShowParticipantsView());
            } else if (event.getSource() == content.getAdminView().getTeamsBtn()){

            } else if (event.getSource() == content.getAdminView().getFirmsBtn()){

            } else if (event.getSource() == content.getAdminView().getLogoutBtn()){
                content.switchToGuestView();
            }
        }
    }

    private void addClickhandlers(){
        content.getAdminView().addClickHandlers(new AdminClickHandler());
    }

    private void createParticipantsTable(){

        rpcService.getAllParticipants(new AsyncCallback<ArrayList<Participant>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ArrayList<Participant> result) {
                ListDataProvider<Participant> participantListDataProvider = new ListDataProvider<>();

                participantListDataProvider.getList().addAll(result);
                Window.alert(Integer.toString(participantListDataProvider.getList().size()) );

                content.getAdminView().getShowParticipantsView().initTable(participantListDataProvider);
            }
        });


    }



}

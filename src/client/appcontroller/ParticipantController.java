package client.appcontroller;

import client.ui.Content;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import rpc.ApplicationServiceAsync;
import server.ApplicationServiceImpl;
import shared.DTO.Participant;

import java.util.ArrayList;

public class ParticipantController {

    private Content content;
    private Participant currentParticipant;
    private ApplicationServiceAsync rpcService;
    private ListDataProvider<Participant> participantListDataProvider;

    public ParticipantController(Content content, Participant currentParticipant, ApplicationServiceAsync rpcService) {
        this.content = content;
        this.currentParticipant = currentParticipant;
        this.rpcService = rpcService;

        checkParticipantTeam();

        createTable();
        addClickhandler();
        createMyProfile();

    }

    public void checkParticipantTeam(){

        if (currentParticipant.getTeamID() == null)
            content.getParticipantView().getCreateTeamBox().removeStyleName("hidden");
    }

    private void addClickhandler(){
        content.getParticipantView().addClickHandlers(new ParticipantClickHandler());
    }

    class ParticipantClickHandler implements ClickHandler{
        @Override
        public void onClick(ClickEvent event) {

            if (event.getSource() == content.getParticipantView().getMyProfileBtn()){
                content.getParticipantView().changeView(content.getParticipantView().getMyProfileView());
            } else if (event.getSource() == content.getParticipantView().getStatistiskBtn()){
                content.getParticipantView().changeView(content.getParticipantView().getStatisticView());
            } else if (event.getSource() == content.getParticipantView().getCreateTeamBtn()){
                content.getParticipantView().changeView(content.getParticipantView().getCreateTeamView());
            } else if (event.getSource() == content.getParticipantView().getMyTeamBtn()){
                content.getParticipantView().changeView(content.getParticipantView().getMyTeamView());
            } else if (event.getSource() == content.getParticipantView().getLogoutBtn()){
                content.switchToGuestView();
            }
        }
    }

    private void createTable(){
        rpcService.getAllParticipants(new AsyncCallback<ArrayList<Participant>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(ArrayList<Participant> result) {
                content.getParticipantView().getStatisticView().initTable(participantListDataProvider);
                participantListDataProvider.getList().addAll(result);
            }
        });
    }

    private void createMyProfile(){

        //Sætter name
        rpcService.getParticipantName(currentParticipant.getEmail(), new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(String result) {
                if(result != null){
                    content.getParticipantView().getMyProfileView().getNameLabel().setText(result);
                }
            }
        });

        //Sætter email
        content.getParticipantView().getMyProfileView().getEmailLabel().setText(currentParticipant.getEmail());

        rpcService.getParticipantCyclistType(currentParticipant.getEmail(), new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(String result) {

            }
        });


    }



}

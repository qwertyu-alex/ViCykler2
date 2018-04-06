package client.appcontroller;

import client.ui.Content;
import client.ui.admin.AdminView;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.FieldUpdater;
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
    private Participant currentParticipant;
    private AdminView adminView;

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

    class ChangeParticipantDelegateHandler implements ActionCell.Delegate<Participant>{

        @Override
        public void execute(Participant object) {
            currentParticipant = object;

            content.getAdminView().getChangeParticipantView().getNameField().setText(currentParticipant.getName());
            content.getAdminView().getChangeParticipantView().getEmailField().setText(currentParticipant.getEmail());
            content.getAdminView().getChangeParticipantView().getPersonTypeField().setText(currentParticipant.getPersonType());
            content.getAdminView().getChangeParticipantView().getCyclistTypeField().setText(currentParticipant.getCyclistType());
            content.getAdminView().getChangeParticipantView().getPassField().setText(currentParticipant.getPassword());
            content.getAdminView().getChangeParticipantView().getFirmNameField().setText(currentParticipant.getFirmName());
            content.getAdminView().getChangeParticipantView().getTeamNameField().setText(currentParticipant.getTeamName());

            content.getAdminView().changeView(content.getAdminView().getChangeParticipantView());
        }
    }

    class ChangeParticipantClickHandler implements ClickHandler{
        @Override
        public void onClick(ClickEvent event) {
            if (event.getSource() == content.getAdminView().getChangeParticipantView().getSubmitBtn()){

                Participant changingParticipant = new Participant();

                changingParticipant.setName(content.getAdminView().getChangeParticipantView().getNameField().getText());
                changingParticipant.setEmail(content.getAdminView().getChangeParticipantView().getEmailField().getText());
                changingParticipant.setPersonType(content.getAdminView().getChangeParticipantView().getPersonTypeField().getText());
                changingParticipant.setCyclistType(content.getAdminView().getChangeParticipantView().getCyclistTypeField().getText());
                changingParticipant.setPassword(content.getAdminView().getChangeParticipantView().getPassField().getText());
                changingParticipant.setFirmName(content.getAdminView().getChangeParticipantView().getFirmNameField().getText());
                changingParticipant.setTeamName(content.getAdminView().getChangeParticipantView().getTeamNameField().getText());



            } else if (event.getSource() == content.getAdminView().getChangeParticipantView().getReturnBtn()){

            }
        }
    }

    private void addClickhandlers(){
        content.getAdminView().addClickHandlers(new AdminClickHandler());
        content.getAdminView().getShowParticipantsView().setDelegate(new ChangeParticipantDelegateHandler());
        content.getAdminView().getChangeParticipantView().addClickHandlers(new ChangeParticipantClickHandler());
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

                for (Participant par :
                        participantListDataProvider.getList()) {
                    par.setPassword("****");
                }


                content.getAdminView().getShowParticipantsView().initTable(participantListDataProvider);

                tableClickLogic();
            }
        });
    }

    /***
     * Denne metode sørger for at fjerne "****" fra password og sætter de rigtige passwords ind.
     */
    private void tableClickLogic(){
        content.getAdminView().getShowParticipantsView().getPassCol().setFieldUpdater(new FieldUpdater<Participant, String>() {
            @Override
            public void update(int index, Participant object, String value) {
                rpcService.getParticipantPassword(object.getEmail(), new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {

                    }

                    @Override
                    public void onSuccess(String result) {
                        object.setPassword(result);
                        content.getAdminView().getShowParticipantsView().getParticipantListDataProvider().getList().set(index, object);
                        content.getAdminView().getShowParticipantsView().getParticipantListDataProvider().refresh();
//                        content.getAdminView().getShowParticipantsView().getCellTable().redraw();

                    }
                });
            }
        });
    }

}

package client.appcontroller;

import client.ui.Content;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import rpc.ApplicationServiceAsync;
import server.ApplicationServiceImpl;
import shared.DTO.Participant;
import shared.DTO.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParticipantController {

    private Content content;
    private Participant currentParticipant;
    private Team currentTeam;
    private ApplicationServiceAsync rpcService;
    private ListDataProvider<Participant> participantListDataProvider;

    public ParticipantController(Content content, Participant currentParticipant, ApplicationServiceAsync rpcService) {
        this.content = content;
        this.currentParticipant = currentParticipant;
        this.rpcService = rpcService;

        findCurrentTeam();
        addClickhandlers();
        createParticipantView();

    }

    public void createParticipantView(){
        createMyProfile();
        checkParticipantTeam();
        createTable();
    }

    public void findCurrentTeam(){
        rpcService.getTeam(currentParticipant.getEmail(), new AsyncCallback<Team>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(Team result) {
                currentTeam = result;
            }
        });
    }

    public void checkParticipantTeam(){

        if (currentParticipant.getPersonType().equalsIgnoreCase("TEAMCAPTAIN")){
            content.getParticipantView().getChangeTeamBox().removeStyleName("hidden");
        }

        if (currentParticipant.getTeamID() == 0){
            content.getParticipantView().getCreateTeamBox().removeStyleName("hidden");
            content.getParticipantView().getMyTeamBox().addStyleName("hidden");
        }

        if (currentParticipant.getTeamID() != 0){
            content.getParticipantView().getMyTeamBox().removeStyleName("hidden");
            content.getParticipantView().getCreateTeamBox().addStyleName("hidden");
        }
    }

    private void addClickhandlers(){
        content.getParticipantView().addClickHandlers(new ParticipantClickHandler());
        content.getParticipantView().getCreateTeamView().addClickHandlers(new CreateTeamClickHandler());
        content.getParticipantView().getMyTeamView().setDelegate(new MyTeamDelegateHandler());
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
                /**
                 * Få fat i alle deltagere
                 */
                rpcService.getAllParticipants(new AsyncCallback<ArrayList<Participant>>() {
                    @Override
                    public void onFailure(Throwable caught) {

                    }

                    @Override
                    public void onSuccess(ArrayList<Participant> result) {

                        /**
                         * Klargør en listdataprovider til tabellen.
                         * Finder alle participants og evaluerer om deres TeamID er den samme som currentTeams
                         * Hvis den er det bliver de tilføjet til en arrayliste i dataprovideren
                         */
                        ListDataProvider<Participant> participantListDataProvider = new ListDataProvider<>();

                        for (Participant participant: result) {
                            if (participant.getTeamID() == currentTeam.getTeamID()){
                                participantListDataProvider.getList().add(participant);
                            }
                        }

                        /**
                         * Lav tabellen og skift view
                         */
                        content.getParticipantView().getMyTeamView().initTable(participantListDataProvider);
                        content.getParticipantView().changeView(content.getParticipantView().getMyTeamView());
                    }
                });
            } else if (event.getSource() == content.getParticipantView().getChangeTeamBtn()){
                content.getParticipantView().changeView(content.getParticipantView().getChangeTeamView());
            } else if (event.getSource() == content.getParticipantView().getLogoutBtn()){
                currentParticipant = null;
                content.switchToGuestView();
            }
        }
    }

    class CreateTeamClickHandler implements ClickHandler{
        /**
         * Called when a native click event is fired.
         *
         * @param event the {@link ClickEvent} that was fired
         */
        @Override
        public void onClick(ClickEvent event) {
            if (event.getSource() == content.getParticipantView().getCreateTeamView().getSubmitBtn()) {
                if (content.getParticipantView().getCreateTeamView().getTeamNameField() != null) {
                    Team newTeam = new Team();
                    newTeam.setTeamName(content.getParticipantView().getCreateTeamView().getTeamNameField().getText());
                    String participantsString = content.getParticipantView()
                            .getCreateTeamView()
                            .getTeamMembersField()
                            .getText();

                    /**
                     * Fjerner alle whitespace characters.
                     * Det første \ angiver at det er regex. \s angiver at det er alle whitespace enheder
                     */
                    participantsString = participantsString.replaceAll("\\s", "");

                    String[] participantsArray = participantsString.split(";");
                    List<String> participantsList = Arrays.asList(participantsArray);

                    ArrayList<String> participantsArrayList = new ArrayList<>();
                    participantsArrayList.addAll(participantsList);

                    newTeam.setParticipants(participantsArrayList);

                    /**
                     * Lav holdet som current participant
                     */
                    rpcService.createTeam(newTeam, currentParticipant, new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert("Kan ikke lave holdet");

                        }

                        @Override
                        public void onSuccess(Boolean result) {
                            Window.alert("Success");

                            /**
                             * Opdater current participant så han nu er teamcaptain
                             */
                            rpcService.getParticipant(currentParticipant.getEmail(), new AsyncCallback<Participant>() {
                                @Override
                                public void onFailure(Throwable caught) {

                                }

                                @Override
                                public void onSuccess(Participant result) {
                                    currentParticipant = result;
                                    createParticipantView();
                                    content.getParticipantView().changeView(content.getParticipantView().getMyTeamView());

                                    /**
                                     * Sæt current team til current participants hold
                                     */
                                    rpcService.getTeam(currentParticipant.getEmail(), new AsyncCallback<Team>() {
                                        @Override
                                        public void onFailure(Throwable caught) {

                                        }

                                        @Override
                                        public void onSuccess(Team result) {
                                            currentTeam = result;
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        }
    }

    class MyTeamDelegateHandler implements ActionCell.Delegate<Participant>{
        /**
         * Perform the desired action on the given object.
         *
         * @param object the object to be acted upon
         */
        @Override
        public void execute(Participant object) {
            rpcService.removeFromTeam(object, new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(Boolean result) {
                    Window.alert("Personen er fjernet ;)");
                }
            });
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
                    content.getParticipantView().getMyProfileView().getNameLabel().setText(
                            "Dit navn: " + result);
                }
            }
        });

        //Sætter email
        content.getParticipantView().getMyProfileView().getEmailLabel().setText(
                "Email: " + currentParticipant.getEmail());

        rpcService.getParticipantCyclistType(currentParticipant.getEmail(), new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(String result) {
                content.getParticipantView().getMyProfileView().getCyclistTypeLabel().setText(
                        "Din cyclist-type: " + result);
            }
        });

        rpcService.getParticipantFirmName(currentParticipant.getEmail(), new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(String result) {
                content.getParticipantView().getMyProfileView().getFirmLabel().setText(
                        "Firma: " + result);
            }
        });

        rpcService.getParticipantTeamName(currentParticipant.getEmail(), new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(String result) {
                if (result == null){
                    content.getParticipantView().getMyProfileView().getTeamLabel().setText(null);
                } else {
                    content.getParticipantView().getMyProfileView().getTeamLabel().setText(
                            "Dit hold: " + result);
                }


            }
        });


    }



}

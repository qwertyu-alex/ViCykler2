package client.appcontroller;

import client.ui.Content;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import rpc.ApplicationServiceAsync;
import server.ApplicationServiceImpl;
import shared.DTO.Firm;
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
        content.getParticipantView().getMyTeamBox().addStyleName("hidden");
        content.getParticipantView().getChangeTeamBox().addStyleName("hidden");
        content.getParticipantView().getMyTeamView().getChangeTeam().addStyleName("hidden");
        content.getParticipantView().getCreateTeamBox().addStyleName("hidden");

        /**
         * Hvis der ikke er et hold forbundet
         */
        if (currentParticipant.getTeamID() == 0){
            content.getParticipantView().getCreateTeamBox().removeStyleName("hidden");
        }

        /**
         * Hvis der er et hold forbundet
         */
        if (currentParticipant.getTeamID() != 0){
            content.getParticipantView().getMyTeamBox().removeStyleName("hidden");
            createMyTeam();

            if (currentParticipant.getPersonType().equalsIgnoreCase("TEAMCAPTAIN")){
                content.getParticipantView().getChangeTeamBox().removeStyleName("hidden");
                content.getParticipantView().getMyTeamView().getChangeTeam().removeStyleName("hidden");
            }

            findCurrentTeam();
        }
    }

    private void addClickhandlers(){
        content.getParticipantView().addClickHandlers(new ParticipantClickHandler());
        content.getParticipantView().getCreateTeamView().addClickHandlers(new CreateTeamClickHandler());
        content.getParticipantView().getMyTeamView().setDelegate(new MyTeamDelegateHandler());
        if (currentParticipant.getPersonType().equalsIgnoreCase("TEAMCAPTAIN")){
            content.getParticipantView().getMyTeamView().addTeamCaptainClickHandler(new MyTeamTeamCaptainClickHandler());
        }

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
                createMyTeam();
            } else if (event.getSource() == content.getParticipantView().getChangeTeamBtn()){
                content.getParticipantView().changeView(content.getParticipantView().getChangeTeamView());
            } else if (event.getSource() == content.getParticipantView().getLogoutBtn()){
                currentParticipant = null;
                currentTeam = null;
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
                                public void onFailure(Throwable caught) {}

                                @Override
                                public void onSuccess(Participant result) {
                                    currentParticipant = result;

                                    /**
                                     * Sæt current team til det hold der lige er blevet lavet
                                     */
                                    rpcService.getTeam(currentParticipant.getEmail(), new AsyncCallback<Team>() {
                                        @Override
                                        public void onFailure(Throwable caught) {}

                                        @Override
                                        public void onSuccess(Team result) {
                                            currentTeam = result;

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


                                            rpcService.addParticipantsToTeam(currentTeam, participantsArrayList, new AsyncCallback<Boolean>() {
                                                @Override
                                                public void onFailure(Throwable caught) {

                                                }

                                                @Override
                                                public void onSuccess(Boolean result) {
                                                    createParticipantView();
                                                    content.getParticipantView().changeView(content.getParticipantView().getMyTeamView());
                                                }
                                            });
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
                    participantListDataProvider.getList().remove(object);
                    participantListDataProvider.refresh();
                    createParticipantView();
                }

            });
        }
    }

    class MyTeamTeamCaptainClickHandler implements ClickHandler{
        /**
         * Called when a native click event is fired.
         *
         * @param event the {@link ClickEvent} that was fired
         */
        @Override
        public void onClick(ClickEvent event) {
            if (event.getSource() == content.getParticipantView().getMyTeamView().getSubmitBtn()){
                if (content.getParticipantView().getMyTeamView().getChangeTeamNameField().getText()
                        .replaceAll("\\s", "")
                        .length() != 0){
                    Team newTeam = new Team();
                    newTeam.setTeamName(content.getParticipantView().getMyTeamView().getChangeTeamNameField().getText());

                    rpcService.changeTeamInfo(currentTeam, newTeam, new AsyncCallback<Team>() {
                        @Override
                        public void onFailure(Throwable caught) {

                        }

                        @Override
                        public void onSuccess(Team result) {
                            content.getParticipantView().getMyTeamView().getChangeTeamNameField().setText("");
                            createParticipantView();
                        }
                    });
                }

                if (content.getParticipantView().getMyTeamView().getAddParticipantField().getText()
                        .replaceAll("\\s", "")
                        .length() != 0){

                    ArrayList<String> participants = new ArrayList<>();


                    participants.add(content.getParticipantView().getMyTeamView().getAddParticipantField().getText()
                            .replaceAll("\\s", ""));

                    rpcService.addParticipantsToTeam(currentTeam, participants, new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable caught) {

                        }

                        @Override
                        public void onSuccess(Boolean result) {
                            content.getParticipantView().getMyTeamView().getAddParticipantField().setText("");
                            createParticipantView();
                        }
                    });
                }
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
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(String result) {
                content.getParticipantView().getMyProfileView().getCyclistTypeLabel().setText(
                        "Din cyclist-type: " + result);
            }
        });

        rpcService.getParticipantFirmName(currentParticipant.getEmail(), new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(String result) {
                content.getParticipantView().getMyProfileView().getFirmLabel().setText(
                        "Firma: " + result);
            }
        });

        rpcService.getParticipantTeamName(currentParticipant.getEmail(), new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {}

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

    private void createMyTeam(){

        rpcService.getAllParticipantsInTeamFromTeamID(currentParticipant.getTeamID(), new AsyncCallback<ArrayList<Participant>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ArrayList<Participant> result) {
                participantListDataProvider = new ListDataProvider<>();
                participantListDataProvider.getList().addAll(result);

                content.getParticipantView().getMyTeamView().initTable(participantListDataProvider, currentParticipant);
                content.getParticipantView().changeView(content.getParticipantView().getMyTeamView());
            }
        });

        /**
         * Sætter de forskellige labels af holdet til de rigtige værdier, fx hold id bliver sat til holdets id i databasen
         */
        rpcService.getTeam(currentParticipant.getEmail(), new AsyncCallback<Team>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(Team result) {
                content.getParticipantView().getMyTeamView().getTeamIDLabel().setText(result.getTeamID() + "");
                content.getParticipantView().getMyTeamView().getTeamNameLabel().setText(result.getTeamName());
                content.getParticipantView().getMyTeamView().getNumberOfParticipantsLabel().setText(result.getParticipants().size()+ "");
            }
        });

        rpcService.getAllParticipantsInTeamFromTeamID(currentParticipant.getTeamID(), new AsyncCallback<ArrayList<Participant>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ArrayList<Participant> result) {
                content.getParticipantView().getMyTeamView().getNumberOfParticipantsLabel().setText(result.size()+ "");
            }
        });

        /**
         * Henter firmaet navn ud fra currentParticipants email og sætter det til en label
         */
        rpcService.getFirmFromEmail(currentParticipant.getEmail(), new AsyncCallback<Firm>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(Firm firm) {
                content.getParticipantView().getMyTeamView().getFirmNameLabel().setText(firm.getFirmName());
            }
        });
    }
}

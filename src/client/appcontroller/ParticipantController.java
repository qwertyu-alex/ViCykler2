package client.appcontroller;

import client.ui.Content;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;
import rpc.ApplicationServiceAsync;
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
        createStatistic();
    }

    public void findCurrentTeam(){
        rpcService.getTeamFromEmail(currentParticipant.getEmail(), new AsyncCallback<Team>() {
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
        content.getParticipantView().getParticipantStatisticView().addClickHandlers(new ParticipantStatisticClickHandler());

        //Denne tilføjer ikke en clickhandler men en changehandler dvs en en handler der lytter efter ændringer.
        content.getParticipantView().getParticipantStatisticView().getFirmsList2().addChangeHandler(new SearchTeamChangeHandler());

    }

    class ParticipantClickHandler implements ClickHandler{
        @Override
        public void onClick(ClickEvent event) {

            if (event.getSource() == content.getParticipantView().getMyProfileBtn()){
                content.getParticipantView().changeView(content.getParticipantView().getMyProfileView());
            } else if (event.getSource() == content.getParticipantView().getStatistiskBtn()){
                content.getParticipantView().changeView(content.getParticipantView().getParticipantStatisticView());
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
                                    rpcService.getTeamFromEmail(currentParticipant.getEmail(), new AsyncCallback<Team>() {
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

    /**
     * Denne changehandler sørger for at skrifte holdlisten hver gang den opfatter at firmaet er skiftet.
     */
    class SearchTeamChangeHandler implements ChangeHandler{
        /**
         * Called when a change event is fired.
         *
         * @param event the {@link ChangeEvent} that was fired
         */
        @Override
        public void onChange(ChangeEvent event) {

            content.getParticipantView().getParticipantStatisticView().getTeamList().clear();

            rpcService.getAllTeams(new AsyncCallback<ArrayList<Team>>() {
                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(ArrayList<Team> teams) {
                    rpcService.getFirmIDFromFirmName(content.getParticipantView().getParticipantStatisticView().getFirmsList2().getSelectedValue(), new AsyncCallback<Integer>() {
                        @Override
                        public void onFailure(Throwable caught) {

                        }

                        @Override
                        public void onSuccess(Integer firmID) {
                            for (Team team :teams) {
                                if (team.getFirmID() == firmID){
                                    content.getParticipantView().getParticipantStatisticView().getTeamList().addItem(
                                            team.getTeamName()
                                    );
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    class ParticipantStatisticClickHandler implements ClickHandler{
        /**
         * Called when a native click event is fired.
         *
         * @param event the {@link ClickEvent} that was fired
         */
        @Override
        public void onClick(ClickEvent event) {
            /**
             * Klargører nogle knapper som skal bruges senere
             * De andre elementer kan giver ikke mening at klargøres, da de skal lave dynamisk.
             * Disse knapper skal være der, og derfor giver det mening at lave dem her i starten.
             */
            Button returnToFirmSearchBtn = new Button();
            Button returnToTeamSearchBtn = new Button();
            Button returnToParticipantSearchBtn = new Button();

            returnToFirmSearchBtn.setText("Gå tilbage");
            returnToTeamSearchBtn.setText("Gå tilbage");
            returnToParticipantSearchBtn.setText("Gå tilbage");


            /**
             * Tilføjer clickhandlers til knapperne foroven
             */
            returnToFirmSearchBtn.addClickHandler(new ReturnToFirmSearchClickHandler());
            returnToTeamSearchBtn.addClickHandler(new ReturnToTeamSearchClickHandler());
            returnToParticipantSearchBtn.addClickHandler(new ReturnToParticipantSearchClickHandler());
            /**
             * Firma søgning
             */
            if (event.getSource() == content.getParticipantView().getParticipantStatisticView().getSubmitFirmBtn()){
                String selectedFirmName = content.getParticipantView().getParticipantStatisticView().getFirmsList1().getSelectedValue();
                rpcService.getFirmIDFromFirmName(selectedFirmName, new AsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable caught) {}

                    @Override
                    public void onSuccess(Integer firmID) {

                        rpcService.getAllTeamCaptains(new AsyncCallback<ArrayList<Participant>>() {
                            @Override
                            public void onFailure(Throwable caught) {

                            }

                            @Override
                            public void onSuccess(ArrayList<Participant> teamCaptains) {
                                /**
                                 * Fjerner alt content inden den bygger panelet
                                 */
                                content.getParticipantView().getParticipantStatisticView().getResFirmPanel().clear();
                                /**
                                 * Laver getResFirm panelet
                                 */
                                content.getParticipantView().getParticipantStatisticView().getResFirmPanel().add(new Label(
                                        "Du søgte på " + selectedFirmName + " som har følgende ID: " + firmID
                                ));
                                content.getParticipantView().getParticipantStatisticView().getResFirmPanel().add(new Label(
                                        "Firmaet har følgende holdkaptajn og hold:"
                                ));

                                boolean foundMatch = false;
                                /**
                                 * Laver et panel hvori alle resultaterne skal skubbes ind
                                 */
                                VerticalPanel teamsPanel = new VerticalPanel();

                                content.getParticipantView().getParticipantStatisticView().getResFirmPanel().add(teamsPanel);
                                for (Participant teamCaptain:teamCaptains){
                                    if (teamCaptain.getFirmID() == firmID){
                                        foundMatch = true;
                                        rpcService.getTeamFromEmail(teamCaptain.getEmail(), new AsyncCallback<Team>() {
                                            @Override
                                            public void onFailure(Throwable caught) {}

                                            @Override
                                            public void onSuccess(Team team) {
                                                teamsPanel.add(
                                                        new Label("Holdkaptajn: " + teamCaptain.getName() + "\tHold: " + team.getTeamName()));
                                            }
                                        });
                                    }
                                }


                                if (!foundMatch){
                                    content.getParticipantView().getParticipantStatisticView().getResFirmPanel().add(
                                         new Label("Intet hold er forbundet med firmaet")
                                    );
                                }
                                content.getParticipantView().getParticipantStatisticView().getResFirmPanel().add(returnToFirmSearchBtn);
                                content.getParticipantView().getParticipantStatisticView().getFirmSearchDeck().showWidget(1);
                            }
                        });
                    }
                });

            } else if (event.getSource() == content.getParticipantView().getParticipantStatisticView().getSubmitTeamBtn()){

                /**
                 * Fjerner alt content inden den bygger panelet
                 */
                content.getParticipantView().getParticipantStatisticView().getResTeamPanel().clear();

                String selectedFirmName = content.getParticipantView().getParticipantStatisticView().getFirmsList2().getSelectedValue();
                String selectedTeamName = content.getParticipantView().getParticipantStatisticView().getTeamList().getSelectedValue();

                rpcService.getFirmIDFromFirmName(selectedFirmName, new AsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable caught) {

                    }

                    @Override
                    public void onSuccess(Integer firmID) {
                        rpcService.getTeamIDFromTeamNameAndFirmID(selectedTeamName, firmID, new AsyncCallback<Integer>() {
                            @Override
                            public void onFailure(Throwable caught) {
                            }

                            @Override
                            public void onSuccess(Integer teamID) {
                                content.getParticipantView().getParticipantStatisticView().getResTeamPanel().add(
                                        new Label("Firma: " + selectedFirmName)
                                );
                                content.getParticipantView().getParticipantStatisticView().getResTeamPanel().add(
                                        new Label("Du har søgt på: \"" + selectedTeamName + "\" som har følgende ID: " + teamID)
                                );
                                rpcService.getAllParticipantsInTeamFromTeamID(teamID, new AsyncCallback<ArrayList<Participant>>() {
                                    @Override
                                    public void onFailure(Throwable caught) {

                                    }

                                    @Override
                                    public void onSuccess(ArrayList<Participant> participants) {
                                        ArrayList<Participant> participantsInTeam = new ArrayList<>();
                                        Participant teamCaptainInTeam = null;

                                        /**
                                         * Find alle participants i holdet og holdkaptajnen.
                                         */
                                        for (Participant participant: participants){
                                            if (participant.getTeamID() == teamID){
                                                participantsInTeam.add(participant);
                                                if (participant.getPersonType().equalsIgnoreCase("TEAMCAPTAIN")){
                                                    teamCaptainInTeam = participant;
                                                }
                                            }
                                        }

                                        /**
                                         * Hvis antal deltagere i holdet
                                         */
                                        if (participantsInTeam.size() > 0){
                                            content.getParticipantView().getParticipantStatisticView().getResTeamPanel().add(
                                                    new Label("Der er " + participantsInTeam.size() + " antal deltagere i holdet")
                                            );
                                        }

                                        /**
                                         * Indsæt holdkaptajn label
                                         */
                                        if (teamCaptainInTeam != null){
                                            content.getParticipantView().getParticipantStatisticView().getResTeamPanel().add(
                                                    new Label("Holdkaptajnen er " + teamCaptainInTeam.getName() + " - " + teamCaptainInTeam.getEmail())
                                            );
                                        }

                                        /**
                                         * Indsæt deltagernes fra holdets oplysninger
                                         */
                                        content.getParticipantView().getParticipantStatisticView().getResTeamPanel().add(
                                                new Label("Der er følgende personer i holdet:")
                                        );
                                        for (Participant participant : participantsInTeam){
                                            content.getParticipantView().getParticipantStatisticView().getResTeamPanel().add(
                                                    new Label(participant.getName() + "\t" + participant.getEmail())
                                            );
                                        }

                                        content.getParticipantView().getParticipantStatisticView().getResTeamPanel().add(returnToTeamSearchBtn);
                                        content.getParticipantView().getParticipantStatisticView().getTeamSearchDeck().showWidget(1);
                                    }
                                });
                            }
                        });
                    }
                });
            } else if (event.getSource() == content.getParticipantView().getParticipantStatisticView().getSubmitParticipantBtn()){
                content.getParticipantView().getParticipantStatisticView().getResParticipantPanel().clear();

                rpcService.getParticipant(content.getParticipantView().getParticipantStatisticView().getParticipantEmailField().getText(),
                        new AsyncCallback<Participant>() {
                    @Override
                    public void onFailure(Throwable caught) {}

                    @Override
                    public void onSuccess(Participant participant) {
                        if(participant == null){
                            content.getParticipantView().getParticipantStatisticView().getSearchParticipantPanelErrLabel().setText("Intet resultat");
                        } else {
                            content.getParticipantView().getParticipantStatisticView().getSearchParticipantPanelErrLabel().setText("");
                            rpcService.getTeamFromEmail(participant.getEmail(), new AsyncCallback<Team>() {
                                @Override
                                public void onFailure(Throwable caught) {

                                }

                                @Override
                                public void onSuccess(Team team) {
                                    rpcService.getFirmFromEmail(participant.getEmail(), new AsyncCallback<Firm>() {
                                        @Override
                                        public void onFailure(Throwable caught) {

                                        }

                                        @Override
                                        public void onSuccess(Firm firm) {
                                            content.getParticipantView().getParticipantStatisticView().getResParticipantPanel().add(
                                                    new Label("Du har søgt på " + participant.getEmail() + ":")
                                            );
                                            content.getParticipantView().getParticipantStatisticView().getResParticipantPanel().add(
                                                    new Label("Navn: " + participant.getName())
                                            );
                                            content.getParticipantView().getParticipantStatisticView().getResParticipantPanel().add(
                                                    new Label("Personen er en " + participant.getPersonType())
                                            );
                                            if (team != null){
                                                content.getParticipantView().getParticipantStatisticView().getResParticipantPanel().add(
                                                        new Label("Personen er med i holdet: " + team.getTeamName())
                                                );
                                            }

                                            content.getParticipantView().getParticipantStatisticView().getResParticipantPanel().add(
                                                    new Label("Niveau: " + participant.getCyclistType())
                                            );

                                            if (firm != null){
                                                content.getParticipantView().getParticipantStatisticView().getResParticipantPanel().add(
                                                        new Label("Deltager sammen med " + firm.getFirmName())
                                                );
                                            }

                                            content.getParticipantView().getParticipantStatisticView().getResParticipantPanel().add(returnToParticipantSearchBtn);
                                            content.getParticipantView().getParticipantStatisticView().getParticipantSearchDeck().showWidget(1);

                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    class ReturnToFirmSearchClickHandler implements ClickHandler{
        /**
         * Called when a native click event is fired.
         *
         * @param event the {@link ClickEvent} that was fired
         */
        @Override
        public void onClick(ClickEvent event) {
            content.getParticipantView().getParticipantStatisticView().getFirmSearchDeck().showWidget(0);
        }
    }

    class ReturnToTeamSearchClickHandler implements ClickHandler{
        /**
         * Called when a native click event is fired.
         *
         * @param event the {@link ClickEvent} that was fired
         */
        @Override
        public void onClick(ClickEvent event) {
            content.getParticipantView().getParticipantStatisticView().getTeamSearchDeck().showWidget(0);
        }
    }

    class ReturnToParticipantSearchClickHandler implements ClickHandler{
        /**
         * Called when a native click event is fired.
         *
         * @param event the {@link ClickEvent} that was fired
         */
        @Override
        public void onClick(ClickEvent event) {
            content.getParticipantView().getParticipantStatisticView().getParticipantSearchDeck().showWidget(0);
        }
    }
//    private void createTable(){
//        rpcService.getAllParticipants(new AsyncCallback<ArrayList<Participant>>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                Window.alert(caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(ArrayList<Participant> result) {
//                content.getParticipantView().getStatisticView().initTable(participantListDataProvider);
//                participantListDataProvider.getList().addAll(result);
//            }
//        });
//    }
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

        /**
         * Opretter tabellen
         */
        rpcService.getAllParticipantsInTeamFromTeamID(currentParticipant.getTeamID(), new AsyncCallback<ArrayList<Participant>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ArrayList<Participant> result) {
                participantListDataProvider = new ListDataProvider<>();
                participantListDataProvider.getList().addAll(result);

                CellTable<Participant> cellTable = content.getParticipantView().getMyTeamView().getCellTable();
                if (cellTable.getColumnCount() > 0){
                    for (int i = 0; i <= cellTable.getColumnCount() + 1; i++){
                        cellTable.removeColumn(0);
                    }
                }

                content.getParticipantView().getMyTeamView().initTable(participantListDataProvider);

                if (currentParticipant.getPersonType().equalsIgnoreCase("TEAMCAPTAIN")){
                    content.getParticipantView().getMyTeamView().createTeamCaptainCol();
                }

                content.getParticipantView().changeView(content.getParticipantView().getMyTeamView());
            }
        });

        /**
         * Sætter de forskellige labels af holdet til de rigtige værdier, fx hold id bliver sat til holdets id i databasen
         */
        rpcService.getTeamFromEmail(currentParticipant.getEmail(), new AsyncCallback<Team>() {
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

    private void createStatistic(){
        /**
         * Laver en liste over de forskellige firmaer og deres stats
         */
        rpcService.getAllFirms(new AsyncCallback<ArrayList<Firm>>() {
            @Override
            public void onFailure(Throwable caught) {Window.alert(caught.getMessage());}

            @Override
            public void onSuccess(ArrayList<Firm> firms) {
                rpcService.getAllTeams(new AsyncCallback<ArrayList<Team>>() {
                    @Override
                    public void onFailure(Throwable caught) {Window.alert(caught.getMessage());}

                    @Override
                    public void onSuccess(ArrayList<Team> teams) {
                        rpcService.getAllParticipants(new AsyncCallback<ArrayList<Participant>>() {
                            @Override
                            public void onFailure(Throwable caught) {Window.alert(caught.getMessage());}

                            @Override
                            public void onSuccess(ArrayList<Participant> participants) {

                                /**
                                 * Denne metode sørger for at alt fra panelet bliver slettet, så den ikke bygger videre på noget der allerede er der.
                                 */
                                content.getParticipantView().getParticipantStatisticView().getStatisticPanel().clear();

                                content.getParticipantView().getParticipantStatisticView().getStatisticPanel().add(
                                        new Label("Der er i alt " + firms.size() + " firmaer tilmeldt.")
                                );

                                content.getParticipantView().getParticipantStatisticView().getStatisticPanel().add(
                                        new Label("Der er i alt " + teams.size() + " hold tilmeldt.")
                                );

                                content.getParticipantView().getParticipantStatisticView().getStatisticPanel().add(
                                        new Label("Der er i alt " + participants.size() + " deltagere tilmeldt.")
                                );


                                for (Firm firm :firms) {
                                    VerticalPanel tempVerticalPanel = new VerticalPanel();
                                    tempVerticalPanel.addStyleName("fakefakeBtn margintop marginbot");

                                    content.getParticipantView().getParticipantStatisticView().getStatisticPanel().add(
                                            tempVerticalPanel
                                    );

                                    tempVerticalPanel.add(new Label(firm.getFirmName()));

                                    int numberOfTeamsInFirm = 0;
                                    for (Team team : teams) {
                                        if (team.getFirmID() == firm.getID()){
                                            teams.remove(team);
                                            numberOfTeamsInFirm++;
                                        }
                                    }

                                    int numberOfParticipantsInFirm = 0;
                                    for (Participant participant: participants){
                                        if (participant.getFirmID() == firm.getID()){
                                            participants.remove(participant);
                                            numberOfParticipantsInFirm++;
                                        }
                                    }

                                    double pctOfTeams = (double) numberOfTeamsInFirm / (double) teams.size() * (double) 100;
                                    double pctOfParticipants = (double) numberOfParticipantsInFirm / (double) participants.size() * (double) 100;

                                    /**
                                     * Formaterer double så den kun har 2 digits
                                     */
                                    String formattedPctOfTeams = NumberFormat.getFormat("00.00").format(pctOfTeams);
                                    String formattedPctOfParticipants = NumberFormat.getFormat("00.00").format(pctOfParticipants);

                                    tempVerticalPanel.add(new Label("Antal hold i firmaet: " + numberOfTeamsInFirm));
                                    tempVerticalPanel.add(new Label("Antallet af hold udgør: " + formattedPctOfTeams + "%" ));
                                    tempVerticalPanel.add(new Label("Antal deltagere i firmaet: " + numberOfParticipantsInFirm));
                                    tempVerticalPanel.add(new Label("Antallet af deltagere udgør: " + formattedPctOfParticipants + "%"));
                                }
                            }
                        });
                    }
                });
            }
        });
        /**
         * Laver firma søgningen. Indsætter de forskellige firmanavne i listen.
         */
        rpcService.getAllFirms(new AsyncCallback<ArrayList<Firm>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ArrayList<Firm> firms) {

                for (Firm firm : firms){
                    content.getParticipantView().getParticipantStatisticView().getFirmsList1().addItem(
                            firm.getFirmName()
                    );
                }
            }
        });
        /**
         * Laver holdsøgningen ved at indsætte de forskellige firma og holdnavne i listerne
         */
        rpcService.getAllFirms(new AsyncCallback<ArrayList<Firm>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ArrayList<Firm> firms) {

                for (Firm firm : firms){
                    content.getParticipantView().getParticipantStatisticView().getFirmsList2().addItem(
                            firm.getFirmName()
                    );
                }

                rpcService.getAllTeams(new AsyncCallback<ArrayList<Team>>() {
                    @Override
                    public void onFailure(Throwable caught) {

                    }

                    @Override
                    public void onSuccess(ArrayList<Team> teams) {
                        for (Team team: teams){
                            if (team.getFirmID() == firms.get(0).getID()){
                                content.getParticipantView().getParticipantStatisticView().getTeamList().addItem(team.getTeamName());
                            }
                        }
                    }
                });
            }
        });
    }
}

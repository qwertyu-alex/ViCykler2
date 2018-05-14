package client.appcontroller;

import client.ui.Content;
import client.ui.participant.ParticipantView;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;
import client.rpc.ApplicationServiceAsync;
import shared.DTO.Firm;
import shared.DTO.Participant;
import shared.DTO.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ParticipantController {

    private Content content;
    private Participant currentParticipant;
    private Team currentTeam;
    private ApplicationServiceAsync rpcService;
    private ListDataProvider<Participant> participantListDataProvider;
    ParticipantView participantView; 
    

    //default konstruktør
    public ParticipantController(Content content, Participant currentParticipant,
                                 ApplicationServiceAsync rpcService) {
        this.content = content;
        this.currentParticipant = currentParticipant;
        this.rpcService = rpcService;
        //Opret et participantView
        this.participantView = new ParticipantView();
        //Tilføj participantView til DeckLayoutPanel i Content
        content.getMainDeck().add(participantView);
        content.getMainDeck().showWidget(participantView);
        //Udfyld de manglende elementer i participantView med data fra databasen
        createParticipantView();
        addClickhandlers();
    }

    /**
     * Opretter alle elementerne på de forskellige menuer
     */
    public void createParticipantView(){
        createMyProfile();
        checkParticipantTeam();
        createStatistic();
    }

    /**
     * Finder holdet og sætter controllerens currentTeam attribute til det som den finder
     */
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

    /**
     * Metode der tjekker om deltager er på et hold eller ikke.
     * Hvis deltager er på et hold, vises knappen "Mit hold" i north view og ellers vises knappen "opret hold"
     */
    public void checkParticipantTeam(){
        participantView.getMyTeamBox().addStyleName("hidden");
        participantView.getMyTeamView().getChangeTeam().addStyleName("hidden");
        participantView.getCreateTeamBox().addStyleName("hidden");

        /**
         * Hvis der ikke er et hold forbundet
         */
        if (currentParticipant.getTeamID() == 0){
            participantView.getCreateTeamBox().removeStyleName("hidden");
        }

        /**
         * Hvis der er et hold forbundet
         */
        if (currentParticipant.getTeamID() != 0){
            participantView.getMyTeamBox().removeStyleName("hidden");
            createMyTeam();

            if (currentParticipant.getPersonType().equalsIgnoreCase("TEAMCAPTAIN")){
                participantView.getMyTeamView().getChangeTeam().removeStyleName("hidden");
            }

            findCurrentTeam();
        }
    }

    /**
     * ClickHandlers for Team kaptajnen, bliver tilføjet til deltager, hvis han er brugertypen team kaptajn
     */
    private void addTeamCaptainClickHandlers(){
        participantView.getMyTeamView().addTeamCaptainClickHandler(new MyTeamTeamCaptainClickHandler());
        participantView.getMyTeamView().setDelegate(new MyTeamDelegateHandler());
    }

    /**
     * Metode der tilføjer ClickHandlers til metoder og forbinder dem med GuestView
     */
    private void addClickhandlers(){
        participantView.addClickHandlers(new ParticipantClickHandler());
        participantView.getCreateTeamView().addClickHandlers(new CreateTeamClickHandler());
        participantView.getParticipantStatisticView().addClickHandlers(new ParticipantStatisticClickHandler());

        //Kun hvis det er en holdkaptain skal han have rettigheder til at gøre det som en holdkaptain kan
        if (currentParticipant.getPersonType().equalsIgnoreCase("TEAMCAPTAIN")){
            addTeamCaptainClickHandlers();
        }
        //Denne tilføjer ikke en clickhandler men en changehandler dvs en en handler der lytter efter ændringer.
        participantView.getParticipantStatisticView().getFirmsList2().addChangeHandler(new SearchTeamChangeHandler());
    }

    /**
     * ClickHandler metode til knapperne oppe i north view og som skifter centerView.
     */
    class ParticipantClickHandler implements ClickHandler{
        @Override
        public void onClick(ClickEvent event) {

            if (event.getSource() == participantView.getMyProfileBtn()){
                participantView.changeView(participantView.getMyProfileView());
            } else if (event.getSource() == participantView.getStatistiskBtn()){
                participantView.changeView(participantView.getParticipantStatisticView());
            } else if (event.getSource() == participantView.getCreateTeamBtn()){
                participantView.changeView(participantView.getCreateTeamView());
            } else if (event.getSource() == participantView.getMyTeamBtn()){
                participantView.changeView(participantView.getMyTeamView());
                createMyTeam();
            } else if (event.getSource() == participantView.getLogoutBtn()){
                currentParticipant = null;
                currentTeam = null;
                content.getMainDeck().remove(participantView);
                content.switchToGuestView();
            }
        }
    }

    /**
     * Metoder der opretter et hold på click, hvis holdnavn er godkendt
     */
    class CreateTeamClickHandler implements ClickHandler{
        /**
         * Called when a native click event is fired
         * @param event the {@link ClickEvent} that was fired
         */
        @Override
        public void onClick(ClickEvent event) {

            if (event.getSource() == participantView.getCreateTeamView().getSubmitBtn()) {
                if (participantView.getCreateTeamView().getTeamNameField() != null) {

                    /**
                     * Lav holdet som current participant
                     */
                    rpcService.createTeam(participantView.getCreateTeamView().getTeamNameField().getText(), currentParticipant.getEmail(), new AsyncCallback<String>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert(caught.getMessage());
                            Window.alert("Kan ikke lave holdet");
                        }

                        @Override
                        public void onSuccess(String result) {

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

                                            String participantsString = participantView
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

                                            if (participantsArrayList.size() > 0){
                                                rpcService.addParticipantsToTeam(currentTeam, participantsArrayList, new AsyncCallback<String>() {
                                                    @Override
                                                    public void onFailure(Throwable caught) {

                                                    }

                                                    @Override
                                                    public void onSuccess(String result) {
                                                        createParticipantView();
                                                        addTeamCaptainClickHandlers();
                                                        participantView.changeView(participantView.getMyTeamView());
                                                    }
                                                });
                                            } else {
                                                createParticipantView();
                                                addTeamCaptainClickHandlers();
                                                participantView.changeView(participantView.getMyTeamView());
                                            }
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

    /**
     * En delegate der sørger for at fjerne den person som der bliver klikket ved fra holdet
     * Denne delegate kan kun bruges af holdkaptainen
     */
    class MyTeamDelegateHandler implements ActionCell.Delegate<Participant>{
        /**
         * Perform the desired action on the given object.
         *
         * @param object the object to be acted upon
         */
        @Override
        public void execute(Participant object) {


            if (object.getEmail() != currentParticipant.getEmail()){
                rpcService.removeFromTeam(object, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {

                    }

                    @Override
                    public void onSuccess(String result) {
                        Window.alert(result);

                        /**
                         * Refresh listDataProvider
                         */
                        rpcService.getAllParticipantsInTeamFromTeamID(currentTeam.getTeamID(), new AsyncCallback<ArrayList<Participant>>() {
                            @Override
                            public void onFailure(Throwable caught) {

                            }

                            @Override
                            public void onSuccess(ArrayList<Participant> result) {

                                participantListDataProvider = new ListDataProvider<>();
                                participantListDataProvider.getList().addAll(result);
                                participantListDataProvider.refresh();
                                createParticipantView();
                            }
                        });
                    }
                });
            } else {
                Window.alert("Du kan ikke slette dig selv");
            }
        }
    }

    /**
     * En klickhandler for den ekstra menu der kommer op når man er logget ind som holdkaptain
     */
    class MyTeamTeamCaptainClickHandler implements ClickHandler{
        /**
         * Called when a native click event is fired.
         *
         * @param event the {@link ClickEvent} that was fired
         */
        @Override
        public void onClick(ClickEvent event) {
            if (event.getSource() == participantView.getMyTeamView().getSubmitBtn()){
                if (participantView.getMyTeamView().getChangeTeamNameField().getText()
                        .replaceAll("\\s", "")
                        .length() != 0){
                    Team newTeam = new Team();
                    newTeam.setTeamName(participantView.getMyTeamView().getChangeTeamNameField().getText());

                    rpcService.changeTeamInfo(currentTeam, newTeam, new AsyncCallback<Team>() {
                        @Override
                        public void onFailure(Throwable caught) {

                        }

                        @Override
                        public void onSuccess(Team result) {
                            participantView.getMyTeamView().getChangeTeamNameField().setText("");
                            createParticipantView();
                        }
                    });
                }

                if (participantView.getMyTeamView().getAddParticipantField().getText()
                        .replaceAll("\\s", "")
                        .length() != 0){

                    ArrayList<String> participants = new ArrayList<>();

                    participants.add(participantView.getMyTeamView().getAddParticipantField().getText()
                            .replaceAll("\\s", ""));

                    rpcService.addParticipantsToTeam(currentTeam, participants, new AsyncCallback<String>() {
                        @Override
                        public void onFailure(Throwable caught) {

                        }

                        @Override
                        public void onSuccess(String result) {
                            participantView.getMyTeamView().getAddParticipantField().setText("");
                            createParticipantView();
                        }
                    });
                }
            }

            if (event.getSource() == participantView.getMyTeamView().getDeleteTeamBtn()){

                participantView.changeView(participantView.getMyProfileView());

                if (Window.confirm("Er du sikker på at du vil slette holdet?")){
                    rpcService.deleteTeam(currentParticipant.getTeamID(), new AsyncCallback<String>() {
                        @Override
                        public void onFailure(Throwable caught) {}

                        @Override
                        public void onSuccess(String result) {
                            participantView.changeView(participantView.getMyProfileView());

                            rpcService.changePersonType(currentParticipant.getEmail(), "PARTICIPANT", new AsyncCallback<String>() {
                                @Override
                                public void onFailure(Throwable caught) {

                                }

                                @Override
                                public void onSuccess(String result) {
                                    currentTeam = null;
                                    rpcService.getParticipant(currentParticipant.getEmail(), new AsyncCallback<Participant>() {
                                        @Override
                                        public void onFailure(Throwable caught) {

                                        }

                                        @Override
                                        public void onSuccess(Participant result) {
                                            currentParticipant.setTeamID(0);
                                            currentParticipant.setTeamName(null);
                                            currentTeam = null;
                                            checkParticipantTeam();
                                            createParticipantView();
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

    /**
     * Denne changehandler sørger for at skrifte holdlisten hver gang den opfatter at firmaet er skiftet.
     * Denne klasse bliver brugt under statistik når man skal søge efter et hold.
     */
    class SearchTeamChangeHandler implements ChangeHandler{
        /**
         * Called when a change event is fired.
         *
         * @param event the {@link ChangeEvent} that was fired
         */
        @Override
        public void onChange(ChangeEvent event) {

            participantView.getParticipantStatisticView().getTeamList().clear();

            rpcService.getAllTeamsAndTeamNameAndParticipants(new AsyncCallback<ArrayList<Team>>() {
                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(ArrayList<Team> teams) {
                    rpcService.getFirmIDFromFirmName(participantView.getParticipantStatisticView().getFirmsList2().getSelectedValue(), new AsyncCallback<Integer>() {
                        @Override
                        public void onFailure(Throwable caught) {

                        }

                        @Override
                        public void onSuccess(Integer firmID) {
                            for (Team team :teams) {
                                if (team.getFirmID() == firmID){
                                    participantView.getParticipantStatisticView().getTeamList().addItem(
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

    /**
     *Metode der indlæser statistikker over hold og firmaer og indeholder clickhandlers metoder
     * Det er muligt i denne metode at søge efter firma, søge efter hold og søge efter deltager
     */
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
            if (event.getSource() == participantView.getParticipantStatisticView().getSubmitFirmBtn()){
                String selectedFirmName = participantView.getParticipantStatisticView().getFirmsList1().getSelectedValue();
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
                                participantView.getParticipantStatisticView().getResFirmPanel().clear();
                                /**
                                 * Laver getResFirm panelet
                                 */
                                participantView.getParticipantStatisticView().getResFirmPanel().add(new Label(
                                        "Du søgte på " + selectedFirmName + " som har følgende ID: " + firmID
                                ));
                                participantView.getParticipantStatisticView().getResFirmPanel().add(new Label(
                                        "Firmaet har følgende holdkaptajn og hold:"
                                ));

                                boolean foundMatch = false;
                                /**
                                 * Laver et panel hvori alle resultaterne skal skubbes ind
                                 */
                                VerticalPanel teamsPanel = new VerticalPanel();

                                participantView.getParticipantStatisticView().getResFirmPanel().add(teamsPanel);
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
                                    participantView.getParticipantStatisticView().getResFirmPanel().add(
                                         new Label("Intet hold er forbundet med firmaet")
                                    );
                                }
                                participantView.getParticipantStatisticView().getResFirmPanel().add(returnToFirmSearchBtn);
                                participantView.getParticipantStatisticView().getFirmSearchDeck().showWidget(1);
                            }
                        });
                    }
                });

            } else if (event.getSource() == participantView.getParticipantStatisticView().getSubmitTeamBtn()){

                /**
                 * Fjerner alt content inden den bygger panelet
                 */
                participantView.getParticipantStatisticView().getResTeamPanel().clear();

                String selectedFirmName = participantView.getParticipantStatisticView().getFirmsList2().getSelectedValue();
                String selectedTeamName = participantView.getParticipantStatisticView().getTeamList().getSelectedValue();

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
                                participantView.getParticipantStatisticView().getResTeamPanel().add(
                                        new Label("Firma: " + selectedFirmName)
                                );
                                participantView.getParticipantStatisticView().getResTeamPanel().add(
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
                                            participantView.getParticipantStatisticView().getResTeamPanel().add(
                                                    new Label("Der er " + participantsInTeam.size() + " antal deltagere i holdet")
                                            );
                                        }

                                        /**
                                         * Indsæt holdkaptajn label
                                         */
                                        if (teamCaptainInTeam != null){
                                            participantView.getParticipantStatisticView().getResTeamPanel().add(
                                                    new Label("Holdkaptajnen er " + teamCaptainInTeam.getName() + " - " + teamCaptainInTeam.getEmail())
                                            );
                                        }

                                        /**
                                         * Indsæt deltagernes fra holdets oplysninger
                                         */
                                        participantView.getParticipantStatisticView().getResTeamPanel().add(
                                                new Label("Der er følgende personer i holdet:")
                                        );
                                        for (Participant participant : participantsInTeam){
                                            participantView.getParticipantStatisticView().getResTeamPanel().add(
                                                    new Label(participant.getName() + "\t" + participant.getEmail())
                                            );
                                        }

                                        participantView.getParticipantStatisticView().getResTeamPanel().add(returnToTeamSearchBtn);
                                        participantView.getParticipantStatisticView().getTeamSearchDeck().showWidget(1);
                                    }
                                });
                            }
                        });
                    }
                });
            } else if (event.getSource() == participantView.getParticipantStatisticView().getSubmitParticipantBtn()){
                participantView.getParticipantStatisticView().getResParticipantPanel().clear();

                rpcService.getParticipant(participantView.getParticipantStatisticView().getParticipantEmailField().getText(),
                        new AsyncCallback<Participant>() {
                    @Override
                    public void onFailure(Throwable caught) {}

                    @Override
                    public void onSuccess(Participant participant) {
                        if(participant == null){
                            participantView.getParticipantStatisticView().getSearchParticipantPanelErrLabel().setText("Intet resultat");
                        } else {
                            participantView.getParticipantStatisticView().getSearchParticipantPanelErrLabel().setText("");
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
                                            participantView.getParticipantStatisticView().getResParticipantPanel().add(
                                                    new Label("Du har søgt på " + participant.getEmail() + ":")
                                            );
                                            participantView.getParticipantStatisticView().getResParticipantPanel().add(
                                                    new Label("Navn: " + participant.getName())
                                            );
                                            participantView.getParticipantStatisticView().getResParticipantPanel().add(
                                                    new Label("Personen er en " + participant.getPersonType())
                                            );
                                            if (team != null){
                                                participantView.getParticipantStatisticView().getResParticipantPanel().add(
                                                        new Label("Personen er med i holdet: " + team.getTeamName())
                                                );
                                            }

                                            participantView.getParticipantStatisticView().getResParticipantPanel().add(
                                                    new Label("Niveau: " + participant.getCyclistType())
                                            );

                                            if (firm != null){
                                                participantView.getParticipantStatisticView().getResParticipantPanel().add(
                                                        new Label("Deltager sammen med " + firm.getFirmName())
                                                );
                                            }

                                            participantView.getParticipantStatisticView().getResParticipantPanel().add(returnToParticipantSearchBtn);
                                            participantView.getParticipantStatisticView().getParticipantSearchDeck().showWidget(1);

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

    /**
     * ClickHandler der skifter DeckPanel tilbage til søge funktionen efter firma
     */
    class ReturnToFirmSearchClickHandler implements ClickHandler{
        /**
         * Called when a native click event is fired.
         *
         * @param event the {@link ClickEvent} that was fired
         */
        @Override
        public void onClick(ClickEvent event) {
            participantView.getParticipantStatisticView().getFirmSearchDeck().showWidget(0);
        }
    }

    /**
     * ClickHandler der skifter DeckPanel tilbage til søge funktion efter hold
     */
    class ReturnToTeamSearchClickHandler implements ClickHandler{
        /**
         * Called when a native click event is fired.
         *
         * @param event the {@link ClickEvent} that was fired
         */
        @Override
        public void onClick(ClickEvent event) {
            participantView.getParticipantStatisticView().getTeamSearchDeck().showWidget(0);
        }
    }

    /**
     * ClickHandler der skifter DeckPanel tilbage til søge funktion efter deltager
     */
    class ReturnToParticipantSearchClickHandler implements ClickHandler{
        /**
         * Called when a native click event is fired.
         *
         * @param event the {@link ClickEvent} that was fired
         */
        @Override
        public void onClick(ClickEvent event) {
            participantView.getParticipantStatisticView().getParticipantSearchDeck().showWidget(0);
        }
    }

    /**
     * Metode der sætter centerView MyProfileView til at være lig deltager attributter
     */
    private void createMyProfile(){

        //Sætter name
        rpcService.getParticipantName(currentParticipant.getEmail(), new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(String result) {
                if(result != null){
                    participantView.getMyProfileView().getNameLabel().setText(
                            "Dit navn: " + result);
                }
            }
        });

        //Sætter email
        participantView.getMyProfileView().getEmailLabel().setText(
                "Email: " + currentParticipant.getEmail());

        rpcService.getParticipantCyclistType(currentParticipant.getEmail(), new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(String result) {
                participantView.getMyProfileView().getCyclistTypeLabel().setText(
                        "Din cyclist-type: " + result);
            }
        });

        rpcService.getParticipantFirmName(currentParticipant.getEmail(), new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(String result) {
                participantView.getMyProfileView().getFirmLabel().setText(
                        "Firma: " + result);
            }
        });

        rpcService.getParticipantTeamName(currentParticipant.getEmail(), new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(String result) {
                if (result == null){
                    participantView.getMyProfileView().getTeamLabel().setText(null);
                } else {
                    participantView.getMyProfileView().getTeamLabel().setText(
                            "Dit hold: " + result);
                }
            }
        });
    }

    /**
     *Metode der styre hele MyTeamView knapper og funktioner
     * Her er det muligt at se informationer om ens pågældende hold
     * Se deltager og emails på ens hold
     *
     * Hvis man er Team Kaptajn er det muligt at fjerne og tilføje folk, samt ændre holdnavn
     */
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

                CellTable<Participant> cellTable = participantView.getMyTeamView().getCellTable();
                if (cellTable.getColumnCount() > 0){
                    for (int i = 0; i <= cellTable.getColumnCount() + 1; i++){
                        cellTable.removeColumn(0);
                    }
                }

                participantListDataProvider.addDataDisplay(cellTable);

                TextColumn<Participant> participantNameCol = new TextColumn<Participant>() {
                    @Override
                    public String getValue(Participant object) {
                        return object.getName();
                    }
                };

                TextColumn<Participant> emailCol = new TextColumn<Participant>() {
                    @Override
                    public String getValue(Participant object) {
                        return object.getEmail();
                    }
                };

                cellTable.addColumn(participantNameCol, "Deltager");
                cellTable.addColumn(emailCol, "Email");

                if (currentParticipant.getPersonType().equalsIgnoreCase("TEAMCAPTAIN")){
                    Column<Participant, Participant> removeParticipantCol = new Column<Participant, Participant>(new ActionCell<>("Fjern fra hold", participantView.getMyTeamView().getDelegate())) {
                        @Override
                        public Participant getValue(Participant object) {
                            return object;
                        }
                    };
                    cellTable.addColumn(removeParticipantCol);
                }

                participantNameCol.setSortable(true);
                emailCol.setSortable(true);

                ColumnSortEvent.ListHandler<Participant> sortHandler = new ColumnSortEvent.ListHandler<>(participantListDataProvider.getList());

                sortHandler.setComparator(participantNameCol, new Comparator<Participant>() {
                    @Override
                    public int compare(Participant o1, Participant o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                sortHandler.setComparator(emailCol, new Comparator<Participant>() {
                    @Override
                    public int compare(Participant o1, Participant o2) {
                        return o1.getEmail().compareTo(o2.getEmail());
                    }
                });

                cellTable.addColumnSortHandler(sortHandler);

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
                participantView.getMyTeamView().getTeamIDLabel().setText(result.getTeamID() + "");
                participantView.getMyTeamView().getTeamNameLabel().setText(result.getTeamName());
                participantView.getMyTeamView().getNumberOfParticipantsLabel().setText(result.getParticipants().size()+ "");
            }
        });

        rpcService.getAllParticipantsInTeamFromTeamID(currentParticipant.getTeamID(), new AsyncCallback<ArrayList<Participant>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ArrayList<Participant> result) {
                participantView.getMyTeamView().getNumberOfParticipantsLabel().setText(result.size()+ "");
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
                participantView.getMyTeamView().getFirmNameLabel().setText(firm.getFirmName());
            }
        });
    }

    /**
     * Denne metode styre hele ParticipantStaticView, hvor der oprettes et vertikalt panel med
     * statistikker over samtlige hold og deres statistikker
     * 3 metoder der gør det muligt at søge efter firma, søge efter hold og søge efter deltager
     */
    private void createStatistic(){
        /**
         * Laver en liste over de forskellige firmaer og deres stats
         */
        rpcService.getAllFirmsAndTeamsAndParticipants(new AsyncCallback<ArrayList<Firm>>() {
            @Override
            public void onFailure(Throwable caught) {Window.alert(caught.getMessage());}

            @Override
            public void onSuccess(ArrayList<Firm> firms) {
                rpcService.getAllTeamsAndTeamNameAndParticipants(new AsyncCallback<ArrayList<Team>>() {
                    @Override
                    public void onFailure(Throwable caught) {Window.alert(caught.getMessage());}

                    @Override
                    public void onSuccess(ArrayList<Team> teams) {
                        rpcService.getAllParticipantsAndTeamNameAndFirmName(new AsyncCallback<ArrayList<Participant>>() {
                            @Override
                            public void onFailure(Throwable caught) {Window.alert(caught.getMessage());}

                            @Override
                            public void onSuccess(ArrayList<Participant> participants) {

                                /**
                                 * Denne metode sørger for at alt fra panelet bliver slettet, så den ikke bygger videre på noget der allerede er der.
                                 */
                                participantView.getParticipantStatisticView().getStatisticPanel().clear();

                                participantView.getParticipantStatisticView().getStatisticPanel().add(
                                        new Label("Der er i alt " + firms.size() + " firmaer tilmeldt")
                                );

                                participantView.getParticipantStatisticView().getStatisticPanel().add(
                                        new Label("Der er i alt " + teams.size() + " hold tilmeldt")
                                );

                                participantView.getParticipantStatisticView().getStatisticPanel().add(
                                        new Label("Der er i alt " + participants.size() + " deltagere tilmeldt")
                                );


                                /**
                                 * Enhanced for loop der opretter midlertidelige paneler med alle hold oplysninger
                                 */
                                for (Firm firm :firms) {
                                    VerticalPanel tempVerticalPanel = new VerticalPanel();
                                    tempVerticalPanel.addStyleName("fakefakeBtn margintop marginbot");

                                    participantView.getParticipantStatisticView().getStatisticPanel().add(
                                            tempVerticalPanel
                                    );

                                    tempVerticalPanel.add(new Label(firm.getFirmName()));

                                    int numberOfTeamsInFirm = 0;
                                    for (Team team : teams) {
                                        if (team.getFirmID() == firm.getID()){
                                            numberOfTeamsInFirm++;
                                        }
                                    }

                                    int numberOfParticipantsInFirm = 0;
                                    for (Participant participant: participants){
                                        if (participant.getFirmID() == firm.getID()){
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
        rpcService.getAllFirmsAndTeamsAndParticipants(new AsyncCallback<ArrayList<Firm>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ArrayList<Firm> firms) {

                for (Firm firm : firms){
                    participantView.getParticipantStatisticView().getFirmsList1().addItem(
                            firm.getFirmName()
                    );
                }
            }
        });
        /**
         * Laver holdsøgningen ved at indsætte de forskellige firma og holdnavne i listerne
         */
        rpcService.getAllFirmsAndTeamsAndParticipants(new AsyncCallback<ArrayList<Firm>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ArrayList<Firm> firms) {

                for (Firm firm : firms){
                    participantView.getParticipantStatisticView().getFirmsList2().addItem(
                            firm.getFirmName()
                    );
                }

                rpcService.getAllTeamsAndTeamNameAndParticipants(new AsyncCallback<ArrayList<Team>>() {
                    @Override
                    public void onFailure(Throwable caught) {

                    }

                    @Override
                    public void onSuccess(ArrayList<Team> teams) {
                        for (Team team: teams){
                            if (team.getFirmID() == firms.get(0).getID()){
                                participantView.getParticipantStatisticView().getTeamList().addItem(team.getTeamName());
                            }
                        }
                    }
                });
            }
        });
    }
}

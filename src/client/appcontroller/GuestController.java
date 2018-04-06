package client.appcontroller;

import client.ui.Content;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import rpc.ApplicationServiceAsync;
//import server.withoutDB.Data;
import shared.DTO.Admin;
import shared.DTO.Participant;
import shared.DTO.Person;

import java.util.ArrayList;


public class GuestController {

    private Content content;
    private ListDataProvider<Participant> participantListDataProvider;
//    private Data data;
    private ArrayList<Participant> participants;
    private ApplicationServiceAsync rpcService;

    public GuestController(Content content, ApplicationServiceAsync rpcService){
        this.content = content;
        this.participantListDataProvider = new ListDataProvider<>();
        this.rpcService = rpcService;


//        Tilføjer clickhandlers til forskellige elementer på siden
        addClickHandlers();

//        Opretter tabellen
        createTable();
    }

    /**
     * En clickhandler klasse som indeholder en onclick metode.
     */
    class GuestClickHandlers implements ClickHandler{

        @Override
        public void onClick(ClickEvent event) {


            if (event.getSource() == content.getGuestView().getLogindBtn()){
                content.getGuestView().changeView(0);
            } else if (event.getSource() == content.getGuestView().getTilmeldingBtn()){
                content.getGuestView().changeView(1);
            } else if (event.getSource() == content.getGuestView().getLogoImg()){
                content.getGuestView().changeView(content.getGuestView().getStartView());
            } else if (event.getSource() == content.getGuestView().getStatistiskBtn()){
                content.getGuestView().changeView(content.getGuestView().getGuestStatisticView());
//                Dette kald skal være her for at tabellen viser sine data.
                participantListDataProvider.refresh();
                //Skifter til Participant view
            }
        }
    }

    class LoginClickHandler implements ClickHandler{

        @Override
        public void onClick(ClickEvent event) {
            String username = content.getGuestView().getLoginView().getUsernameTB().getText();
            String password = content.getGuestView().getLoginView().getPasswordTB().getText();

            rpcService.authorizePerson(username, password, new AsyncCallback<Person>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Server fejl");
                    }

                    @Override
                    public void onSuccess(Person result) {
                        if (result == null){
                            content.getGuestView().getLoginView().getErrMessageLabel().setText("Email og password matcher ikke");
                        }
                        if (result instanceof  Participant){
                            content.switchToParticipantView();
                            new ParticipantController(content, (Participant) result, rpcService);
                        }

                        if (result instanceof Admin){
                            content.switchToAdminView();
                            new AdminController(content, rpcService);
                        }
                    }
                });


        }
    }

    class ShowInfoHandler implements ActionCell.Delegate<Participant>{
        @Override
        public void execute(Participant object) {
            Window.alert("Hello");
        }
    }

    class CreateParticipantClickHandler implements ClickHandler{
        private String name;
        private String email;
        private String cyclistType;
        private String password;
        private String passwordCheck;
        private ArrayList<String> errMessage;

        @Override
        public void onClick(ClickEvent event) {
            this.email = content.getGuestView().getSignUpView().getEmailField().getText();
            this.name = content.getGuestView().getSignUpView().getNameField().getText();
            this.cyclistType = content.getGuestView().getSignUpView().getCyclistTypeList().getSelectedValue();
            this.password = content.getGuestView().getSignUpView().getPasswordField().getText();
            this.passwordCheck = content.getGuestView().getSignUpView().getPasswordCheckField().getText();
            this.errMessage = new ArrayList<>();

            Boolean isCorrectSignUp = validateName() & validateEmail() & validatePassword();
            content.getGuestView().getSignUpView().getErrorMessageLabel().setHTML ( "<p>" + String.join("<br>", errMessage) + "</p>");

            //Her benytter jeg kun et enkelt &. Dette heder bitwise operatoren og evaluere begge sider uden at stoppe dens kondition fra at "short circuiting"
            if (isCorrectSignUp){
                rpcService.createParticipant(email, name, cyclistType, password, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        System.out.println(caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (!result){
                            Window.alert("Email already exist");
                        } else {
                            Window.alert("Successful added participant!");
                        }
                    }
                });
            }

        }

        private boolean validateEmail(){
            int atPosition, dotPosition;

            atPosition = email.indexOf("@");
            dotPosition = email.lastIndexOf(".");

            //Tjekker om det er en valid email-adresse før den efterspørger databasen.
            if (atPosition > 0 &&
                    dotPosition > atPosition &&
                    dotPosition < (email.length()-2) &&
                    email.lastIndexOf("@") == email.indexOf("@") &&
                    !email.contains(" ")) {

                return true;
            }
            errMessage.add("Din Email er ikke valid. Tjek for eventuelle tastefejl ;)");
            return false;
        }

        private boolean validateName() {
            RegExp regExp = RegExp.compile("^[a-zA-Z ]+$");
            MatchResult matchResult = regExp.exec(name);
            Boolean valid = matchResult != null;
            if(!valid)
                errMessage.add("Dit navn må ikke indeholde specialtegn eller tal i vores program ;)");
            return valid;
        }

        private boolean validatePassword(){

            boolean error = false;

            Boolean hasSmallLetters = RegExp.compile("[a-z]").exec(password) != null;
            Boolean hasCapitalLetters = RegExp.compile("[A-X]").exec(password) != null;
            Boolean hasSpaces = RegExp.compile("[ ]").exec(password) != null;
            Boolean hasNumbers = RegExp.compile("[0-9]").exec(password) != null;
            Boolean hasSpecialSymbols = RegExp.compile("[^a-zA-Z0-9 ]+").exec(password) != null;

            if (!password.equals(passwordCheck)){
                errMessage.add("Kodeordene matcher ikke hinanden ;)");
                error = true;
            }

            if (!hasSmallLetters || !hasCapitalLetters){
                errMessage.add("Dit kodeord skal både indeholde store og små bogstaver ;)");
                error = true;
            }

            if (hasSpaces){
                errMessage.add("Der må ikke forekomme mellemrum i dit kodeord ;)");
                error = true;
            }

            if (!hasNumbers){
                errMessage.add("Dit kodeord skal have numre ;)");
                error = true;
            }

            if (!hasSpecialSymbols){
                errMessage.add("Dit kodekord skal have et specialsymbol ;)");
                error = true;
            }

            return !error;
        }
    }


    private void addClickHandlers(){
        content.getGuestView().addClickHandlers(new GuestClickHandlers());
        content.getGuestView().getStatisticView().addClickHandler(new ShowInfoHandler());
        content.getGuestView().getSignUpView().addClickHandlers(new CreateParticipantClickHandler());
        content.getGuestView().getLoginView().addClickHandler(new LoginClickHandler());
    }

    private void createTable(){


        rpcService.getAllParticipants(new AsyncCallback<ArrayList<Participant>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(ArrayList<Participant> result) {
                content.getGuestView().getStatisticView().initTable(participantListDataProvider);
                participantListDataProvider.getList().addAll(result);
            }
        });
    }
}

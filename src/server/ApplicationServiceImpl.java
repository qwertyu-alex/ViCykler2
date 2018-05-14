package server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mysql.jdbc.Connection;
import client.rpc.ApplicationService;
import shared.DTO.*;

import java.sql.*;
import java.util.ArrayList;

/**
 * DAO
 */
public class ApplicationServiceImpl extends RemoteServiceServlet implements ApplicationService {

    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/vicykler";
    private final String USERNAME = "root";
    private final String PASSWORD = "qwerty123";
    //Meme_1234
    private Connection connection;

    public ApplicationServiceImpl(){
        System.out.println("Running");



        try {
             connection = (Connection) DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            System.out.println("Successful");
        } catch (SQLException err){
            err.printStackTrace();
            //https://stackoverflow.com/questions/2434592/difference-in-system-exit0-system-exit-1-system-exit1-in-java

            System.exit(1);
        }
    }

    /**
     *AuthorizePerson metoden, tager 2 parameter fra login som parameter og derefter tjekker om de matcher en
     * en person i databasen. Hvis både email og password matcher en person, vil personen blive retuneret efter
     * hvilken brugertype personen er
     */
    @Override
    public Person authorizePerson(String email, String password) throws Exception {
        Person foundPerson = null;

        PreparedStatement findMatch = connection.prepareStatement("SELECT * FROM persons WHERE Email LIKE ? AND Password LIKE ?");
        findMatch.setString(1, email);
        findMatch.setString(2, password);
        try {
            ResultSet resultSet = findMatch.executeQuery();
            /**
             * Tjekke om man er en participant, teamcaptain eller admin
             */
            if (resultSet.next()){
                if (resultSet.getString("PersonType").equalsIgnoreCase("PARTICIPANT")
                        || resultSet.getString("PersonType").equalsIgnoreCase("TEAMCAPTAIN")){
                    Participant foundParticipant = new Participant();

                    foundParticipant.setName(resultSet.getString("PersonName"));
                    foundParticipant.setEmail(resultSet.getString("Email"));
                    foundParticipant.setCyclistType(resultSet.getString("CyclistType"));
                    foundParticipant.setTeamID(resultSet.getInt("TeamID"));
                    foundParticipant.setFirmID(resultSet.getInt("FirmID"));
                    foundParticipant.setPersonType(resultSet.getString("PersonType"));

                    foundPerson = foundParticipant;
                } else if (resultSet.getString("PersonType").equalsIgnoreCase("ADMIN")){
                    foundPerson = new Admin();
                }
            }
        } catch (SQLException err){
            err.printStackTrace();
            return null;
        }
        return foundPerson;
    }

    /**
     *Metode der gemmer alle deltager fra et bestemt team i en arrayliste
     */
    @Override
    public ArrayList<Participant> getAllParticipantsInTeamFromTeamID(int teamID){

        ArrayList<Participant> participants = new ArrayList<>();
        Participant participant;

        try {
            PreparedStatement findPersons = connection.prepareStatement("SELECT * FROM persons WHERE TeamID = ?");
            findPersons.setInt(1, teamID);
            ResultSet resultSet = findPersons.executeQuery();

            while(resultSet.next()){
                participant = new Participant();
                participant.setName(resultSet.getString("PersonName").toLowerCase());
                participant.setEmail(resultSet.getString("Email").toLowerCase());
                participant.setCyclistType(resultSet.getString("CyclistType").toLowerCase());
                participant.setPersonType(resultSet.getString("PersonType"));
                participant.setTeamID(resultSet.getInt("TeamID"));
                participant.setFirmID(resultSet.getInt("FirmID"));
                participants.add(participant);
          }
        } catch (SQLException err){
            err.printStackTrace();
            return null;
        }

        return participants;
    }

    /**
     *Gemmer en personens navn, email, cyklisttype, brugertype, firma nummer og navn, hold nummer og navn
     */
    @Override
    public ArrayList<Participant> getAllParticipantsAndTeamNameAndFirmName() throws Exception {

        ArrayList<Participant> participants = new ArrayList<>();
        Participant participant;
        ResultSet resultSet = null;

        try {
            PreparedStatement findPersons = connection.prepareStatement(
                    "SELECT * FROM persons");
            resultSet = findPersons.executeQuery();

            while(resultSet.next()){
                //If statement fjerner admin fra arrayliste
                if(!resultSet.getString("PersonType").equalsIgnoreCase("ADMIN")){
                    participant = new Participant();

                    //Person informationer
                    participant.setName(resultSet.getString("PersonName").toLowerCase());
                    participant.setEmail(resultSet.getString("Email").toLowerCase());
                    participant.setCyclistType(resultSet.getString("CyclistType").toLowerCase());
                    participant.setPersonType(resultSet.getString("PersonType"));

                    participant.setFirmID(resultSet.getInt("FirmID"));
                    participant.setTeamID(resultSet.getInt("TeamID"));

                    //Finder firmanavn ud fra ID
                    PreparedStatement firmName = connection.prepareStatement("SELECT FirmName from firms WHERE FirmID = ?");
                    firmName.setInt(1, resultSet.getInt("FirmID"));
                    ResultSet firmNameRes = firmName.executeQuery();
                    if (firmNameRes.next()){
                        participant.setFirmName(firmNameRes.getString("FirmName"));
                    } else {
                        throw new SQLException();
                    }

                    //Finder holdnavn ud fra ID
                    if (resultSet.getInt("TeamID") != 0){
                        PreparedStatement teamName = connection.prepareStatement("SELECT TeamName from teams WHERE TeamID = ?");
                        teamName.setInt(1, resultSet.getInt("TeamID"));
                        ResultSet teamNameRes = teamName.executeQuery();
                        if (teamNameRes.next()){
                            participant.setTeamName(teamNameRes.getString("TeamName"));
                        }
                    }

                    participants.add(participant);

                }
            }
        } catch (SQLException err){
            err.printStackTrace();
        }
        return participants;
    }

    /**
     * Gemmer alle personer fra et valgt firma i en arrayliste
     */
    @Override
    public ArrayList<Participant> getAllParticipantsInFirmFromFirmID(int firmID) throws Exception {
        ArrayList<Participant> participantsList = new ArrayList<>();

        try {
            PreparedStatement participants = connection.prepareStatement("SELECT * FROM persons WHERE FirmID = ?");
            participants.setInt(1, firmID);
            ResultSet participantsRes = participants.executeQuery();
            ResultSetMetaData meta = participantsRes.getMetaData();

            //while loop der gemmer alle personer i arrayliste
            if (meta.getColumnCount() > 0){
                while (participantsRes.next()){
                    Participant participant = new Participant();

                    participant.setName(participantsRes.getString("PersonName").toLowerCase());
                    participant.setEmail(participantsRes.getString("Email").toLowerCase());
                    participant.setCyclistType(participantsRes.getString("CyclistType").toLowerCase());
                    participant.setPersonType(participantsRes.getString("PersonType"));

                    participant.setFirmID(participantsRes.getInt("FirmID"));
                    participant.setTeamID(participantsRes.getInt("TeamID"));

                    participantsList.add(participant);
                }
                return participantsList;
            } else {
                System.out.println("Ingen personer med det pågældende firmID");
                throw new SQLException();
            }

        } catch (SQLException err){
            err.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Metode der insætter ny bruger i databasen
     */
    @Override
    public String createParticipant(Participant newParticipant) throws Exception {
        String email = newParticipant.getEmail();
        String name = newParticipant.getName();
        String cyclistType = newParticipant.getCyclistType();
        String password = newParticipant.getPassword();
        String personType = newParticipant.getPersonType();
        int firmID = newParticipant.getFirmID();
        int teamID = newParticipant.getTeamID();

        /**
         * Fordi at der skal være en type for at man kan logge ind
         */
        if (personType == null){
            personType = "PARTICIPANT";
        }

        ArrayList<String> emailList = new ArrayList<>();
        ResultSet emailsResultSet = null;

        try {

            /***
             * Først skal der tjekkes om emailen allerede eksisterer.
             */
            PreparedStatement emailsPreparedStatement = connection.prepareStatement("SELECT Email FROM persons WHERE Email LIKE ?");
            emailsPreparedStatement.setString(1, Character.toString(email.charAt(0)) + "%");
            emailsResultSet = emailsPreparedStatement.executeQuery();

            while(emailsResultSet.next()){
                emailList.add(emailsResultSet.getString("Email"));
            }

            for (String emailName: emailList) {
                if (emailName.equalsIgnoreCase(email)){
                    return "Emailen eksisterer ikke";
                }
            }

            emailsPreparedStatement.close();
            emailsResultSet.close();

            /**
             * Finde firmID ud fra firmName som er givet
             */
            if (newParticipant.getFirmName() != null){
                System.out.println("FirmName not null");
                PreparedStatement getFirmID = connection.prepareStatement("SELECT FirmID FROM firms WHERE FirmName = ?");
                getFirmID.setString(1,newParticipant.getFirmName());
                ResultSet getFirmIDRes = getFirmID.executeQuery();
                if (getFirmIDRes.next()){
                    firmID = getFirmIDRes.getInt("FirmID");
                    System.out.println(firmID);
                }
            }



            /**
             * Her bliver personen oprettet i databasen.
             */
            PreparedStatement createParticipant = connection.prepareStatement("INSERT INTO persons(PersonName, Email, Password, PersonType, CyclistType, FirmID, TeamID  ) VALUES (?,?,?,?,?,?,?)");
            createParticipant.setString(1, name);
            createParticipant.setString(2, email);
            createParticipant.setString(3, password);
            createParticipant.setString(4, personType);
            createParticipant.setString(5, cyclistType);
            if (firmID != 0){
                createParticipant.setInt(6, firmID);
            } else {
                createParticipant.setObject(6, null);
            }

            if (teamID != 0){
                createParticipant.setInt(7, teamID);
            } else {
                createParticipant.setObject(7, null);
            }

            createParticipant.executeUpdate();

        } catch (SQLException err){
            err.printStackTrace();
        } finally {
            try {
                emailsResultSet.close();
            } catch (Exception err){
                err.printStackTrace();
            }
        }
        return "Personen er blevet oprettet med success";
    }

    /**
     * Metode der indsætter nyt hold i database
     */
    @Override
    public String createTeam(String teamName, String teamCaptainEmail) throws Exception {
        int firmID;
        System.out.println("Kører createTeam");
        try {

            /**
             * Finder firmID ud fra en participant som senere bliver holdkaptain
             */
            PreparedStatement getFirmID = connection.prepareStatement("SELECT FirmID FROM persons WHERE EMAIL = ?");
            getFirmID.setString(1, teamCaptainEmail);
            ResultSet getFirmIDRes = getFirmID.executeQuery();
            if (getFirmIDRes.next()){
                firmID = getFirmIDRes.getInt("FirmID");
            } else {
                System.out.println("Kaster exception fra createTeam. Intet firma forbundet til personen");
                throw new Exception();
            }

            /**
             * Indsætter det nye hold i databasen
             */
            PreparedStatement createTeam = connection.prepareStatement("INSERT INTO teams(TeamName,FirmID) VALUES (?,?)");
            createTeam.setString(1, teamName);
            createTeam.setInt(2, firmID);
            createTeam.executeUpdate();

            /**
             * Find holdID
             */
            PreparedStatement findID = connection.prepareStatement("SELECT TeamID from teams WHERE TeamName = ? AND FirmID = ?");
            findID.setString(1,teamName);
            findID.setInt(2, firmID);
            ResultSet findIDRes = findID.executeQuery();
            int teamID = 0;
            if (findIDRes.next()){
                teamID = findIDRes.getInt("TeamID");
            } else {
                System.out.println("ERROR: Intet teamID");
                throw new SQLException();
            }


            /**
             * Sætter en participant til at være holdkaptain
             */
            PreparedStatement findParticipant = connection.prepareStatement("UPDATE persons SET PersonType = 'TEAMCAPTAIN', TeamID = ? WHERE Email LIKE ?");
            findParticipant.setInt(1, teamID);
            findParticipant.setString(2, teamCaptainEmail);
            findParticipant.executeUpdate();

        } catch (SQLException err){
            err.printStackTrace();
            return "Fejl med at oprette hold";
        }

        return "Holdet er blevet oprettet";
    }

    /**
     * Metode der indsætter et nyt oprettet hold i databasen
     */
    @Override
    public String createFirm(String name) {
        try {
            PreparedStatement create = connection.prepareStatement("INSERT INTO firms(FirmName) VALUES (?)");
            create.setString(1, name);
            create.executeUpdate();
            return "Firmaet er blevet oprettet";

        } catch (SQLException err){
            err.printStackTrace();
            return "Fejl med at oprette firma";
        }
    }

    /**
     * Metode der tilføjer en deltager til et team ved enten at flytte deltager fra et hold eller også
     * er deltageren ikke på et hold
     */
    @Override
    public String addParticipantsToTeam(Team currentTeam, ArrayList<String> participantEmails){
        /**
         * Opdater participants som skal være i holdet, inklusiv teamCaptain,
         * så deres teamID matcher det nye team.
         */
        try {
            System.out.println("Størrelse på alle deltagere " + participantEmails.size());
            if (participantEmails.size() > 0){
                for (String participantEmail : participantEmails) {
                    /**
                     * Skal først tjekke om de har samme FirmID som holdet
                     */
                        PreparedStatement firmID = connection.prepareStatement("SELECT FirmID FROM persons WHERE Email = ?");
                        firmID.setString(1, participantEmail);
                        ResultSet firmIDRes = firmID.executeQuery();
                        if (firmIDRes.next()){
                            if (currentTeam.getFirmID() == firmIDRes.getInt("FirmID")){
                                System.out.println("True");
                                /**
                                 * Updater alle personer i ArrayListen så de har samme TeamID som holdet
                                 */
                                PreparedStatement changeParticipant = connection.prepareStatement("UPDATE persons SET TeamID = ?, PersonType='PARTICIPANT' WHERE Email = ?");
                                changeParticipant.setInt(1,currentTeam.getTeamID());
                                changeParticipant.setString(2, participantEmail);
                                changeParticipant.executeUpdate();
                            }
                        } else {
                            System.out.println("ERR Personen har intet firmaID");
                            throw new SQLException();
                        }
                }
            }
            return "Personerne er blevet tilføjet til holdet";
        } catch (SQLException err){
            err.printStackTrace();
            return "Personerne kunne ikke tilføjes til holdet";
        }
    }

    /**
     * Metoder der finder en specifik deltager ud fra hans email og derefter retunere alle hans oplysninger
     */
    @Override
    public Participant getParticipant(String email) throws Exception{

        Participant participant = new Participant();

        try{
            PreparedStatement foundParticipant = connection.prepareStatement("SELECT * FROM persons WHERE Email = ?");
            foundParticipant.setString(1,email);
            ResultSet foundParticipantRes = foundParticipant.executeQuery();
            if (foundParticipantRes.next()){
                participant.setName(foundParticipantRes.getString("PersonName"));
                participant.setEmail(foundParticipantRes.getString("Email"));
                participant.setPassword(foundParticipantRes.getString("Password"));
                participant.setPersonType(foundParticipantRes.getString("PersonType"));
                participant.setCyclistType(foundParticipantRes.getString("CyclistType"));
                participant.setFirmID(foundParticipantRes.getInt("FirmID"));
                participant.setTeamID(foundParticipantRes.getInt("TeamID"));

                return participant;
            } else {
                System.out.println("Ingen person. Kaster SQLErr");
                throw new SQLException();
            }
        } catch (SQLException err){
            err.printStackTrace();
            return null;
        }
    }

    /**
     * Metode der retunere en deltagers navn udfra en søgt email
     */
    @Override
    public String getParticipantName(String email) throws Exception {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT PersonName FROM persons WHERE Email LIKE ?");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString("PersonName");
        } catch (SQLException err){
            err.printStackTrace();
            return "**ERROR Serverfejl**";
        }
    }

    /**
     * Metode der retunere en persons cyklisttype udfra en specifik email
     */
    @Override
    public String getParticipantCyclistType(String email) throws Exception {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT CyclistType FROM persons WHERE Email LIKE ?");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString("CyclistType");
        } catch (SQLException err){
            err.printStackTrace();
            return "**ERROR Serverfejl**";
        }
    }

    /**
     * Metode der returnere et firma udfra en persons firma id ved at indtaste personens email
     */
    @Override
    public String getParticipantFirmName(String email) throws Exception {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(  "SELECT FirmName FROM firms INNER JOIN persons ON firms.FirmID = persons.FirmID WHERE persons.Email = ? ");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return resultSet.getString("FirmName");
            } else {
                return null;
            }
        } catch (SQLException err){
            err.printStackTrace();
            return null;
        }
    }

    /**
     * Metode der henter en persons holdnavn udfra hans email
     * hold navnet bliver fundet ved at finde et team med den søgte email og tilsvarende hold ID
     * Hvis ikke personen er på et hold, bliver der retuneret en error
     */
    @Override
    public String getParticipantTeamName(String email) throws Exception {
        try{
            PreparedStatement getTeamID = connection.prepareStatement("SELECT TeamID FROM persons WHERE Email = ?");
            getTeamID.setString(1, email);
            ResultSet getTeamIDResult = getTeamID.executeQuery();
            getTeamIDResult.next();
            int teamID = getTeamIDResult.getInt("TeamID");

            //Hvis der er et hold
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT teams.TeamName, persons.PersonName FROM teams INNER JOIN persons ON teams.TeamID WHERE teams.TeamID = ? AND persons.Email = ?");
            preparedStatement.setInt(1, teamID);
            preparedStatement.setString(2, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return resultSet.getString("TeamName");
            } else {
                return null;
            }
        } catch (SQLException err){
            err.printStackTrace();
            return null;
        }
    }

    /**
     * Metode der retunere en deltageres password udfra en søgt email
     */
    @Override
    public String getParticipantPassword(String email) throws Exception {

        try {
            PreparedStatement getPassword = connection.prepareStatement("SELECT Password FROM persons WHERE Email = ?");
            getPassword.setString(1, email);
            ResultSet resultSet = getPassword.executeQuery();
            resultSet.next();
            return resultSet.getString("Password");
        } catch (SQLException err){
            err.printStackTrace();
            return "**ERROR Serverfejl**";
        }
    }

    /**
     * Metode der retunere alle teams navne og deres deltagere
     */
    @Override
    public ArrayList<Team> getAllTeamsAndTeamNameAndParticipants() throws Exception {
        ArrayList<Team> teams = new ArrayList<>();

        try{
            PreparedStatement getTeams = connection.prepareStatement("SELECT * FROM teams INNER JOIN firms ON firms.FirmID = teams.FirmID");
            ResultSet getTeamsRes = getTeams.executeQuery();

            //Gemmer alle teams i en arrayliste med teamID, TeamName, FirmID & FirmName
            while (getTeamsRes.next()){
                Team tempTeam = new Team();
                tempTeam.setTeamID(getTeamsRes.getInt("TeamID"));
                tempTeam.setTeamName(getTeamsRes.getString("TeamName"));
                tempTeam.setFirmName(getTeamsRes.getString("FirmName"));
                tempTeam.setFirmID(getTeamsRes.getInt("FirmID"));
                teams.add(tempTeam);
            }

            //Finder alle participant i disse teams
            for (Team team: teams) {
                PreparedStatement getParticipants = connection.prepareStatement("SELECT persons.email FROM persons INNER JOIN teams ON persons.TeamID = teams.TeamID WHERE teams.TeamID = ?");
                getParticipants.setInt(1, team.getTeamID());

                ResultSet participantsRes = getParticipants.executeQuery();

                //Gemmer alle deltagers Email i arrraylisten
                while(participantsRes.next()){
                    team.getParticipants().add(participantsRes.getString("Email"));
                }
            }
            return teams;
        } catch (Exception err){
            err.printStackTrace();
            return null;
        }
    }

    /**
     * En metode der henter alle firmer, deres hold og deres deltagere
     */
    @Override
    public ArrayList<Firm> getAllFirmsAndTeamsAndParticipants() throws Exception {

        ArrayList<Firm> firms = new ArrayList<>();

        try{
            PreparedStatement getFirms = connection.prepareStatement("SELECT * FROM firms");
            ResultSet getFirmsRes = getFirms.executeQuery();

            while (getFirmsRes.next()){
                Firm tempFirm = new Firm();
                tempFirm.setFirmName(getFirmsRes.getString("FirmName"));
                tempFirm.setID(getFirmsRes.getInt("FirmID"));
                firms.add(tempFirm);
            }

            getFirms.close();
            getFirmsRes.close();
            // henter alle hold i firmaet
            for (Firm firm : firms) {
                PreparedStatement getTeams = connection.prepareStatement("SELECT teams.TeamID FROM teams INNER JOIN firms ON firms.FirmID = teams.FirmID WHERE firms.FirmName = ?");
                getTeams.setString(1, firm.getFirmName());
                ResultSet getTeamsRes = getTeams.executeQuery();

                while (getTeamsRes.next()){
                    firm.getTeams().add((Integer)getTeamsRes.getInt("TeamID"));
                    firm.getTeams().get(0);
                }

                getTeams.close();
                getTeamsRes.close();
            }
            // henter alle deltagere i firmaet
            for (Firm firm : firms){
                PreparedStatement getParticipants = connection.prepareStatement("SELECT persons.Email FROM persons INNER JOIN firms ON persons.FirmID = firms.FirmID WHERE firms.FirmName = ?");
                getParticipants.setString(1, firm.getFirmName());
                ResultSet getParticipantsRes = getParticipants.executeQuery();

                while (getParticipantsRes.next()){
                    firm.getParticipants().add(getParticipantsRes.getString("Email"));
                }
                getParticipants.close();
                getParticipantsRes.close();
            }

            return firms;

        } catch (SQLException err){
            err.printStackTrace();
            return null;
        }
    }

    /**
     * En metode der opdatere oplysninger om en deltager
     */
    @Override
    public Participant changeParticipantInfo(Participant currentParticipant, Participant changingParticipant) throws Exception  {
        try {

            setAnotherTeamCaptainIfTeamCaptain(currentParticipant.getEmail());

            PreparedStatement updateParticipant = connection.prepareStatement(
                    "UPDATE persons SET PersonName = ?, Email = ?,  " +
                            "Password = ?, PersonType = ?, CyclistType = ?, TeamID = ?, FirmID = ? WHERE Email = ?");

            updateParticipant.setString(1,changingParticipant.getName());
            updateParticipant.setString(2,changingParticipant.getEmail());
            updateParticipant.setString(3,changingParticipant.getPassword());
            updateParticipant.setString(4,changingParticipant.getPersonType());
            updateParticipant.setString(5,changingParticipant.getCyclistType());
            updateParticipant.setInt(6,changingParticipant.getTeamID());
            updateParticipant.setInt(7,changingParticipant.getFirmID());
            updateParticipant.setString(8, currentParticipant.getEmail());

            updateParticipant.executeUpdate();
            return changingParticipant;
        } catch (SQLException err){
            err.printStackTrace();
            return changingParticipant;
        }
    }

    /**
     * Metode der opdatere et teams hold navn
     */
    @Override
    public Team changeTeamInfo(Team currentTeam, Team changingTeam) throws Exception {
        try {

            if (currentTeam.getTeamID() == 0){
                System.out.println("ERROR: Intet holdID på currentTeam");
                throw new SQLException();
            }

            PreparedStatement changeTeam = connection.prepareStatement("UPDATE teams SET TeamName = ? WHERE TeamID = ?");
            changeTeam.setString(1, changingTeam.getTeamName());
            changeTeam.setInt(2, currentTeam.getTeamID());

            changeTeam.executeUpdate();

            return changingTeam;

        } catch (SQLException err){
            err.printStackTrace();
            return changingTeam;
        }
    }

    /**
     * Metode der opdatere firma navn og hvis det eksisterer vil der blive retuneret null
     */
    @Override
    public Firm changeFirmInfo(Firm currentFirm, Firm changingFirm) throws Exception {
        try {
            PreparedStatement changeFirm = connection.prepareStatement("UPDATE firms SET FirmName = ? WHERE FirmName = ?");
            changeFirm.setString(1, changingFirm.getFirmName());
            changeFirm.setString(2, currentFirm.getFirmName());

            changeFirm.executeUpdate();
            return changingFirm;
        } catch (SQLException err){
            /**
             * Hvis error er pga man prøver at opdatere en primary key af værdi som allerede eksistere skal den bare
             * returnere null uden at printe stacktrace
             */
            if (err instanceof SQLIntegrityConstraintViolationException){
                System.out.println("Err: Duplicate entry");
                return null;
            }
            err.printStackTrace();
            return null;
        }
    }

    /**
     * Metode der fjerne deltager fra hold
     */
    @Override
    public String removeFromTeam(Participant participant) throws Exception {
        try {
            PreparedStatement remove = connection.prepareStatement("UPDATE persons SET teamID = null WHERE Email = ?");
            remove.setString(1, participant.getEmail());
            remove.executeUpdate();
            return "Personen er blevet fjernet fra holdet";
        }catch (SQLException err){
            err.printStackTrace();
            return "Kunne ikke fjerne person fra hold";
        }
    }

    /**
     * Metode der hente en personens FirmID og matcher dette med en firmName udfra personens Email.
     * Hvis personen ikke har noget firma, vil der blive returneret at personen ikke har noget Firma
     */
    @Override
    public Firm getFirmFromEmail(String email) throws Exception {
        try{
            PreparedStatement getFirm = connection.prepareStatement("SELECT * from firms INNER JOIN persons ON firms.FirmID = persons.FirmID WHERE persons.Email = ?");
            getFirm.setString(1, email);
            ResultSet getFirmRes = getFirm.executeQuery();
            if (getFirmRes.next()){
                Firm firm = new Firm();
                firm.setID(getFirmRes.getInt("FirmID"));
                firm.setFirmName(getFirmRes.getString("FirmName"));
                return firm;
            } else {
                System.out.println("Personen har ikke noget firma");
                throw new SQLException();
            }

        } catch (SQLException err){
            err.printStackTrace();
            return null;
        }
    }

    /**
     * Metode der hente en personens TeamID og matcher dette med en TeamName udfra personens Email.
     * Hvis personen ikke har noget team, vil der blive returneret at personen ikke har noget Team
     */
    @Override
    public Team getTeamFromEmail(String email) throws Exception {
        try {
            PreparedStatement getTeam = connection.prepareStatement("SELECT * FROM teams INNER JOIN persons ON teams.TeamID = persons.TeamID WHERE Email = ?");
            getTeam.setString(1, email);
            ResultSet getTeamRes = getTeam.executeQuery();
            if (getTeamRes.next()){
                Team foundTeam = new Team();
                foundTeam.setTeamID(getTeamRes.getInt("TeamID"));
                foundTeam.setTeamName(getTeamRes.getString("TeamName"));
                foundTeam.setFirmID(getTeamRes.getInt("FirmID"));

                return foundTeam;
            } else {
                System.out.println("PERSONEN ER IKKE FORBUNDET TIL NOGET HOLD");
                throw new SQLException();
            }
        } catch (SQLException err){
            err.printStackTrace();
            return null;
        }
    }

    /**
     * Metode der henter FirmaID udfra et givet firma navn
     */
    @Override
    public int getFirmIDFromFirmName(String firmName) throws Exception {
        try {
            PreparedStatement getID = connection.prepareStatement("SELECT FirmID FROM firms WHERE FirmName = ?");
            getID.setString(1, firmName);
            ResultSet getIDRes = getID.executeQuery();
            if (getIDRes.next()){
                return getIDRes.getInt("FirmID");
            } else {
                System.out.println("ERR: Intet match");
                throw new SQLException();
            }

        } catch (SQLException err){
            err.printStackTrace();
            return 0;
        }
    }

    /**
     * Metode der gemmer alle TeamCaptajner i en arrayliste ved at hente alle personer som er kaptajn
     */
    @Override
    public ArrayList<Participant> getAllTeamCaptains() throws Exception {

        ArrayList<Participant> teamCaptains = new ArrayList<>();

        try {
            PreparedStatement getAll = connection.prepareStatement("SELECT * FROM persons WHERE PersonType = 'TEAMCAPTAIN'");
            ResultSet getAllRes = getAll.executeQuery();
            while (getAllRes.next()){
                Participant tempParticipant = new Participant();
                tempParticipant.setName(getAllRes.getString("PersonName"));
                tempParticipant.setEmail(getAllRes.getString("Email"));
                tempParticipant.setPersonType(getAllRes.getString("PersonType"));
                tempParticipant.setCyclistType(getAllRes.getString("CyclistType"));
                tempParticipant.setFirmID(getAllRes.getInt("FirmID"));
                tempParticipant.setTeamID(getAllRes.getInt("TeamID"));

                teamCaptains.add(tempParticipant);
            }
            return teamCaptains;

        } catch (SQLException err){
            err.printStackTrace();
            return null;
        }
    }

    /**
     * Returnere et teams ID ved at finde en team der matcher i teamnavn og firma ID
     */
    @Override
    public int getTeamIDFromTeamNameAndFirmID(String teamName, int firmID) throws Exception {
        try {
            PreparedStatement getID = connection.prepareStatement("SELECT TeamID FROM teams WHERE FirmID = ? AND TeamName = ?");
            getID.setInt(1, firmID);
            getID.setString(2, teamName);

            ResultSet getIDRes = getID.executeQuery();
            if (getIDRes.next()){
                return getIDRes.getInt("TeamID");
            } else {
                System.out.println("ERR: intet match");
                throw new SQLException();
            }
        } catch (SQLException err){
            err.printStackTrace();
            return 0;
        }
    }

    /**
     * Metode der sletter et firma fra databasen
     * Når firmaet bliver slettet, bliver alle deltagere og teams også slettet
     */
    @Override
    public String deleteFirm(int firmID) throws Exception {
        try {
            //Sletter personer
            PreparedStatement changePart = connection.prepareStatement("DELETE FROM persons WHERE FirmID = ?");
            changePart.setInt(1, firmID);
            changePart.executeUpdate();

            //Sletter Teams
            PreparedStatement changeTeam = connection.prepareStatement("DELETE FROM teams WHERE FirmID = ?");
            changeTeam.setInt(1, firmID);
            changeTeam.executeUpdate();

            //Sletter firmaer
            PreparedStatement delete = connection.prepareStatement("DELETE FROM firms WHERE FirmID = ?");
            delete.setInt(1, firmID);
            delete.executeUpdate();

            return "Firmaet er blevet slettet";
        } catch (SQLException err){
            err.printStackTrace();
            return "Kunne ikke slette firmaet";
        }
    }

    /**
     * Metode der sletter teams udfra et givet teamID
     * Når et team bliver slettet, bliver alle deltager på teamet fjernet, altså deres TeamID = NULL
     */
    @Override
    public String deleteTeam(int teamID) throws Exception {
        try {
            //Fjerner deltagere
            PreparedStatement changePart = connection.prepareStatement("UPDATE persons SET TeamID = NULL WHERE TeamID = ?");
            changePart.setInt(1, teamID);
            changePart.executeUpdate();

            //Sletter Team
            PreparedStatement delete = connection.prepareStatement("DELETE FROM teams WHERE TeamID = ?");
            delete.setInt(1, teamID);
            delete.executeUpdate();
            return "Holdet er blevet slettet";
        } catch (SQLException err){
            err.printStackTrace();
            return "Holdet kunne ikke slettes";
        }
    }

    /**
     * Metode der sletter en deltager udfra hans email
     */
    @Override
    public String deleteParticipant(String email) throws Exception {
        try {

            //tjekker om han er team kaptajn
            setAnotherTeamCaptainIfTeamCaptain(email);
            // Sletter personen
            PreparedStatement delete = connection.prepareStatement("DELETE FROM persons WHERE Email = ?");
            delete.setString(1, email);
            delete.executeUpdate();


        } catch (SQLException err){
            err.printStackTrace();
            return "Personen kunne ikke slettes";
        }
        return "Personen er blevet slettet";
    }



        /**
         * Inden vi begynder at slette denne participant, skal vi tjekke om han er en teamcaptain.
         * Hvis personen er en TeamCaptain skal en ny blive teamcaptain.
         * Hvis der er ikke er andre personer i holdet, bliver holdet slettet.
         **/
    private void setAnotherTeamCaptainIfTeamCaptain(String email) throws SQLException{
        PreparedStatement find = connection.prepareStatement("SELECT PersonType, TeamID FROM persons WHERE Email = ?");
        find.setString(1, email);
        ResultSet findRes = find.executeQuery();
        if (findRes.next()){
            if (findRes.getString("PersonType").equalsIgnoreCase("TEAMCAPTAIN")){
                PreparedStatement find2 = connection.prepareStatement("SELECT Email FROM persons WHERE TeamID = ? AND Email <> ?");
                find2.setInt(1, findRes.getInt("TeamID"));
                find2.setString(2, email);
                ResultSet find2Res = find2.executeQuery();

                // Sletter personens team for at frigøre afhængigheden i databasen
                PreparedStatement delete = connection.prepareStatement("UPDATE persons SET TeamID = NULL WHERE Email = ?");
                delete.setString(1, email);
                delete.executeUpdate();
                if (find2Res.next()){
                    PreparedStatement newTeamCaptain = connection.prepareStatement("UPDATE persons SET PersonType = 'TEAMCAPTAIN' WHERE Email = ?");
                    newTeamCaptain.setString(1, find2Res.getString("Email"));
                    newTeamCaptain.executeUpdate();
                } else {
                    PreparedStatement deleteTeam = connection.prepareStatement("DELETE FROM teams WHERE TeamID = ?");
                    deleteTeam.setInt(1, findRes.getInt("TeamID"));
                    deleteTeam.executeUpdate();
                    System.out.println("Sletter hold");
                }
            }
        } else {
            System.out.println("ERR Person har ingen persontype");
            throw new SQLException();
        }

    }

    /**
     * Metode hvor en deltagers cyklisttype bliver opdateret
     */
    @Override
    public String changePersonType(String email, String personType) throws Exception {
        try {
            PreparedStatement change = connection.prepareStatement("UPDATE persons SET PersonType = ? WHERE Email = ?");
            change.setString(1, personType);
            change.setString(2, email);
            change.executeUpdate();
            return "Personen er ændret";
        } catch (SQLException err){
            err.printStackTrace();
            return "Personen kunne ikke ændres";
        }
    }
}
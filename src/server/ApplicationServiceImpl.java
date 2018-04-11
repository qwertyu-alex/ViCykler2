package server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.SQLError;
import com.sun.org.apache.regexp.internal.REUtil;
import rpc.ApplicationService;
import shared.DTO.*;


import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.regex.MatchResult;

/**
 * DAO
 */
public class ApplicationServiceImpl extends RemoteServiceServlet implements ApplicationService {

    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/vicykler";
    private final String USERNAME = "dummy";
    private final String PASSWORD = "Meme_1234";
    //Meme_1234
    private Connection connection;

    public ApplicationServiceImpl(){
        try {
             connection = (Connection) DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            System.out.println("Successful");
        } catch (SQLException err){
            err.printStackTrace();
            //https://stackoverflow.com/questions/2434592/difference-in-system-exit0-system-exit-1-system-exit1-in-java
            System.exit(1);
        }
    }

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

                    if (resultSet.getString("PersonType").equalsIgnoreCase("PARTICIPANT")){
                        foundParticipant.setPersonType("PARTICIPANT");
                    } else if (resultSet.getString("PersonType").equalsIgnoreCase("TEAMCAPTAIN")){
                        foundParticipant.setPersonType("TEAMCAPTAIN");
                    }

                    foundPerson = foundParticipant;
                } else if (resultSet.getString("PersonType").equalsIgnoreCase("ADMIN")){
                    foundPerson = new Admin();
                }
            }


        } catch (SQLException err){
            err.printStackTrace();
        }
        return foundPerson;
    }

    /***
     * @return The names of every person in the database
     * @throws Exception
     */
    @Override
    public String returnPersons() throws Exception {

        ArrayList<String> personNames = new ArrayList<>();

        PreparedStatement findPerson = connection.prepareStatement("SELECT PersonName FROM persons");
        ResultSet resultSet = findPerson.executeQuery();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

        while(resultSet.next()){
            personNames.add(resultSet.getString(1));
        }

        System.out.println(String.join(", ", personNames));
        return String.join(", ", personNames);
    }

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

    @Override
    public ArrayList<Participant> getAllParticipants() throws Exception {

        ArrayList<Participant> participants = new ArrayList<>();
        Participant participant;
        ResultSet resultSet = null;

        try {
            PreparedStatement findPersons = connection.prepareStatement("" +
                    "SELECT * FROM persons INNER JOIN teams ON persons.TeamID = teams.TeamID INNER JOIN firms ON persons.FirmID = firms.FirmID");
            resultSet = findPersons.executeQuery();

            while(resultSet.next()){
                if(!resultSet.getString("PersonType").equalsIgnoreCase("ADMIN")){
                    participant = new Participant();

                    participant.setName(resultSet.getString("PersonName").toLowerCase());
                    participant.setEmail(resultSet.getString("Email").toLowerCase());
                    participant.setCyclistType(resultSet.getString("CyclistType").toLowerCase());
                    participant.setPersonType(resultSet.getString("PersonType"));
                    participant.setFirmName(resultSet.getString("FirmName"));
                    participant.setFirmID(resultSet.getInt("FirmID"));
                    participant.setTeamID(resultSet.getInt("TeamID"));
                    participant.setTeamName(resultSet.getString("TeamName"));

                    System.out.println("Part firm ID: " +  participant.getFirmID());

                    participants.add(participant);
                }
            }
        } catch (SQLException err){
            err.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception err){
                err.printStackTrace();
            }
        }

        return participants;
    }

    @Override
    public boolean createParticipant(Participant newParticipant) throws Exception {
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
             * Først skal der tjekkes om emailen allerede eksisterer. Hvis den gør returnerer metoden false.
             */
            PreparedStatement emailsPreparedStatement = connection.prepareStatement("SELECT Email FROM persons WHERE Email LIKE ?");
            emailsPreparedStatement.setString(1, Character.toString(email.charAt(0)) + "%");
            emailsResultSet = emailsPreparedStatement.executeQuery();

            while(emailsResultSet.next()){
                emailList.add(emailsResultSet.getString("Email"));
            }

            for (String emailName: emailList) {
                if (emailName.equalsIgnoreCase(email)){
                    return false;
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
        return true;
    }

    @Override
    public boolean createTeam(Team newTeam, Participant teamCaptain) throws Exception {
        int firmID;
        newTeam.getParticipants().add(teamCaptain.getEmail());

        System.out.println("Kører createTeam");
        try {

            /**
             * Finder firmID ud fra en participant som senere bliver holdkaptain
             */
            PreparedStatement getFirmID = connection.prepareStatement("SELECT FirmID FROM persons WHERE EMAIL = ?");
            getFirmID.setString(1, teamCaptain.getEmail());
            ResultSet getFirmIDRes = getFirmID.executeQuery();
            if (getFirmIDRes.next()){
                firmID = getFirmIDRes.getInt("FirmID");
            } else {
                System.out.println("Kaster exception fra createTeam");
                throw new Exception();
            }

            /**
             * Indsætter det nye hold i databasen
             */
            PreparedStatement createTeam = connection.prepareStatement("INSERT INTO teams(TeamName,FirmID) VALUES (?,?)");
            createTeam.setString(1, newTeam.getTeamName());
            createTeam.setInt(2, firmID);
            createTeam.executeUpdate();

            /**
             * Find holdID
             */

            PreparedStatement findID = connection.prepareStatement("SELECT TeamID from teams WHERE TeamName = ? AND FirmID = ?");
            findID.setString(1,newTeam.getTeamName());
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
            findParticipant.setString(2, teamCaptain.getEmail());
            findParticipant.executeUpdate();

        } catch (SQLException err){
            err.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean addParticipantsToTeam(Team currentTeam, ArrayList<String> participantEmails){
        /**
         * Opdater participants som skal være i holdet, inklusiv teamCaptain,
         * så deres teamID matcher det nye team.
         */
        try {
            System.out.println("Størrelse på alle deltagere " + participantEmails.size());
            if (participantEmails.size() > 0){
                for (String participantEmail : participantEmails) {
                    /**
                     * Updater alle personer i ArrayListen så de har samme TeamID som holdet
                     */
                        PreparedStatement changeParticipant = connection.prepareStatement("UPDATE persons SET TeamID = ? WHERE Email = ?");
                        changeParticipant.setInt(1,currentTeam.getTeamID());
                        changeParticipant.setString(2, participantEmail);
                        changeParticipant.executeUpdate();
                }
            }
        } catch (SQLException err){
            err.printStackTrace();
            return false;
        }

        return true;
    }

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
    };

    @Override
    public String getParticipantName(String email) throws Exception {

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT PersonName FROM persons WHERE Email LIKE ?");
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();

        return resultSet.getString("PersonName");
    }

    @Override
    public String getParticipantCyclistType(String email) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT CyclistType FROM persons WHERE Email LIKE ?");
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();

        return resultSet.getString("CyclistType");
    }

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

    @Override
    public String getParticipantPassword(String email) throws Exception {
        PreparedStatement getPassword = connection.prepareStatement("SELECT Password FROM persons WHERE Email = ?");
        getPassword.setString(1, email);
        ResultSet resultSet = getPassword.executeQuery();
        resultSet.next();
        return resultSet.getString("Password");
    }

    @Override
    public String getGuestStatisticView() throws Exception {

        PreparedStatement statisticForGuest = connection.prepareStatement("SELECT persons.PersonName, persons.FirmName, teams.TeamName " +
                "FROM persons INNER JOIN teams ON teams.TeamID = persons.TeamID");
        return null;
    }

    @Override
    public ArrayList<Team> getAllTeams() throws Exception {
        ArrayList<Team> teams = new ArrayList<>();

        try{
            PreparedStatement getTeams = connection.prepareStatement("SELECT * FROM teams INNER JOIN firms ON firms.FirmID = teams.FirmID");
            ResultSet getTeamsRes = getTeams.executeQuery();

            while (getTeamsRes.next()){
                Team tempTeam = new Team();
                tempTeam.setTeamID(getTeamsRes.getInt("TeamID"));
                tempTeam.setTeamName(getTeamsRes.getString("TeamName"));
                tempTeam.setFirmName(getTeamsRes.getString("FirmName"));
                tempTeam.setFirmID(getTeamsRes.getInt("FirmID"));
                System.out.println("Team: firmID" + tempTeam.getFirmID());
                teams.add(tempTeam);
            }

            for (Team team: teams) {
                PreparedStatement getParticipants = connection.prepareStatement("SELECT persons.email FROM persons INNER JOIN teams ON persons.TeamID = teams.TeamID WHERE teams.TeamID = ?");
                getParticipants.setInt(1, team.getTeamID());

                ResultSet participantsRes = getParticipants.executeQuery();

                while(participantsRes.next()){
                    team.getParticipants().add(participantsRes.getString("Email"));
                }
            }
            return teams;
        } catch (Exception err){
            err.printStackTrace();
        }
         return teams;
    }

    @Override
    public ArrayList<Firm> getAllFirms() throws Exception {

        ArrayList<Firm> firms = new ArrayList<>();

        try{
            PreparedStatement getFirms = connection.prepareStatement("SELECT * FROM firms");
            ResultSet getFirmsRes = getFirms.executeQuery();


//            System.out.println("Kører Firmanavn");
            while (getFirmsRes.next()){
                Firm tempFirm = new Firm();
                tempFirm.setFirmName(getFirmsRes.getString("FirmName"));
                tempFirm.setID(getFirmsRes.getInt("FirmID"));
                firms.add(tempFirm);
            }

            getFirms.close();
            getFirmsRes.close();

//            System.out.println("Kører hold");
            for (Firm firm : firms) {
                PreparedStatement getTeams = connection.prepareStatement("SELECT teams.TeamID FROM teams INNER JOIN firms ON firms.FirmID = teams.FirmID WHERE firms.FirmName = ?");
                getTeams.setString(1, firm.getFirmName());
                ResultSet getTeamsRes = getTeams.executeQuery();

//                System.out.println("Kører while i hold");

                while (getTeamsRes.next()){
//                    System.out.println(getTeamsRes.getInt("TeamID"));
//                    System.out.println("Tilføjer holdet til firm");
                    firm.getTeams().add((Integer)getTeamsRes.getInt("TeamID"));
                    firm.getTeams().get(0);
                }

//                System.out.println("Lukker lortet i hold");

                getTeams.close();
                getTeamsRes.close();
            }

//            System.out.println("Kører deltagere");
            for (Firm firm : firms){
                PreparedStatement getParticipants = connection.prepareStatement("SELECT persons.Email FROM persons INNER JOIN firms ON persons.FirmID = firms.FirmID WHERE firms.FirmName = ?");
                getParticipants.setString(1, firm.getFirmName());
                ResultSet getParticipantsRes = getParticipants.executeQuery();

                int i = 0;

//                System.out.println("Kører while i deltageere");
                while (getParticipantsRes.next()){
                    firm.getParticipants().add(getParticipantsRes.getString("Email"));
                    i++;
//                    System.out.println("Personer: " + i);
//                    System.out.println(getParticipantsRes.getFetchSize());
                }
                getParticipants.close();
                getParticipantsRes.close();
            }

//            System.out.println("Returnere lortet");
            return firms;

        } catch (SQLException err){
            err.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean createFirm(String name) {
        return false;
    }

    @Override
    public Participant changeParticipantInfo(Participant currentParticipant, Participant changingParticipant) throws Exception {

        try {
            PreparedStatement getTeamID = connection.prepareStatement("SELECT TeamID FROM teams WHERE teams.TeamName LIKE ?");
            PreparedStatement getFirmID = connection.prepareStatement("SELECT FirmID FROM firms WHERE firms.FirmName LIKE ?");

            getTeamID.setString(1, changingParticipant.getTeamName());
            getFirmID.setString(1, changingParticipant.getFirmName());

            ResultSet getTeamIDRes = getTeamID.executeQuery();
            ResultSet getFirmIDRes = getFirmID.executeQuery();

            getTeamIDRes.next();
            getFirmIDRes.next();

            int teamID = getTeamIDRes.getInt("TeamID");
            int firmID = getFirmIDRes.getInt("FirmID");


            PreparedStatement updateParticipant = connection.prepareStatement(
                    "UPDATE persons SET PersonName = ?, Email = ?,  " +
                            "Password = ?, PersonType = ?, CyclistType = ?, FirmID = ?, TeamID = ? WHERE Email = ?");

            updateParticipant.setString(1,changingParticipant.getName());
            updateParticipant.setString(2,changingParticipant.getEmail());
            updateParticipant.setString(3,changingParticipant.getPassword());
            updateParticipant.setString(4,changingParticipant.getPersonType());
            updateParticipant.setString(5,changingParticipant.getCyclistType());
            updateParticipant.setInt(6,firmID);
            updateParticipant.setInt(7,teamID);
            updateParticipant.setString(8, currentParticipant.getEmail());

            updateParticipant.executeUpdate();
            return changingParticipant;
        } catch (SQLException err){
            err.printStackTrace();
            return changingParticipant;
        }
    }

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

    @Override
    public boolean removeFromTeam(Participant participant) throws Exception {

        try {
            PreparedStatement remove = connection.prepareStatement("UPDATE persons SET teamID = null WHERE Email = ?");
            remove.setString(1, participant.getEmail());
            remove.executeUpdate();
        }catch (SQLException err){
            err.printStackTrace();
            return false;
        }

        return true;
    }

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

        }catch (SQLException err){
            err.printStackTrace();
        }


        return null;
    }

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

        }catch (SQLException err){
            err.printStackTrace();
        }

        return null;
    }

    @Override
    public Team getTeamFromTeamID(int teamID) throws Exception {
        try {
            PreparedStatement getTeam = connection.prepareStatement("SELECT * FROM teams WHERE TeamID = ?");
            getTeam.setInt(1, teamID);
            ResultSet getTeamRes = getTeam.executeQuery();
            if (getTeamRes.next()){
                Team foundTeam = new Team();
                foundTeam.setTeamID(getTeamRes.getInt("TeamID"));
                foundTeam.setFirmID(getTeamRes.getInt("FirmID"));
                foundTeam.setTeamName(getTeamRes.getString("TeamName"));
                return foundTeam;
            } else  {
                System.out.println("ERR intet hold fra ID");
                throw new SQLException();
            }

        } catch (SQLException err){

        }

        return null;
    }
}
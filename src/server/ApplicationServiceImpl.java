package server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mysql.jdbc.Connection;
import rpc.ApplicationService;
import shared.DTO.Participant;
import shared.DTO.Person;


import java.sql.*;
import java.util.ArrayList;
import java.util.regex.MatchResult;

public class ApplicationServiceImpl extends RemoteServiceServlet implements ApplicationService {

    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/vicykler";
    private final String USERNAME = "dummy";
    private final String PASSWORD = "Meme_123";
    private Connection connection;

    public ApplicationServiceImpl(){
        try {
             connection = (Connection) DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        } catch (SQLException err){
            err.printStackTrace();
            //https://stackoverflow.com/questions/2434592/difference-in-system-exit0-system-exit-1-system-exit1-in-java
            System.exit(1);
        }
    }

    @Override
    public Person authorizePerson(String email, String password) throws Exception {
        Participant foundPerson = null;

        PreparedStatement findMatch = connection.prepareStatement("SELECT * FROM persons WHERE Email LIKE ? AND Password LIKE ?");
        findMatch.setString(1, email);
        findMatch.setString(2, password);

        try {
            ResultSet resultSet = findMatch.executeQuery();

            if (resultSet.next()){
                if (resultSet.getString("PersonType").equalsIgnoreCase("PARTICIPANT")){
                    Participant foundParticipant = new Participant();
                    foundParticipant.setName(resultSet.getString("PersonName"));
                    foundParticipant.setEmail(resultSet.getString("Email"));
                    foundParticipant.setCyclistType(resultSet.getString("CyclistType"));
                    foundParticipant.setTeamID(resultSet.getString("TeamID"));
                    foundParticipant.setFirmName(resultSet.getString("FirmName"));

                    foundPerson = foundParticipant;
                }
            }


        } catch (SQLException err){
            err.printStackTrace();
        }







        return foundPerson;
    }

    /***
     *
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
    public ArrayList<Participant> getAllParticipants() throws Exception {

        System.out.println("Running: getAllPersons()");

        ArrayList<Participant> participants = new ArrayList<>();
        int numberOfColums;
        Participant participant;
        ResultSet resultSet = null;

        try {
            PreparedStatement findPersons = connection.prepareStatement("SELECT * FROM persons");
            resultSet = findPersons.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            numberOfColums = metaData.getColumnCount();

            System.out.println("Number of colums: " + numberOfColums);

            while(resultSet.next()){
                if(resultSet.getString("PersonType").equalsIgnoreCase("PARTICIPANT")){
                    participant = new Participant();

                    participant.setName(resultSet.getString("PersonName").toLowerCase());
                    participant.setEmail(resultSet.getString("Email").toLowerCase());
                    participant.setCyclistType(resultSet.getString("CyclistType").toLowerCase());

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
    public boolean createParticipant(String email, String name, String cyclistType, String password) throws Exception {
        ArrayList<String> emailList = new ArrayList<>();
        ResultSet emailsResultSet = null;

        try {

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

            PreparedStatement createParticipant = connection.prepareStatement("INSERT INTO persons(Email, PersonName, CyclistType, Password, PersonType) VALUES (?,?,?,?, 'PARTICIPANT')");
            createParticipant.setString(1, email);
            createParticipant.setString(2, name);
            createParticipant.setString(3, cyclistType);
            createParticipant.setString(4, password);

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

    private Person findPerson() {
        return null;
    }

}
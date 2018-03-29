package server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mysql.jdbc.Connection;
import rpc.ApplicationService;
import shared.DTO.Participant;
import shared.DTO.Person;


import java.sql.*;
import java.util.ArrayList;

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
    public boolean authorizePerson(String username, String password) throws Exception {
        return true;
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

                    participant.setName(resultSet.getString("PersonName"));
                    participant.setEmail(resultSet.getString("Email"));
                    participant.setCyklistType(resultSet.getString("CyclistType"));

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

}
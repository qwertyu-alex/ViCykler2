//Mikkel Storm

package server.withoutDB;

import shared.DTO.*;

import java.util.ArrayList;

public class Data {
    private ArrayList<Firm> firms = new ArrayList<>();
    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<Person> persons = new ArrayList<>();
    private ArrayList<Participant> participants = new ArrayList<>();
    private ArrayList<TeamCaptain> teamCaptains = new ArrayList<>();
    private ArrayList<Admin> admins = new ArrayList<>();

//    private ArrayList<TestDude> testDudes = new ArrayList<>();


    //Constructor
    public Data() {
        generateData();
//    generateData2();

    }

    /**
     * Genererer alle persons, firms og teams
     */

    /** test
     *
     */
//    public void generateData2(){
//       TestDude testDude = new TestDude("hej", "meme");
//
//       testDudes.add(testDude);
//    }

    public void generateData() {
        //Generer admin
        Admin testAdmin = generateAdmin("Admin1", "support@vicykler.dk", "1234");

        //Generer deltager
        Participant testParticipant = generateParticipant("Brian Larsen", "Brian@gmail.com","4321", "Master");

        //Firma: Arla
        Firm arla = generateFirm("Arla");

        //Folk fra hold: Mælkekartonerne
        Team team1 = generateTeam("Mælkekarton", arla);
        generateTeamCaptain("Flemming Sørensen", "Flemming.S@gmail.com","4321", "Master").setTeam(team1);
        generateParticipant("Søren Henriksen", "Søren.H@gmail.com","4321", "Master").setTeam(team1);
        generateParticipant("Tina Eriksen", "Tina.E@gmail.com","4321", "Master").setTeam(team1);
        generateParticipant("Muhammed Ali", "Muhammed.A@gmail.com","4321", "Master").setTeam(team1);
        generateParticipant("Torben Torbensen", "Torben.T@gmail.com","4321",  "Master").setTeam(team1);

        //Folk fra hold: Førsteholdet
        Team team2 = generateTeam("Førsteholdet", arla);
        generateTeamCaptain("Birthe Kjær", "Birthe.K@gmail.com", "9876", "Master").setTeam(team2);
        generateParticipant("Richard Ragnvald", "Richard.R@gmail.com","9876", "Master").setTeam(team2);
        generateParticipant("Peter Belli", "Ulven@gmail.com","9876", "Master").setTeam(team2);


        //Firma: Postnord
        Firm postnord = generateFirm("Postnord");

        //Folk fra hold: Cykelbudende
        Team team3 = generateTeam("Cykelbudene", postnord);
        generateTeamCaptain("Per Person", "Per.P@gmail.com","6789", "Master").setTeam(team3);
        generateParticipant("Tim Flemmingsen", "Tim.F@gmail.com","6789", "Master").setTeam(team3);
        generateParticipant("Simon Thiesen", "Simon.T@gmail.com","6789", "Master").setTeam(team3);
        generateParticipant("Carsten Madsen", "Carsten.M@gmail.com","6789", "Master").setTeam(team3);
        generateParticipant("Jonas Johansen", "Jonas.J@gmail.com","6789",  "Master").setTeam(team3);

    }

    /**
     *Genererer en admin og lægger den i en liste over admins som en attribut i dataobjektet
     */
    public Admin generateAdmin(String name, String email, String password){
        Admin newAdmin = new Admin(name, email, password);
        admins.add(newAdmin);
        persons.add(newAdmin);
        return newAdmin;
    }
    /**
     *Genererer en teamcaptain og lægger den i en liste over teamcaptains som en attribut i dataobjektet
     */
    public TeamCaptain generateTeamCaptain(String name, String email, String password, String cyclistType){
        TeamCaptain newTeamCaptain = new TeamCaptain(name, email, password, cyclistType);
        teamCaptains.add(newTeamCaptain);
        persons.add(newTeamCaptain);
        return newTeamCaptain;
    }
    /**
     *Genererer en Participant og lægger den i en liste over Participant som en attribut i dataobjektet
     */
    public Participant generateParticipant(String name, String email, String password, String cyclistType){
        Participant newParticipant = new Participant(name, email, password, cyclistType);
        participants.add(newParticipant);
        persons.add(newParticipant);
        return newParticipant;
    }
    /**
     *Genererer en Team og lægger den i en liste over Team som en attribut i dataobjektet
     */
    public Team generateTeam(String teamName, Firm firm){
        Team newTeam = new Team(teamName, firm);
        teams.add(newTeam);
        return newTeam;
    }
    /**
     *Genererer en Firm og lægger den i en liste over Firm som en attribut i dataobjektet
     */
    public Firm generateFirm(String firmName){
        Firm newFirm = new Firm(firmName);
        firms.add(newFirm);
        return newFirm;
    }

    public ArrayList<Firm> getFirms() {
        return firms;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public ArrayList<TeamCaptain> getTeamCaptains() {
        return teamCaptains;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public ArrayList<Admin> getAdmins() {
        return admins;
    }
}




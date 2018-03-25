package shared.DTO;//Alexander Van Le && Oliver Lange

import java.util.ArrayList;

public class Firm {
    private String firmName;

    private ArrayList<Participant> participants = new ArrayList<>();
    private ArrayList<Team> teamList = new ArrayList<>();

    //Contructor
    public Firm(String firmName) {

        //Variable definitioner
        this.firmName = firmName;
    }

    //Addere
    public void addTeam (Team team){
        teamList.add(team);
    }

    //Setter
    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    //Getter
    public String getFirmName() {
        return firmName;
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public ArrayList<Team> getTeamList() {
        return teamList;
    }

}

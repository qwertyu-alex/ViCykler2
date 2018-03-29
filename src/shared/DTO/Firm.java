package shared.DTO;//Alexander Van Le && Oliver Lange

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

public class Firm implements IsSerializable{
    private String firmName;

    private ArrayList<Participant> participants = new ArrayList<>();
    private ArrayList<Team> teamList = new ArrayList<>();

    //Default Contructor
    public Firm(){

    }

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

package shared.DTO;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

public class Team implements IsSerializable{
    private String teamName;
    private String firmName;
    private int teamID;
    private int firmID;
    private ArrayList <String> participants = new ArrayList<>();



    //Default constructor
    public Team(){
        this.teamID = 0;
    }

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public int getFirmID() {
        return firmID;
    }

    public void setFirmID(int firmID) {
        this.firmID = firmID;
    }
}

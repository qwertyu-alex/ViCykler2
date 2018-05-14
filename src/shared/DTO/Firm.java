package shared.DTO;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

public class Firm implements IsSerializable{
    private String firmName;
    private int ID;
    private ArrayList<String> participants = new ArrayList<>();
    private ArrayList<Integer> teams = new ArrayList<>();

    //Default Contructor
    public Firm(){
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFirmName() {
        return firmName;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public ArrayList<Integer> getTeams() {
        return teams;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

}

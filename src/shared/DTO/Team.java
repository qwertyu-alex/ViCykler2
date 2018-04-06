package shared.DTO;//Alexander Van Le && Oliver Lange

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

public class Team implements IsSerializable{
    private String teamName;
    private Firm firm;

    private ArrayList <Participant> participants = new ArrayList<>();


    //Default constructor
    public Team(){
        this.teamName = "null";
        this.firm = null;
    }

    //Constructor
    public Team(String teamName, Firm firm){
        //variabler
        this.teamName = teamName;
        this.firm = firm;
        firm.addTeam(this);
    }

    //Adder metoder
    public void addParticipant(Participant participant){
        participants.add(participant);
    }

    //Getter metoder
    public String getTeamName(){
        return teamName;
    }

    public Firm getFirm() {
        return firm;
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    //Setter metoder
    public void setTeamName(String teamName){
        this.teamName = teamName;
    }

    //Remover
    public void removeParticipant(Participant participant){
        participants.remove(participant);
    }

}

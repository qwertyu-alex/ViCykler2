package shared;//Alexander Van Le && Oliver Lange

import java.util.ArrayList;

public class Team {
    private String teamName;
    private Firm firm;
    private TeamCaptain teamCaptain;

    private ArrayList <Participant> participants = new ArrayList<>();


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

    public TeamCaptain getTeamCaptain() {
        return teamCaptain;
    }

    //Setter metoder
    public void setTeamName(String teamName){
        this.teamName = teamName;
    }

    public void setTeamCaptain(TeamCaptain teamCaptain) {
        this.teamCaptain = teamCaptain;
        if (teamCaptain != null){
            if (teamCaptain.getTeam() != (this)){
                teamCaptain.setTeam(this);
            }
        }
    }

    //Remover
    public void removeParticipant(Participant participant){
        participants.remove(participant);
    }

}

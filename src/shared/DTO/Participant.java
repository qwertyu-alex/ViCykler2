package shared.DTO;//Alexander Van Le && Oliver Lange

/**
 * Kilde: Y. Daniel Liang (2015), s. 431
 */
public class Participant extends Person {
    //Variabler
    private String cyclistType;
    private Firm firm = null;
    private Team team = null; // arraylist


    //Constructor
    public Participant(String name, String email, String password, String cyclistType) {
        super(name, email, password);
        this.cyclistType = cyclistType;

    }

    //Overloaded contructor
    public Participant(Participant participant){
        super(participant.getName(), participant.getEmail(), participant.getPassword());
        this.cyclistType = participant.getCyclistType();
        //hvis der er forbundet et hold
        if (participant.getTeam() != null){
            this.team = participant.getTeam();
        }
        //hvis der er forbundet et firma
        if (participant.getFirm() != null){
            this.firm = participant.getFirm();
        }
    }

    //Getter
    public String getCyclistType() {
        return cyclistType;
    }

    public Team getTeam() {
        if (team == null){
            return null;
        }
        return team;
    }

    public Firm getFirm() {
        if (firm == null){
            return null;
        }
        return firm;
    }

    //Setter
    public void setCyklistType(String cyclistType) {
        this.cyclistType = cyclistType;
    }

    public void setTeam(Team team) {
        this.team = team;
        if (team != null){
            team.addParticipant(this);
            this.firm = team.getFirm();
        }

    }

    public void setFirm(Firm firm){
        this.firm = firm;
    }
}

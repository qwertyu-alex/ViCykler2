package shared.DTO;//Alexander Van Le && Oliver Lange

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Kilde: Y. Daniel Liang (2015), s. 431
 */
public class Participant extends Person implements IsSerializable {
    //Variabler
    private String cyclistType;
    private String firmName;
    private int teamID;
    private String personType;
    private String teamName;


    //Default constructor
    public Participant(){
        super("null", "null", "null");
        personType = "Participant";
    }

    //Getter
    public String getCyclistType() {
        return cyclistType;
    }

    //Setter
    public void setCyclistType(String cyclistType) {
        this.cyclistType = cyclistType;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public String getFirmName() {
        return firmName;
    }

    public int getTeamID() {
        return teamID;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}

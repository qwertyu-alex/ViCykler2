package shared.DTO;//Alexander Van Le && Oliver Lange

/**
 * Kilde: Y. Daniel Liang (2015), s. 431
 */
public class TeamCaptain extends Participant {
    //Constructor
    public TeamCaptain(Participant participant){
        super(participant);
    }

    public TeamCaptain(String name, String email, String password, String cyclistType){
        super(name, email, password, cyclistType);
    }

    /**
     * Overrider setTeam-metoden i participant klassen
     */
    @Override
    public void setTeam(Team team) {
        super.setTeam(team);
        if (team != null){
            if (team.getTeamCaptain() != this){
                team.setTeamCaptain(this);
            }
        }
    }
}

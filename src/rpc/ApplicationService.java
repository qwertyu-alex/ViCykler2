package rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sun.rmi.rmid.ExecPermission;
import shared.DTO.Firm;
import shared.DTO.Participant;
import shared.DTO.Person;
import shared.DTO.Team;

import java.util.ArrayList;

//Reference til web-inf
@RemoteServiceRelativePath("ViCyklerService")
public interface ApplicationService extends RemoteService {
    Person authorizePerson(String email, String password) throws Exception;
    String returnPersons() throws Exception;
    ArrayList<Participant> getAllParticipants() throws Exception;
    ArrayList<Team> getAllTeams() throws Exception;
    ArrayList<Firm> getAllFirms() throws Exception;
    boolean createParticipant(Participant newParticipant) throws Exception;
    boolean createTeam(Team newTeam, Participant teamCaptain) throws Exception;
    boolean createFirm(String name);
    String getParticipantName(String email) throws Exception;
    String getParticipantCyclistType(String email) throws Exception;
    String getParticipantFirmName(String email) throws Exception;
    String getParticipantTeamName(String email) throws Exception;
    String getParticipantPassword(String email) throws Exception;
    Team getTeam(String email) throws Exception;

    Participant changeParticipantInfo(Participant currentParticipant, Participant changingParticipant) throws Exception;
    Team changeTeamInfo(Team currentTeam, Team changingTeam) throws Exception;
    Firm changeFirmInfo(Firm currentFirm, Firm changingFirm) throws Exception;
    Participant getParticipant(String email) throws Exception;
    String getGuestStatisticView() throws Exception;
    boolean removeFromTeam(Participant participant) throws Exception;
}

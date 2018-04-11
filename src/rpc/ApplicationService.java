package rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import shared.DTO.Firm;
import shared.DTO.Participant;
import shared.DTO.Person;
import shared.DTO.Team;

import java.util.ArrayList;

//Reference til web-inf
//<url-pattern>/ViCykler/ViCyklerServicee</url-pattern>
@RemoteServiceRelativePath("ViCyklerServicee")
public interface ApplicationService extends RemoteService {
    Person authorizePerson(String email, String password) throws Exception;
    String returnPersons() throws Exception;
    ArrayList<Participant> getAllParticipants() throws Exception;
    ArrayList<Team> getAllTeams() throws Exception;
    ArrayList<Firm> getAllFirms() throws Exception;
    boolean createParticipant(Participant newParticipant) throws Exception;
    boolean createTeam(Team newTeam, Participant teamCaptain) throws Exception;
    boolean createFirm(String name);
    Participant getParticipant(String email) throws Exception;
    Team getTeam(String email) throws Exception;
    Team getTeam(Team team) throws Exception;
    String getParticipantName(String email) throws Exception;
    String getParticipantCyclistType(String email) throws Exception;
    String getParticipantFirmName(String email) throws Exception;
    String getParticipantTeamName(String email) throws Exception;
    String getParticipantPassword(String email) throws Exception;
    Participant changeParticipantInfo(Participant currentParticipant, Participant changingParticipant) throws Exception;
    Team changeTeamInfo(Team currentTeam, Team changingTeam) throws Exception;
    Firm changeFirmInfo(Firm currentFirm, Firm changingFirm) throws Exception;
    String getGuestStatisticView() throws Exception;
    boolean removeFromTeam(Participant participant) throws Exception;
    Firm getFirmFromEmail (String email) throws Exception;
    boolean addParticipantsToTeam(Team currentTeam, ArrayList<String> participantEmails) throws Exception;
    ArrayList<Participant> getAllParticipantsInTeamFromTeamID(int teamID) throws Exception;
}

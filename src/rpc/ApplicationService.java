package rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import jdk.nashorn.internal.runtime.ECMAException;
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
    ArrayList<Participant> getAllParticipantsAndTeamNameAndFirmName() throws Exception;
    ArrayList<Team> getAllTeams() throws Exception;
    ArrayList<Firm> getAllFirms() throws Exception;
    String createParticipant(Participant newParticipant) throws Exception;
    String createTeam(Team newTeam, Participant teamCaptain) throws Exception;
    String createFirm(String name);
    Participant getParticipant(String email) throws Exception;
    Team getTeamFromEmail(String email) throws Exception;
    Team getTeamFromTeamID(int teamID) throws Exception;
    String getParticipantName(String email) throws Exception;
    String getParticipantCyclistType(String email) throws Exception;
    String getParticipantFirmName(String email) throws Exception;
    String getParticipantTeamName(String email) throws Exception;
    String getParticipantPassword(String email) throws Exception;
    Participant changeParticipantInfo(Participant currentParticipant, Participant changingParticipant) throws Exception;
    Team changeTeamInfo(Team currentTeam, Team changingTeam) throws Exception;
    Firm changeFirmInfo(Firm currentFirm, Firm changingFirm) throws Exception;
    String getGuestStatisticView() throws Exception;
    String removeFromTeam(Participant participant) throws Exception;
    String deleteTeam(int teamID) throws Exception;
    String deleteFirm(int firmID) throws Exception;
    String deleteParticipant(String email) throws Exception;
    Firm getFirmFromEmail (String email) throws Exception;
    String addParticipantsToTeam(Team currentTeam, ArrayList<String> participantEmails) throws Exception;
    ArrayList<Participant> getAllTeamCaptains() throws Exception;
    ArrayList<Participant> getAllParticipantsInTeamFromTeamID(int teamID) throws Exception;
    int getFirmIDFromFirmName(String firmName) throws Exception;
    int getTeamIDFromTeamNameAndFirmID(String teamName, int firmID) throws Exception;
    String changePersonType(String email, String personType) throws Exception;
}

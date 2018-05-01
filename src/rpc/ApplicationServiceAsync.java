package rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import shared.DTO.Firm;
import shared.DTO.Participant;
import shared.DTO.Person;
import shared.DTO.Team;

import java.util.ArrayList;

public interface ApplicationServiceAsync {

    void authorizePerson(String email, String password, AsyncCallback<Person> async);

    void returnPersons(AsyncCallback<String> async);

    void getAllParticipantsAndTeamNameAndFirmName(AsyncCallback<ArrayList<Participant>> async);

    void createParticipant(Participant newParticipant, AsyncCallback<String> async);

    void createTeam(Team newTeam, Participant teamCaptain, AsyncCallback<String> async);

    void getParticipantCyclistType(String email, AsyncCallback<String> async);

    void getParticipantName(String email, AsyncCallback<String> async);

    void getParticipantFirmName(String email, AsyncCallback<String> async);

    void getParticipantTeamName(String email, AsyncCallback<String> async);

    void getGuestStatisticView(AsyncCallback<String> async);

    void getParticipantPassword(String email, AsyncCallback<String> async);

    void changeParticipantInfo(Participant currentParticipant, Participant changingParticipant, AsyncCallback<Participant> async);

    void getAllTeams(AsyncCallback<ArrayList<Team>> async);

    void changeTeamInfo(Team currentTeam, Team changingTeam, AsyncCallback<Team> async);

    void changeFirmInfo(Firm currentFirm, Firm changingFirm, AsyncCallback<Firm> async);

    void createFirm(String name, AsyncCallback<String> async);

    void getAllFirms(AsyncCallback<ArrayList<Firm>> async);

    void getParticipant(String email, AsyncCallback<Participant> async);

    void removeFromTeam(Participant participant,AsyncCallback<String> async);

    void getTeamFromEmail(String email, AsyncCallback<Team> async);

    void getTeamFromTeamID(int teamID, AsyncCallback<Team> async);

    void getFirmFromEmail(String email, AsyncCallback<Firm> async);

    void addParticipantsToTeam(Team currentTeam, ArrayList<String> participantEmails, AsyncCallback<String> async);

    void getAllParticipantsInTeamFromTeamID(int teamID, AsyncCallback<ArrayList<Participant>> async);

    void getFirmIDFromFirmName(String firmName, AsyncCallback<Integer> async);

    void getAllTeamCaptains(AsyncCallback<ArrayList<Participant>> async);

    void getTeamIDFromTeamNameAndFirmID(String teamName, int firmID, AsyncCallback<Integer> async);

    void deleteTeam(int teamID, AsyncCallback<String> async);

    void deleteFirm(int firmID, AsyncCallback<String> async);

    void deleteParticipant(String email, AsyncCallback<String> async);

    void changePersonType(String email, String personType, AsyncCallback<String> async);
}

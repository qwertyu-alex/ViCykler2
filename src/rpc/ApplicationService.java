package rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import shared.DTO.Participant;
import shared.DTO.Person;

import java.util.ArrayList;

//Reference til web-inf
@RemoteServiceRelativePath("ViCyklerService")
public interface ApplicationService extends RemoteService {
    Person authorizePerson(String email, String password) throws Exception;
    String returnPersons() throws Exception;
    ArrayList<Participant> getAllParticipants() throws Exception;
    boolean createParticipant(String email, String name, String cyclistType, String password) throws Exception;
    boolean createTeam(String name, Participant teamCaptain) throws Exception;
    String getParticipantName(String email) throws Exception;
    String getParticipantCyclistType(String email) throws Exception;
    String getParticipantFirmName(String email) throws Exception;
    String getParticipantTeamName(String email) throws Exception;
}

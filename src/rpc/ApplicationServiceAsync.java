package rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import shared.DTO.Participant;
import shared.DTO.Person;

import java.util.ArrayList;

public interface ApplicationServiceAsync {

    void authorizePerson(String email, String password, AsyncCallback<Person> async);

    void returnPersons(AsyncCallback<String> async);

    void getAllParticipants(AsyncCallback<ArrayList<Participant>> async);

    void createParticipant(String email, String name, String cyclistType, String password, AsyncCallback<Boolean> async);

    void createTeam(String name, Participant teamCaptain, AsyncCallback<Boolean> async);

    void getParticipantCyclistType(String email, AsyncCallback<String> async);

    void getParticipantName(String email, AsyncCallback<String> async);

    void getParticipantFirmName(String email, AsyncCallback<String> async);

    void getParticipantTeamName(String email, AsyncCallback<String> async);
}

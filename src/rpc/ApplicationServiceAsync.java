package rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import shared.DTO.Participant;
import shared.DTO.Person;

import java.util.ArrayList;

public interface ApplicationServiceAsync {

    void authorizePerson(String username, String password, AsyncCallback<Boolean> async);

    void returnPersons(AsyncCallback<String> async);

    void getAllParticipants(AsyncCallback<ArrayList<Participant>> async);
}

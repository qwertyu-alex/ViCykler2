package rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import shared.DTO.Participant;
import shared.DTO.Person;

import java.util.ArrayList;

@RemoteServiceRelativePath("ViCyklerService")
public interface ApplicationService extends RemoteService {
    boolean authorizePerson(String username, String password) throws Exception;
    String returnPersons() throws Exception;
    ArrayList<Participant> getAllParticipants() throws Exception;

}

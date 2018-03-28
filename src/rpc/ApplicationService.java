package rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ViCyklerService")
public interface ApplicationService extends RemoteService {
    boolean authorizePerson(String username, String password) throws Exception;
    String returnPersons() throws Exception;

}

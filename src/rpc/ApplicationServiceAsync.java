package rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ApplicationServiceAsync {

    void authorizePerson(String username, String password, AsyncCallback<Boolean> async);

    void returnPersons(AsyncCallback<String> async);
}

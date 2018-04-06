//Mikkel Storm && Astid Christensen

package shared.DTO;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Kilde: Y. Daniel Liang (2015), s. 431
 */
public class Admin extends Person implements IsSerializable{

    //Constructor

    public Admin(){
        super();
    }

    public Admin(String name, String email, String password){
        super(name,email,password);
    }
}

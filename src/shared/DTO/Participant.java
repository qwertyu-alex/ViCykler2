package shared.DTO;//Alexander Van Le && Oliver Lange

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Kilde: Y. Daniel Liang (2015), s. 431
 */
public class Participant extends Person implements IsSerializable {
    //Variabler
    private String cyclistType;

    //Default constructor
    public Participant(){
        super("null", "null", "null");
        cyclistType = "null";
    }

    //Getter
    public String getCyclistType() {
        return cyclistType;
    }

    //Setter
    public void setCyklistType(String cyclistType) {
        this.cyclistType = cyclistType;
    }


}

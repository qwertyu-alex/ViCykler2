package shared.DTO;//Astrid Christensen

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Kilde: Y. Daniel Liang (2015), s. 514
 */
public abstract class Person implements IsSerializable{

    protected String name = "";
    protected String email = "";
    protected String password = "";


    //Default constructor
    public Person(){

    }

    //Constructer
    public Person(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    //Getter-metoder
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    //Setter-metoder
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


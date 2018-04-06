package shared;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import java.util.*;

public class FieldVerifier {

    public static boolean validateName(String name) {
        //Compiler en regex:
        RegExp regExp = RegExp.compile("[a-zA-Z ]+");
        // Tjekker om vores string "name" passer til vores regex:
        MatchResult matcher = regExp.exec(name);
        //Hvis matcher ikke er null, så gør matchFound true, ellers så gør matchFound false:
        boolean matchFound = matcher != null;

        return matchFound;
    }


}

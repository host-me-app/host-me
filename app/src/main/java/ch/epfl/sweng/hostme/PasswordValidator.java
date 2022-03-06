package ch.epfl.sweng.hostme;

import java.util.regex.Pattern;

public class PasswordValidator {

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])"
            + "(?=.*[!@#&()–:;',?/*~$^+=<>]).{8,20}$";
    private static final Pattern PATTERN = Pattern.compile(PASSWORD_PATTERN);

    private static Boolean checkPattern(String password) {
        return PATTERN.matcher(password)
                .matches();
    }

    public static Boolean isValid(String password) {
        return checkPattern(password);
    }

}

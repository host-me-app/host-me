package ch.epfl.sweng.hostme.utils;

import java.util.regex.Pattern;


public class PasswordValidator {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])"
                    + "(?=.*[!@#&()–:;',?/*~$^+=<>]).{8,20}$";
    private static final Pattern PATTERN = Pattern.compile(PASSWORD_PATTERN);

    /**
     * Check if the password has a valid pattern
     *
     * @return True if the password has a valid pattern
     */
    private static Boolean checkPattern(String password) {
        return PATTERN.matcher(password)
                .matches();
    }

    /**
     * Public method to check password validity
     *
     * @return True if the password is valid
     */
    public static Boolean isValid(String password) {
        return checkPattern(password);
    }

}

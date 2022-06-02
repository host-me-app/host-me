package ch.epfl.sweng.hostme.creation;

import java.util.regex.Pattern;

public class EmailValidator {

    private static final String EMAIL_PATTERN =
            "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]"
                    + "{2,})$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_PATTERN);

    /**
     * Check if the email has a valid pattern
     *
     * @return True if the email has a valid pattern
     */
    private static Boolean checkPattern(String email) {
        return PATTERN.matcher(email)
                .matches();
    }

    /**
     * Check email validity (uniqueness and pattern)
     *
     * @return True if email is valid
     */
    public static Boolean isValid(String email) {
        return checkPattern(email);
    }
}

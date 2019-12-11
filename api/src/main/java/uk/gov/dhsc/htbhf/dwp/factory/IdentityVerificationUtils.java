package uk.gov.dhsc.htbhf.dwp.factory;

import org.flywaydb.core.internal.util.StringUtils;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCAdult;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;
import uk.gov.dhsc.htbhf.dwp.model.VerificationOutcome;

import java.util.function.BiPredicate;

/**
 * A set of utility methods used to help verify an incoming request matches what is stored in the seeding database.
 */
public class IdentityVerificationUtils {

    /**
     * Determines the verification outcome based on the provided seeded value and request value and the BiPredicate to run against those values.
     *
     * @param ucSeededValue     The seeded value
     * @param requestValue      The request value
     * @param verificationCheck The check to run against those values.
     * @return NOT_HELD if the seeded value is null, NOT_SUPPLIED if the request value is null, MATCHED if the predicate result is true otherwise NOT_MATCHED.
     */
    public static VerificationOutcome determineVerificationOutcome(String ucSeededValue, String requestValue, BiPredicate<String, String> verificationCheck) {
        if (requestValue == null) {
            return VerificationOutcome.NOT_SUPPLIED;
        }
        if (ucSeededValue == null) {
            return VerificationOutcome.NOT_HELD;
        }
        if (verificationCheck.test(ucSeededValue, requestValue)) {
            return VerificationOutcome.MATCHED;
        }
        return VerificationOutcome.NOT_MATCHED;
    }


    /**
     * Tests if the two Strings are equal ignoring whitespace.
     *
     * @param s1 The first string
     * @param s2 The second string
     * @return true if they match, otherwise false
     */
    public static boolean areEqualIgnoringWhitespace(String s1, String s2) {
        return areEqual(s1.replaceAll("\\s+", ""), s2.replaceAll("\\s+", ""));
    }

    /**
     * Tests if the first six characters of the two strings match (ignoring case).
     *
     * @param s1 The first string
     * @param s2 The second string
     * @return true if they match, else false.
     */
    public static boolean firstSixCharacterMatch(String s1, String s2) {
        return areEqual(StringUtils.left(s1, 6), StringUtils.left(s2, 6));
    }


    /**
     * Tests if the adult from the seeded database matches the given person. They match if the
     * NINO on both match, the surname matches and the date of birth match if it has been set on
     * the seeded value. If there is no seeded value, then it is presumed that they match.
     *
     * @param adult  The seeded adult data.
     * @param person The person to match against the seeded data.
     * @return true if match, else false.
     */
    public static boolean matchingAdult(UCAdult adult, PersonDTO person) {
        return areEqual(adult.getNino(), person.getNino())
                && areEqual(adult.getSurname(), person.getSurname())
                && dateOfBirthMatches(adult, person);
    }

    /**
     * Are the two Strings equal ignoring case once they have been trimmed. If
     * both values are null then they are considered equal.
     *
     * @param s1 The first string
     * @param s2 The second string
     * @return true if equal, else false.
     */
    public static boolean areEqual(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return true;
        }
        if (s1 == null || s2 == null) {
            return false;
        }
        return s1.trim().equalsIgnoreCase(s2.trim());
    }

    private static boolean dateOfBirthMatches(UCAdult ucAdult, PersonDTO person) {
        if (ucAdult.getDateOfBirth() == null) {
            return true;
        }
        return ucAdult.getDateOfBirth().equals(person.getDateOfBirth());
    }
}

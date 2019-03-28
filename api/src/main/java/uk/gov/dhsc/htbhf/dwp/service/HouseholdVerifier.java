package uk.gov.dhsc.htbhf.dwp.service;

import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.stereotype.Service;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCAdult;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;

@Service
public class HouseholdVerifier {

    /**
     * Determines if a given household contains an adult matching a given person.
     * DWP rules specify to match on the surname, postcode and first six characters of address line 1.
     * @param household household to check
     * @param person person to check
     * @return true the details of the household and the person match.
     */
    public Boolean detailsMatch(UCHousehold household, PersonDTO person) {
        return household.getAdults().stream()
                .anyMatch(adult -> adultMatchesPerson(adult, person));
    }

    public Boolean detailsMatch(LegacyHousehold household, PersonDTO person) {
        boolean nameMatches = household.getAdults().stream()
                .anyMatch(adult -> areEqual(adult.getSurname(), person.getLastName()));

        return nameMatches && addressMatches(household, person);
    }

    private boolean adultMatchesPerson(UCAdult adult, PersonDTO person) {
        return areEqual(adult.getSurname(), person.getLastName())
                && firstSixCharacterMatch(person.getAddress().getAddressLine1(), adult.getAddressLine1())
                && areEqualIgnoringWhitespace(adult.getPostcode(), person.getAddress().getPostcode());
    }

    private boolean addressMatches(LegacyHousehold household, PersonDTO person) {
        return firstSixCharacterMatch(household.getAddressLine1(), person.getAddress().getAddressLine1())
                && areEqualIgnoringWhitespace(household.getPostcode(), person.getAddress().getPostcode());
    }

    private boolean firstSixCharacterMatch(String s1, String s2) {
        return areEqual(StringUtils.left(s1, 6), StringUtils.left(s2, 6));
    }

    private Boolean areEqualIgnoringWhitespace(String s1, String s2) {
        return areEqual(s1.replaceAll("\\s+", ""), s2.replaceAll("\\s+", ""));
    }

    private Boolean areEqual(String s1, String s2) {
        return s1.trim().equalsIgnoreCase(s2.trim());
    }

}

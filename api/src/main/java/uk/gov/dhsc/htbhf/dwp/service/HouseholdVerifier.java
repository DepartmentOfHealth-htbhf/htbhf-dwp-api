package uk.gov.dhsc.htbhf.dwp.service;

import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.stereotype.Service;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;

@Service
public class HouseholdVerifier {

    /**
     * Determines if a given household contains an adult matching a given person.
     * DWP rules specify to only match on the first six characters of the address line 1 field.
     * @param household household to check
     * @param person person to check
     * @return true the details of the household and the person match.
     */
    public Boolean detailsMatch(UCHousehold household, PersonDTO person) {
        return household.getAdults().stream()
                .anyMatch(adult ->
                        areEqual(adult.getForename(), person.getForename())
                        && areEqual(adult.getSurname(), person.getSurname())
                        && firstSixCharacterMatch(person.getAddress().getAddressLine1(), adult.getAddressLine1())
                        && areEqual(adult.getPostcode(), person.getAddress().getPostcode())
                );
    }

    public Boolean detailsMatch(LegacyHousehold household, PersonDTO person) {
        boolean nameMatches = household.getAdults().stream()
                .anyMatch(adult ->
                        areEqual(adult.getForename(), person.getForename())
                        && areEqual(adult.getSurname(), person.getSurname()));

        return nameMatches
                && firstSixCharacterMatch(household.getAddressLine1(), person.getAddress().getAddressLine1())
                && areEqual(household.getPostcode(), person.getAddress().getPostcode());
    }

    private boolean firstSixCharacterMatch(String s1, String s2) {
        return areEqual(StringUtils.left(s1, 6), StringUtils.left(s2, 6));
    }

    private Boolean areEqual(String s1, String s2) {
        return s1.trim().equalsIgnoreCase(s2.trim());
    }
}

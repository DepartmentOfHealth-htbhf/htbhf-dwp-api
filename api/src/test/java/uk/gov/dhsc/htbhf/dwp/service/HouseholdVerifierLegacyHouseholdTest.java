package uk.gov.dhsc.htbhf.dwp.service;

import org.junit.jupiter.api.Test;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyAdult;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;
import uk.gov.dhsc.htbhf.dwp.model.AddressDTO;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.dhsc.htbhf.dwp.entity.LegacyHouseholdFactory.aHouseholdWithNoAdultsOrChildren;

public class HouseholdVerifierLegacyHouseholdTest {

    private final HouseholdVerifier householdVerifier = new HouseholdVerifier();
    private final AddressDTO address = AddressDTO.builder()
            .addressLine1("742 Evergreen Terrace")
            .postcode("AA11AA")
            .build();
    private final PersonDTO person = PersonDTO.builder()
            .forename("Lisa")
            .surname("Simpson")
            .address(address)
            .build();

    @Test
    void shouldReturnTrueWhenPersonMatchesLegacyHousehold() {
        LegacyAdult adult = LegacyAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .build();
        LegacyHousehold household = aHouseholdWithNoAdultsOrChildren().build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, person);

        assertTrue(response);
    }

    @Test
    void shouldReturnFalseWhenForenameDoesNotMatchLegacyHousehold() {
        LegacyAdult adult = LegacyAdult.builder()
                .forename("Bart")
                .surname("Simpson")
                .build();
        LegacyHousehold household = aHouseholdWithNoAdultsOrChildren().build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, person);

        assertFalse(response);
    }

    @Test
    void shouldReturnFalseWhenSurnameDoesNotMatchLegacyHousehold() {
        LegacyAdult adult = LegacyAdult.builder()
                .forename("Lisa")
                .surname("Smith")
                .build();
        LegacyHousehold household = aHouseholdWithNoAdultsOrChildren().build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, person);

        assertFalse(response);
    }

    @Test
    void shouldReturnFalseWhenAddressLine1DoesNotMatchLegacyHousehold() {
        LegacyAdult adult = LegacyAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .build();
        LegacyHousehold household = aHouseholdWithNoAdultsOrChildren().addressLine1("Fake Street").build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, person);

        assertFalse(response);
    }

    @Test
    void shouldReturnFalseWhenPostcodeDoesNotMatchLegacyHousehold() {
        LegacyAdult adult = LegacyAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .build();
        LegacyHousehold household = aHouseholdWithNoAdultsOrChildren().postcode("W1 1NA").build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, person);

        assertFalse(response);
    }

    @Test
    void shouldReturnTrueWhenAddressLine1FirstSixCharactersMatchesLegacyHousehold() {
        LegacyAdult adult = LegacyAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .build();
        LegacyHousehold household = aHouseholdWithNoAdultsOrChildren().addressLine1("742 Ev_DIFFERENT").build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, person);

        assertTrue(response);
    }

    @Test
    void shouldReturnTrueWhenAddressLine1FirstSixCharactersDifferentCaseMatchesLegacyHousehold() {
        LegacyAdult adult = LegacyAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .build();
        LegacyHousehold household = aHouseholdWithNoAdultsOrChildren().addressLine1("742 ev_DIFFERENT").build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, person);

        assertTrue(response);
    }

    @Test
    void shouldReturnFalseWhenAddressLine1UnderSixCharacterDoesNotMatchLegacyHousehold() {
        LegacyAdult adult = LegacyAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .build();
        LegacyHousehold household = aHouseholdWithNoAdultsOrChildren().addressLine1("742").build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, person);

        assertFalse(response);
    }


}

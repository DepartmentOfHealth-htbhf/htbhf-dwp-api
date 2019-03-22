package uk.gov.dhsc.htbhf.dwp.service;

import org.junit.jupiter.api.Test;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCAdult;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.model.AddressDTO;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.dhsc.htbhf.dwp.entity.UCHouseholdFactory.aHouseholdWithNoAdultsOrChildren;

public class HouseholdVerifierUCHouseholdTest {

    private final HouseholdVerifier householdVerifier = new HouseholdVerifier();
    private final AddressDTO address = AddressDTO.builder()
            .addressLine1("742 Evergreen Terrace")
            .postcode("BS1 5AA")
            .build();
    private final PersonDTO person = PersonDTO.builder()
            .forename("Lisa")
            .surname("Simpson")
            .address(address)
            .build();

    @Test
    void shouldReturnTrueWhenPersonMatchesUCHousehold() {
        UCAdult adult = UCAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .addressLine1("742 Evergreen Terrace")
                .postcode("BS1 5AA")
                .build();
        UCHousehold household = aHouseholdWithNoAdultsOrChildren().build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, person);

        assertTrue(response);
    }

    @Test
    void shouldReturnFalseWhenForenameDoesNotMatchUCHousehold() {
        UCAdult adult = UCAdult.builder()
                .forename("Bart")
                .surname("Simpson")
                .addressLine1("742 Evergreen Terrace")
                .postcode("BS1 5AA")
                .build();
        UCHousehold household = aHouseholdWithNoAdultsOrChildren().build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, person);

        assertFalse(response);
    }

    @Test
    void shouldReturnFalseWhenSurnameDoesNotMatchUCHousehold() {
        UCAdult adult = UCAdult.builder()
                .forename("Lisa")
                .surname("Smith")
                .addressLine1("742 Evergreen Terrace")
                .postcode("BS1 5AA")
                .build();
        UCHousehold household = aHouseholdWithNoAdultsOrChildren().build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, person);

        assertFalse(response);
    }

    @Test
    void shouldReturnFalseWhenAddressLine1DoesNotMatchUCHousehold() {
        UCAdult adult = UCAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .addressLine1("745 Deciduous Road")
                .postcode("BS1 5AA")
                .build();
        UCHousehold household = aHouseholdWithNoAdultsOrChildren().build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, person);

        assertFalse(response);
    }

    @Test
    void shouldReturnFalseWhenPostcodeDoesNotMatchUCHousehold() {
        UCAdult adult = UCAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .addressLine1("742 Evergreen Terrace")
                .postcode("W1 1NA")
                .build();
        UCHousehold household = aHouseholdWithNoAdultsOrChildren().build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, person);

        assertFalse(response);
    }

    @Test
    void shouldReturnTrueWhenAddressLine1FirstSixCharactersMatchesUCHousehold() {
        UCAdult adult = UCAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .addressLine1("742 Ev_DIFFERENT")
                .postcode("BS1 5AA")
                .build();
        UCHousehold household = aHouseholdWithNoAdultsOrChildren().build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, person);

        assertTrue(response);
    }

    @Test
    void shouldReturnTrueWhenAddressLine1FirstSixCharactersDifferentCaseMatchesUCHousehold() {
        UCAdult adult = UCAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .addressLine1("742 ev_DIFFERENT")
                .postcode("BS1 5AA")
                .build();
        UCHousehold household = aHouseholdWithNoAdultsOrChildren().build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, person);

        assertTrue(response);
    }

    @Test
    void shouldReturnFalseWhenAddressLine1UnderSixCharacterDoesNotMatchUCHousehold() {
        UCAdult adult = UCAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .addressLine1("742")
                .postcode("BS1 5AA")
                .build();
        UCHousehold household = aHouseholdWithNoAdultsOrChildren().build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, person);

        assertFalse(response);
    }


}

package uk.gov.dhsc.htbhf.dwp.service;

import org.junit.jupiter.api.Test;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyAdult;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static uk.gov.dhsc.htbhf.dwp.entity.LegacyHouseholdFactory.aHouseholdWithNoAdultsOrChildren;
import static uk.gov.dhsc.htbhf.dwp.factory.DWPPersonDTOTestDataFactory.aValidDWPPerson;

public class HouseholdVerifierLegacyHouseholdTest {

    private final HouseholdVerifier householdVerifier = new HouseholdVerifier();

    @Test
    void shouldReturnTrueWhenPersonMatchesLegacyHousehold() {
        LegacyAdult adult = LegacyAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .build();
        LegacyHousehold household = aHouseholdWithNoAdultsOrChildren().build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, aValidDWPPerson());

        assertThat(response).isTrue();
    }

    @Test
    void shouldReturnFalseWhenSurnameDoesNotMatchLegacyHousehold() {
        LegacyAdult adult = LegacyAdult.builder()
                .forename("Lisa")
                .surname("Smith")
                .build();
        LegacyHousehold household = aHouseholdWithNoAdultsOrChildren().build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, aValidDWPPerson());

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenAddressLine1DoesNotMatchLegacyHousehold() {
        LegacyAdult adult = LegacyAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .build();
        LegacyHousehold household = aHouseholdWithNoAdultsOrChildren().addressLine1("Fake Street").build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, aValidDWPPerson());

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenPostcodeDoesNotMatchLegacyHousehold() {
        LegacyAdult adult = LegacyAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .build();
        LegacyHousehold household = aHouseholdWithNoAdultsOrChildren().postcode("W1 1NA").build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, aValidDWPPerson());

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnTrueWhenAddressLine1FirstSixCharactersMatchesLegacyHousehold() {
        LegacyAdult adult = LegacyAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .build();
        LegacyHousehold household = aHouseholdWithNoAdultsOrChildren().addressLine1("742 Ev_DIFFERENT").build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, aValidDWPPerson());

        assertThat(response).isTrue();
    }

    @Test
    void shouldReturnTrueWhenAddressLine1FirstSixCharactersDifferentCaseMatchesLegacyHousehold() {
        LegacyAdult adult = LegacyAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .build();
        LegacyHousehold household = aHouseholdWithNoAdultsOrChildren().addressLine1("742 ev_DIFFERENT").build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, aValidDWPPerson());

        assertThat(response).isTrue();
    }

    @Test
    void shouldReturnFalseWhenAddressLine1UnderSixCharacterDoesNotMatchLegacyHousehold() {
        LegacyAdult adult = LegacyAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .build();
        LegacyHousehold household = aHouseholdWithNoAdultsOrChildren().addressLine1("742").build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, aValidDWPPerson());

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnTrueWhenPostcodeMatchesWithSpacesLegacyHousehold() {
        LegacyAdult adult = LegacyAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .build();
        LegacyHousehold household = aHouseholdWithNoAdultsOrChildren().postcode("AA1 1AA").build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, aValidDWPPerson());

        assertThat(response).isTrue();
    }

}

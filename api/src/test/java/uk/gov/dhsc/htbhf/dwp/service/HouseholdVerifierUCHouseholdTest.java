package uk.gov.dhsc.htbhf.dwp.service;

import org.junit.jupiter.api.Test;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCAdult;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static uk.gov.dhsc.htbhf.dwp.entity.UCHouseholdFactory.aHouseholdWithNoAdultsOrChildren;
import static uk.gov.dhsc.htbhf.dwp.helper.PersonTestFactory.aPerson;

public class HouseholdVerifierUCHouseholdTest {

    private final HouseholdVerifier householdVerifier = new HouseholdVerifier();

    @Test
    void shouldReturnTrueWhenPersonMatchesUCHousehold() {
        UCAdult adult = UCAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .addressLine1("742 Evergreen Terrace")
                .postcode("AA11AA")
                .build();
        UCHousehold household = aHouseholdWithNoAdultsOrChildren().build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, aPerson());

        assertThat(response).isTrue();
    }

    @Test
    void shouldReturnFalseWhenSurnameDoesNotMatchUCHousehold() {
        UCAdult adult = UCAdult.builder()
                .forename("Lisa")
                .surname("Smith")
                .addressLine1("742 Evergreen Terrace")
                .postcode("AA11AA")
                .build();
        UCHousehold household = aHouseholdWithNoAdultsOrChildren().build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, aPerson());

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnFalseWhenAddressLine1DoesNotMatchUCHousehold() {
        UCAdult adult = UCAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .addressLine1("745 Deciduous Road")
                .postcode("AA11AA")
                .build();
        UCHousehold household = aHouseholdWithNoAdultsOrChildren().build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, aPerson());

        assertThat(response).isFalse();
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

        Boolean response = householdVerifier.detailsMatch(household, aPerson());

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnTrueWhenAddressLine1FirstSixCharactersMatchesUCHousehold() {
        UCAdult adult = UCAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .addressLine1("742 Ev_DIFFERENT")
                .postcode("AA11AA")
                .build();
        UCHousehold household = aHouseholdWithNoAdultsOrChildren().build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, aPerson());

        assertThat(response).isTrue();
    }

    @Test
    void shouldReturnTrueWhenAddressLine1FirstSixCharactersDifferentCaseMatchesUCHousehold() {
        UCAdult adult = UCAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .addressLine1("742 ev_DIFFERENT")
                .postcode("AA11AA")
                .build();
        UCHousehold household = aHouseholdWithNoAdultsOrChildren().build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, aPerson());

        assertThat(response).isTrue();
    }

    @Test
    void shouldReturnFalseWhenAddressLine1UnderSixCharacterDoesNotMatchUCHousehold() {
        UCAdult adult = UCAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .addressLine1("742")
                .postcode("AA11AA")
                .build();
        UCHousehold household = aHouseholdWithNoAdultsOrChildren().build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, aPerson());

        assertThat(response).isFalse();
    }

    @Test
    void shouldReturnTrueWhenPostcodeMatchesWithSpacesLegacyHousehold() {
        UCAdult adult = UCAdult.builder()
                .forename("Lisa")
                .surname("Simpson")
                .addressLine1("742 Evergreen Terrace")
                .postcode("AA1 1AA")
                .build();
        UCHousehold household = aHouseholdWithNoAdultsOrChildren().build().addAdult(adult);

        Boolean response = householdVerifier.detailsMatch(household, aPerson());

        assertThat(response).isTrue();
    }

}

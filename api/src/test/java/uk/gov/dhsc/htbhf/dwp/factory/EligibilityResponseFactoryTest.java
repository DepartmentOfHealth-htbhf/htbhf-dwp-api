package uk.gov.dhsc.htbhf.dwp.factory;

import org.junit.jupiter.api.Test;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.testhelper.UCHouseholdTestDataFactory;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static uk.gov.dhsc.htbhf.dwp.factory.EligibilityResponseFactory.createEligibilityResponse;
import static uk.gov.dhsc.htbhf.dwp.testhelper.LegacyHouseholdTestDataFactory.aLegacyHousehold;

public class EligibilityResponseFactoryTest {

    @Test
    void shouldCreateResponseFromLegacyHousehold() {
        LegacyHousehold household = aLegacyHousehold();

        EligibilityResponse response = createEligibilityResponse(household);

        assertThat(response.getHouseholdIdentifier()).isEqualTo(household.getHouseholdIdentifier());
        assertThat(response.getNumberOfChildrenUnderFour()).isEqualTo(2);
        assertThat(response.getNumberOfChildrenUnderOne()).isEqualTo(1);
    }

    @Test
    void shouldCreateResponseFromUCHousehold() {
        UCHousehold household = UCHouseholdTestDataFactory.aUCHousehold();

        EligibilityResponse response = createEligibilityResponse(household);

        assertThat(response.getHouseholdIdentifier()).isEqualTo(household.getHouseholdIdentifier());
        assertThat(response.getNumberOfChildrenUnderFour()).isEqualTo(2);
        assertThat(response.getNumberOfChildrenUnderOne()).isEqualTo(1);
    }
}

package uk.gov.dhsc.htbhf.dwp.factory;

import org.junit.jupiter.api.Test;
import uk.gov.dhsc.htbhf.dwp.entity.UCHouseholdFactory;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static uk.gov.dhsc.htbhf.dwp.entity.LegacyHouseholdFactory.aHousehold;
import static uk.gov.dhsc.htbhf.dwp.factory.EligibilityResponseFactory.createEligibilityResponse;

public class EligibilityResponseFactoryTest {

    @Test
    void shouldCreateResponseFromLegacyHousehold() {
        LegacyHousehold household = aHousehold();

        EligibilityResponse response = createEligibilityResponse(household);

        assertThat(response.getHouseholdIdentifier()).isEqualTo(household.getHouseholdIdentifier());
        assertThat(response.getNumberOfChildrenUnderFour()).isEqualTo(2);
        assertThat(response.getNumberOfChildrenUnderOne()).isEqualTo(1);
    }

    @Test
    void shouldCreateResponseFromUCHousehold() {
        UCHousehold household = UCHouseholdFactory.aHousehold();

        EligibilityResponse response = createEligibilityResponse(household);

        assertThat(response.getHouseholdIdentifier()).isEqualTo(household.getHouseholdIdentifier());
        assertThat(response.getNumberOfChildrenUnderFour()).isEqualTo(2);
        assertThat(response.getNumberOfChildrenUnderOne()).isEqualTo(1);
    }
}

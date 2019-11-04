package uk.gov.dhsc.htbhf.dwp.factory.v1;

import org.junit.jupiter.api.Test;
import uk.gov.dhsc.htbhf.dwp.entity.v1.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.model.v1.ChildDTO;
import uk.gov.dhsc.htbhf.dwp.model.v1.EligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.testhelper.v1.UCHouseholdTestDataFactory;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.LISA_DOB;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.MAGGIE_DOB;
import static uk.gov.dhsc.htbhf.eligibility.model.EligibilityStatus.ELIGIBLE;

public class EligibilityResponseFactoryTest {

    private EligibilityResponseFactory factory = new EligibilityResponseFactory();

    @Test
    void shouldCreateResponseFromUCHousehold() {
        UCHousehold household = UCHouseholdTestDataFactory.aUCHousehold();

        EligibilityResponse response = factory.createEligibilityResponse(household, ELIGIBLE);

        assertThat(response.getHouseholdIdentifier()).isEqualTo(household.getHouseholdIdentifier());
        assertThat(response.getNumberOfChildrenUnderFour()).isEqualTo(2);
        assertThat(response.getNumberOfChildrenUnderOne()).isEqualTo(1);
        assertThat(response.getChildren()).containsExactlyInAnyOrder(expectedChildren());
        assertThat(response.getEligibilityStatus()).isEqualTo(ELIGIBLE);
    }

    private ChildDTO[] expectedChildren() {
        ChildDTO childUnderOne = ChildDTO.builder().dateOfBirth(MAGGIE_DOB).build();
        ChildDTO childUnderFour = ChildDTO.builder().dateOfBirth(LISA_DOB).build();
        return new ChildDTO[]{childUnderOne, childUnderFour};
    }
}

package uk.gov.dhsc.htbhf.dwp.testhelper;

import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;

import static uk.gov.dhsc.htbhf.dwp.model.EligibilityStatus.ELIGIBLE;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.SIMPSON_UC_HOUSEHOLD_IDENTIFIER;

public class EligibilityResponseTestDataFactory {

    public static EligibilityResponse anEligibilityResponse() {
        return aValidEligibilityResponseBuilder()
                .build();
    }

    public static EligibilityResponse.EligibilityResponseBuilder aValidEligibilityResponseBuilder() {
        return EligibilityResponse.builder()
                .eligibilityStatus(ELIGIBLE)
                .numberOfChildrenUnderOne(1)
                .numberOfChildrenUnderFour(2)
                .householdIdentifier(SIMPSON_UC_HOUSEHOLD_IDENTIFIER);
    }
}

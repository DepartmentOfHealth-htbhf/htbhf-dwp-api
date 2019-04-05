package uk.gov.dhsc.htbhf.dwp.helper;

import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;

import static uk.gov.dhsc.htbhf.dwp.model.EligibilityStatus.ELIGIBLE;

public class EligibilityResponseTestFactory {

    public static EligibilityResponse anEligibilityResponse() {
        return aValidEligibilityResponseBuilder()
                .build();
    }

    public static EligibilityResponse.EligibilityResponseBuilder aValidEligibilityResponseBuilder() {
        return EligibilityResponse.builder()
                .eligibilityStatus(ELIGIBLE)
                .numberOfChildrenUnderOne(1)
                .numberOfChildrenUnderFour(2)
                .householdIdentifier("dwpHousehold1");
    }
}

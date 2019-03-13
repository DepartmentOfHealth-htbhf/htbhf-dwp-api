package uk.gov.dhsc.htbhf.dwp.helper;

import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;

public class EligibilityResponseTestFactory {

    public static EligibilityResponse anEligibilityResponse() {
        return EligibilityResponse.builder().build();
    }
}

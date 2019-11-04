package uk.gov.dhsc.htbhf.dwp.testhelper.v1;

import uk.gov.dhsc.htbhf.dwp.model.v1.DWPEligibilityRequest;

import static uk.gov.dhsc.htbhf.dwp.testhelper.v1.DWPPersonDTOTestDataFactory.aValidDWPPerson;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v1.TestConstants.ELIGIBLE_END_DATE;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v1.TestConstants.ELIGIBLE_START_DATE;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v1.TestConstants.UC_MONTHLY_INCOME_THRESHOLD;


public class DWPEligibilityRequestTestDataFactory {

    public static DWPEligibilityRequest aValidDWPEligibilityRequest() {
        return aValidEligibilityRequestBuilder().build();
    }

    private static DWPEligibilityRequest.DWPEligibilityRequestBuilder aValidEligibilityRequestBuilder() {
        return DWPEligibilityRequest.builder()
                .person(aValidDWPPerson())
                .ucMonthlyIncomeThreshold(UC_MONTHLY_INCOME_THRESHOLD)
                .eligibleStartDate(ELIGIBLE_START_DATE)
                .eligibleEndDate(ELIGIBLE_END_DATE);
    }
}

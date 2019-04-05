package uk.gov.dhsc.htbhf.dwp.testhelper;

import uk.gov.dhsc.htbhf.dwp.model.EligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import static uk.gov.dhsc.htbhf.dwp.testhelper.PersonDTOTestDataFactory.aValidPerson;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.ELIGIBLE_END_DATE;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.ELIGIBLE_START_DATE;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.UC_MONTHLY_INCOME_THRESHOLD;

public class EligibilityRequestTestDataFactory {

    public static EligibilityRequest anEligibilityRequest() {
        return buildDefaultRequest().build();
    }

    public static EligibilityRequest aValidEligibilityRequest() {
        return buildDefaultRequest().build();
    }

    public static EligibilityRequest anEligibilityRequestWithPerson(PersonDTO person) {
        return buildDefaultRequest().person(person).build();
    }

    public static EligibilityRequest anEligibilityRequestWithUcMonthlyIncomeThreshold(BigDecimal ucMonthlyIncomeThreshold) {
        return buildDefaultRequest().ucMonthlyIncomeThreshold(ucMonthlyIncomeThreshold).build();
    }

    public static EligibilityRequest anEligibilityRequestWithEligibleStartDate(LocalDate date) {
        return buildDefaultRequest().eligibleStartDate(date).build();
    }

    public static EligibilityRequest anEligibilityRequestWithEligibleEndDate(LocalDate date) {
        return buildDefaultRequest().eligibleEndDate(date).build();
    }

    private static EligibilityRequest.EligibilityRequestBuilder buildDefaultRequest() {
        return EligibilityRequest.builder()
                .person(aValidPerson())
                .eligibleStartDate(ELIGIBLE_START_DATE)
                .eligibleEndDate(ELIGIBLE_END_DATE)
                .ucMonthlyIncomeThreshold(UC_MONTHLY_INCOME_THRESHOLD);
    }

}

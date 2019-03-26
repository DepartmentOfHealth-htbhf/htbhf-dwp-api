package uk.gov.dhsc.htbhf.dwp.factory;

import uk.gov.dhsc.htbhf.dwp.model.EligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import static uk.gov.dhsc.htbhf.dwp.factory.PersonDTOTestDataFactory.aValidPersonBuilder;

public class EligibilityRequestTestDataFactory {

    private static final BigDecimal UC_MONTHLY_INCOME_THRESHOLD = new BigDecimal(408);
    private static final LocalDate ELIGIBLE_START_DATE = LocalDate.parse("2019-01-01");
    private static final LocalDate ELIGIBLE_END_DATE = LocalDate.parse("2019-02-01");

    public static EligibilityRequest aValidEligibilityRequest() {
        return aValidEligibilityRequestBuilder().build();
    }

    public static EligibilityRequest anEligibilityRequestWithPerson(PersonDTO person) {
        return aValidEligibilityRequestBuilder().person(person).build();
    }

    public static EligibilityRequest anEligibilityRequestWithUcMonthlyIncomeThreshold(BigDecimal ucMonthlyIncomeThreshold) {
        return aValidEligibilityRequestBuilder().ucMonthlyIncomeThreshold(ucMonthlyIncomeThreshold).build();
    }

    public static EligibilityRequest anEligibilityRequestWithEligibleStartDate(LocalDate date) {
        return aValidEligibilityRequestBuilder().eligibleStartDate(date).build();
    }

    public static EligibilityRequest anEligibilityRequestWithEligibleEndDate(LocalDate date) {
        return aValidEligibilityRequestBuilder().eligibleEndDate(date).build();
    }

    private static EligibilityRequest.EligibilityRequestBuilder aValidEligibilityRequestBuilder() {
        return EligibilityRequest.builder()
                .person(aValidPersonBuilder().build())
                .ucMonthlyIncomeThreshold(UC_MONTHLY_INCOME_THRESHOLD)
                .eligibleStartDate(ELIGIBLE_START_DATE)
                .eligibleEndDate(ELIGIBLE_END_DATE);
    }
}

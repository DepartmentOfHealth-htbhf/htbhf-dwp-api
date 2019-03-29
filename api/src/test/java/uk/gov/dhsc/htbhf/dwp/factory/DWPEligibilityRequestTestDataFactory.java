package uk.gov.dhsc.htbhf.dwp.factory;

import uk.gov.dhsc.htbhf.dwp.model.DWPEligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.DWPPersonDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import static uk.gov.dhsc.htbhf.dwp.factory.DWPPersonDTOTestDataFactory.aValidDWPPerson;


public class DWPEligibilityRequestTestDataFactory {

    private static final BigDecimal UC_MONTHLY_INCOME_THRESHOLD = new BigDecimal(408);
    private static final LocalDate ELIGIBLE_START_DATE = LocalDate.parse("2019-01-01");
    private static final LocalDate ELIGIBLE_END_DATE = LocalDate.parse("2019-02-01");

    public static DWPEligibilityRequest aValidDWPEligibilityRequest() {
        return aValidEligibilityRequestBuilder().build();
    }

    public static DWPEligibilityRequest aDWPEligibilityRequestWithPerson(DWPPersonDTO person) {
        return aValidEligibilityRequestBuilder().person(person).build();
    }

    public static DWPEligibilityRequest aDWPEligibilityRequestWithUcMonthlyIncomeThreshold(BigDecimal ucMonthlyIncomeThreshold) {
        return aValidEligibilityRequestBuilder().ucMonthlyIncomeThreshold(ucMonthlyIncomeThreshold).build();
    }

    public static DWPEligibilityRequest aDWPEligibilityRequestWithEligibleStartDate(LocalDate date) {
        return aValidEligibilityRequestBuilder().eligibleStartDate(date).build();
    }

    public static DWPEligibilityRequest aDWPEligibilityRequestWithEligibleEndDate(LocalDate date) {
        return aValidEligibilityRequestBuilder().eligibleEndDate(date).build();
    }

    private static DWPEligibilityRequest.DWPEligibilityRequestBuilder aValidEligibilityRequestBuilder() {
        return DWPEligibilityRequest.builder()
                .person(aValidDWPPerson())
                .ucMonthlyIncomeThreshold(UC_MONTHLY_INCOME_THRESHOLD)
                .eligibleStartDate(ELIGIBLE_START_DATE)
                .eligibleEndDate(ELIGIBLE_END_DATE);
    }
}

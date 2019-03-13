package uk.gov.dhsc.htbhf.dwp.helper;

import uk.gov.dhsc.htbhf.dwp.model.EligibilityRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static uk.gov.dhsc.htbhf.dwp.helper.PersonTestFactory.aPerson;

public class EligibilityRequestTestFactory {

    public static EligibilityRequest anEligibilityRequest() {
        return EligibilityRequest.builder()
                .person(aPerson())
                .eligibleStartDate(LocalDate.now())
                .eligibleEndDate(LocalDate.now())
                .ucMonthlyIncomeThreshold(BigDecimal.ONE)
                .build();
    }
}

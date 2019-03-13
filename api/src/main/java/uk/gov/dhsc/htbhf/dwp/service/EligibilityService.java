package uk.gov.dhsc.htbhf.dwp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class EligibilityService {

    private final DWPClient dwpClient;
    private final Integer eligibilityCheckInWeeks;
    private final BigDecimal ucMonthlyIncomeThreshold;

    public EligibilityService(@Value("${dwp.monthly-income-threshold}") BigDecimal ucMonthlyIncomeThreshold,
                              @Value("${dwp.eligibility-check-in-weeks}") Integer eligibilityCheckInWeeks,
                              DWPClient dwpClient) {
        this.dwpClient = dwpClient;
        this.eligibilityCheckInWeeks = eligibilityCheckInWeeks;
        this.ucMonthlyIncomeThreshold = ucMonthlyIncomeThreshold;
    }

    public EligibilityResponse checkEligibility(PersonDTO person) {
        var request = EligibilityRequest.builder()
                .person(person)
                .eligibleEndDate(getEligibleEndDate())
                .eligibleStartDate(getEligibleStartDate())
                .ucMonthlyIncomeThreshold(ucMonthlyIncomeThreshold)
                .build();

        return dwpClient.checkEligibility(request);
    }

    private LocalDate getEligibleStartDate() {
        LocalDate endDate = getEligibleEndDate();
        return endDate.minusWeeks(eligibilityCheckInWeeks);
    }

    private LocalDate getEligibleEndDate() {
        return LocalDate.now();
    }
}

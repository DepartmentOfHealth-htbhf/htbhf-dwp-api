package uk.gov.dhsc.htbhf.dwp.factory;

import uk.gov.dhsc.htbhf.dwp.model.EligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import static uk.gov.dhsc.htbhf.dwp.factory.AddressDTOTestDataFactory.aValidAddress;

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

    public static EligibilityRequest anEligibilityRequestWithInvalidPerson() {
        return anEligibilityRequestWithPerson(anInvalidPerson());
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
                .person(aValidPerson())
                .ucMonthlyIncomeThreshold(UC_MONTHLY_INCOME_THRESHOLD)
                .eligibleStartDate(ELIGIBLE_START_DATE)
                .eligibleEndDate(ELIGIBLE_END_DATE);
    }

    private static PersonDTO aValidPerson() {
        return aValidPersonBuilder().build();
    }

    private static PersonDTO anInvalidPerson() {
        return aValidPersonBuilder().nino(null).build();
    }

    private static PersonDTO.PersonDTOBuilder aValidPersonBuilder() {
        return PersonDTO.builder()
                .forename("Lisa")
                .surname("Simpson")
                .nino("QQ123456C")
                .dateOfBirth(LocalDate.parse("1985-12-30"))
                .address(aValidAddress());
    }
}

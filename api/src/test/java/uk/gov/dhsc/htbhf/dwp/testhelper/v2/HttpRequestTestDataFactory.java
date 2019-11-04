package uk.gov.dhsc.htbhf.dwp.testhelper.v2;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.*;

public class HttpRequestTestDataFactory {

    public static HttpEntity<Void> aValidEligibilityHttpEntity() {
        return buildHttpEntityWithNino(VALID_NINO_V2);
    }

    public static HttpEntity<Void> anInvalidEligibilityHttpEntity() {
        return buildHttpEntityWithNino("ZZZZZZZZZ");
    }

    private static HttpEntity<Void> buildHttpEntityWithNino(String nino) {
        LocalDate eligibilityEndDate = LocalDate.now().plusDays(28);
        String eligibilityEndDateString = DateTimeFormatter.ISO_LOCAL_DATE.format(eligibilityEndDate);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("surname", SIMPSON_SURNAME);
        httpHeaders.add("nino", nino);
        httpHeaders.add("dateOfBirth", HOMER_DOB_STRING);
        httpHeaders.add("eligibilityEndDate", eligibilityEndDateString);
        httpHeaders.add("addressLine1", SIMPSONS_ADDRESS_LINE_1);
        httpHeaders.add("postcode", SIMPSONS_POSTCODE);
        httpHeaders.add("emailAddress", HOMER_EMAIL);
        httpHeaders.add("mobilePhoneNumber", HOMER_MOBILE);
        httpHeaders.add("pregnantDependentDob", MAGGIE_DOB_STRING);
        httpHeaders.add("ucMonthlyIncomeThreshold", String.valueOf(UC_MONTHLY_INCOME_THRESHOLD_IN_PENCE));
        return new HttpEntity<>(httpHeaders);
    }
}

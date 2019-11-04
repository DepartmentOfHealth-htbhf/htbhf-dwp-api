package uk.gov.dhsc.htbhf.dwp.testhelper.v2;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.HOMER_DOB_STRING;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.HOMER_NINO;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.SIMPSON_SURNAME;

public class HttpRequestTestDataFactory {

    public static HttpEntity<Void> aValidEligibilityHttpEntity() {
        LocalDate eligibilityEndDate = LocalDate.now().plusDays(28);
        String eligibilityEndDateString = DateTimeFormatter.ISO_LOCAL_DATE.format(eligibilityEndDate);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("surname", SIMPSON_SURNAME);
        httpHeaders.add("nino", HOMER_NINO);
        httpHeaders.add("dateOfBirth", HOMER_DOB_STRING);
        httpHeaders.add("eligibilityEndDate", eligibilityEndDateString);
        return new HttpEntity<>(httpHeaders);
    }
}

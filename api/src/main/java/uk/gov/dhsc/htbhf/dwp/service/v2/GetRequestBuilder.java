package uk.gov.dhsc.htbhf.dwp.service.v2;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import uk.gov.dhsc.htbhf.dwp.model.v2.DWPEligibilityRequestV2;
import uk.gov.dhsc.htbhf.dwp.model.v2.PersonDTOV2;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static uk.gov.dhsc.htbhf.dwp.service.v2.HeaderName.*;

/**
 * Component used to build up a GET request with headers for calling the DWP API.
 */
@Component
public class GetRequestBuilder {

    /**
     * Builds the HTTP request with headers from the given request. Any values that are not provided will
     * result in the header being omitted from the request. Any dates will be formatted using
     * java.time.format.DateTimeFormatter.ISO_LOCAL_DATE.
     *
     * @param eligibilityRequest The request to take values from and add as headers to the request.
     * @return The built HTTP entity.
     */
    public HttpEntity buildRequestWithHeaders(DWPEligibilityRequestV2 eligibilityRequest) {
        PersonDTOV2 person = eligibilityRequest.getPerson();
        HttpHeaders httpHeaders = new HttpHeaders();
        addHeaderIfNotNull(httpHeaders, SURNAME, person.getSurname());
        addHeaderIfNotNull(httpHeaders, NINO, person.getNino());
        addHeaderIfNotNull(httpHeaders, DATE_OF_BIRTH, nullSafeDateFormat(person.getDateOfBirth()));
        addHeaderIfNotNull(httpHeaders, ELIGIBILITY_END_DATE, nullSafeDateFormat(eligibilityRequest.getEligibilityEndDate()));
        addHeaderIfNotNull(httpHeaders, ADDRESS_LINE_1, person.getAddressLine1());
        addHeaderIfNotNull(httpHeaders, POSTCODE, person.getPostcode());
        addHeaderIfNotNull(httpHeaders, EMAIL_ADDRESS, person.getEmailAddress());
        addHeaderIfNotNull(httpHeaders, MOBILE_PHONE_NUMBER, person.getMobilePhoneNumber());
        addHeaderIfNotNull(httpHeaders, PREGNANT_DEPENDENT_DOB, nullSafeDateFormat(person.getPregnantDependentDob()));
        addHeaderIfNotNull(httpHeaders, UC_MONTHLY_INCOME_THRESHOLD, String.valueOf(eligibilityRequest.getUcMonthlyIncomeThresholdInPence()));
        return new HttpEntity<>(httpHeaders);
    }

    private String nullSafeDateFormat(LocalDate dateToFormat) {
        if (dateToFormat != null) {
            return ISO_LOCAL_DATE.format(dateToFormat);
        }
        return null;
    }

    private void addHeaderIfNotNull(HttpHeaders httpHeaders, HeaderName headerName, String headerValue) {
        if (headerValue != null) {
            httpHeaders.set(headerName.getHeader(), headerValue);
        }
    }
}

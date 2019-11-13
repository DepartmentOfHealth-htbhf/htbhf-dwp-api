package uk.gov.dhsc.htbhf.dwp.converter.v2;

import org.springframework.web.context.request.NativeWebRequest;
import uk.gov.dhsc.htbhf.dwp.http.v2.HeaderName;
import uk.gov.dhsc.htbhf.dwp.model.v2.DWPEligibilityRequestV2;
import uk.gov.dhsc.htbhf.dwp.model.v2.PersonDTOV2;

import static uk.gov.dhsc.htbhf.dwp.converter.v2.ConverterUtils.nullSafeGetDate;
import static uk.gov.dhsc.htbhf.dwp.converter.v2.ConverterUtils.nullSafeGetInteger;
import static uk.gov.dhsc.htbhf.dwp.http.v2.HeaderName.*;

/**
 * Converts the HTTP headers in the web request given as a part of the DWP Eligibility Request into a DTO object.
 */
public class RequestHeaderToDWPEligibilityRequestV2Converter {

    public DWPEligibilityRequestV2 convert(NativeWebRequest webRequest) {
        return DWPEligibilityRequestV2.builder()
                .person(buildPerson(webRequest))
                .eligibilityEndDate(nullSafeGetDate(webRequest, ELIGIBILITY_END_DATE))
                .ucMonthlyIncomeThresholdInPence(nullSafeGetInteger(webRequest, UC_MONTHLY_INCOME_THRESHOLD))
                .build();
    }

    private PersonDTOV2 buildPerson(NativeWebRequest webRequest) {
        return PersonDTOV2.builder()
                .surname(getHeader(webRequest, SURNAME))
                .nino(getHeader(webRequest, NINO))
                .dateOfBirth(nullSafeGetDate(webRequest, DATE_OF_BIRTH))
                .addressLine1(getHeader(webRequest, ADDRESS_LINE_1))
                .postcode(getHeader(webRequest, POSTCODE))
                .emailAddress(getHeader(webRequest, EMAIL_ADDRESS))
                .mobilePhoneNumber(getHeader(webRequest, MOBILE_PHONE_NUMBER))
                .pregnantDependentDob(nullSafeGetDate(webRequest, PREGNANT_DEPENDENT_DOB))
                .build();
    }

    private String getHeader(NativeWebRequest webRequest, HeaderName headerName) {
        return webRequest.getHeader(headerName.getHeader());
    }

}

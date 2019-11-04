package uk.gov.dhsc.htbhf.dwp.converter.v2;

import org.springframework.web.context.request.NativeWebRequest;
import uk.gov.dhsc.htbhf.dwp.model.v2.DWPEligibilityRequestV2;
import uk.gov.dhsc.htbhf.dwp.model.v2.PersonDTOV2;

import static uk.gov.dhsc.htbhf.dwp.converter.v2.ConverterUtils.nullSafeGetDate;
import static uk.gov.dhsc.htbhf.dwp.converter.v2.ConverterUtils.nullSafeGetInteger;

/**
 * Converts the HTTP headers in the web request given as a part of the DWP Eligibility Request into a DTO object.
 */
public class RequestHeaderToDWPEligibilityRequestV2Converter {

    public DWPEligibilityRequestV2 convert(NativeWebRequest webRequest) {
        return DWPEligibilityRequestV2.builder()
                .person(buildPerson(webRequest))
                .eligibilityEndDate(nullSafeGetDate(webRequest, "eligibilityEndDate"))
                .ucMonthlyIncomeThresholdInPence(nullSafeGetInteger(webRequest, "ucMonthlyIncomeThreshold"))
                .build();
    }

    private PersonDTOV2 buildPerson(NativeWebRequest webRequest) {
        return PersonDTOV2.builder()
                .surname(webRequest.getHeader("surname"))
                .nino(webRequest.getHeader("nino"))
                .dateOfBirth(nullSafeGetDate(webRequest, "dateOfBirth"))
                .addressLine1(webRequest.getHeader("addressLine1"))
                .postcode(webRequest.getHeader("postcode"))
                .emailAddress(webRequest.getHeader("emailAddress"))
                .mobilePhoneNumber(webRequest.getHeader("mobilePhoneNumber"))
                .pregnantDependentDob(nullSafeGetDate(webRequest, "pregnantDependentDob"))
                .build();
    }

}

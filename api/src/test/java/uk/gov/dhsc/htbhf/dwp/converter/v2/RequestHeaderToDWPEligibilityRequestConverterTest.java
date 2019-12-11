package uk.gov.dhsc.htbhf.dwp.converter.v2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.NativeWebRequest;
import uk.gov.dhsc.htbhf.dwp.model.DWPEligibilityRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static uk.gov.dhsc.htbhf.TestConstants.*;
import static uk.gov.dhsc.htbhf.dwp.testhelper.DWPEligibilityRequestTestDataFactory.aValidDWPEligibilityRequest;

@ExtendWith(MockitoExtension.class)
class RequestHeaderToDWPEligibilityRequestConverterTest {

    private RequestHeaderToDWPEligibilityRequestConverter converter = new RequestHeaderToDWPEligibilityRequestConverter();

    @Mock
    private NativeWebRequest nativeWebRequest;

    @Test
    void shouldConvert() {
        //Given
        LocalDate eligibilityDate = LocalDate.now().plusDays(28);
        String eligibilityDateString = eligibilityDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        lenient().when(nativeWebRequest.getHeader("eligibilityEndDate")).thenReturn(eligibilityDateString);
        lenient().when(nativeWebRequest.getHeader("ucMonthlyIncomeThreshold")).thenReturn(String.valueOf(UC_MONTHLY_INCOME_THRESHOLD_IN_PENCE));
        lenient().when(nativeWebRequest.getHeader("surname")).thenReturn(SIMPSON_SURNAME);
        lenient().when(nativeWebRequest.getHeader("nino")).thenReturn(HOMER_NINO);
        lenient().when(nativeWebRequest.getHeader("dateOfBirth")).thenReturn(HOMER_DATE_OF_BIRTH_STRING);
        lenient().when(nativeWebRequest.getHeader("addressLine1")).thenReturn(SIMPSONS_ADDRESS_LINE_1);
        lenient().when(nativeWebRequest.getHeader("postcode")).thenReturn(SIMPSONS_POSTCODE);
        lenient().when(nativeWebRequest.getHeader("emailAddress")).thenReturn(HOMER_EMAIL);
        lenient().when(nativeWebRequest.getHeader("mobilePhoneNumber")).thenReturn(HOMER_MOBILE);
        lenient().when(nativeWebRequest.getHeader("pregnantDependentDob")).thenReturn(MAGGIE_DATE_OF_BIRTH_STRING);
        //When
        DWPEligibilityRequest request = converter.convert(nativeWebRequest);
        //Then
        assertThat(request).isEqualTo(aValidDWPEligibilityRequest());
    }

}

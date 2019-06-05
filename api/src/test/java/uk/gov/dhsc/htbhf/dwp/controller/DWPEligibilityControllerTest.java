package uk.gov.dhsc.htbhf.dwp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.dhsc.htbhf.dwp.converter.EligibilityRequestToDWPEligibilityRequestConverter;
import uk.gov.dhsc.htbhf.dwp.model.DWPEligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.service.EligibilityService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static uk.gov.dhsc.htbhf.dwp.testhelper.DWPEligibilityRequestTestDataFactory.aValidDWPEligibilityRequest;
import static uk.gov.dhsc.htbhf.dwp.testhelper.EligibilityRequestTestDataFactory.anEligibilityRequest;
import static uk.gov.dhsc.htbhf.dwp.testhelper.EligibilityResponseTestDataFactory.aValidUCEligibilityResponse;

@ExtendWith(MockitoExtension.class)
class DWPEligibilityControllerTest {

    @InjectMocks
    private DWPEligibilityController controller;

    @Mock
    private EligibilityService eligibilityService;

    @Mock
    private EligibilityRequestToDWPEligibilityRequestConverter converter;

    @Test
    void shouldReturnEligibilityResponse() {
        EligibilityRequest eligibilityRequest = anEligibilityRequest();
        DWPEligibilityRequest dwpEligibilityRequest = aValidDWPEligibilityRequest();
        given(converter.convert(any())).willReturn(dwpEligibilityRequest);
        given(eligibilityService.checkEligibility(any())).willReturn(aValidUCEligibilityResponse());

        EligibilityResponse response = controller.getBenefits(eligibilityRequest);

        assertThat(response).isEqualTo(aValidUCEligibilityResponse());
        verify(converter).convert(eligibilityRequest);
        verify(eligibilityService).checkEligibility(dwpEligibilityRequest);
    }
}

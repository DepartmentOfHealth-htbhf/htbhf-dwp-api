package uk.gov.dhsc.htbhf.dwp.controller.v1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.dhsc.htbhf.dwp.converter.v1.EligibilityRequestToDWPEligibilityRequestConverter;
import uk.gov.dhsc.htbhf.dwp.model.v1.DWPEligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.v1.EligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.v1.EligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.service.v1.EligibilityService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v1.DWPEligibilityRequestTestDataFactory.aValidDWPEligibilityRequest;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v1.EligibilityRequestTestDataFactory.anEligibilityRequest;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v1.EligibilityResponseTestDataFactory.aValidUCEligibilityResponse;

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

package uk.gov.dhsc.htbhf.dwp.controller.v2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.dhsc.htbhf.dwp.model.DWPEligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.IdentityAndEligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.service.v2.IdentityAndEligibilityService;
import uk.gov.dhsc.htbhf.dwp.testhelper.DWPEligibilityRequestTestDataFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static uk.gov.dhsc.htbhf.dwp.testhelper.IdAndEligibilityResponseTestDataFactory.anIdMatchedEligibilityConfirmedUCResponseWithAllMatches;

@ExtendWith(MockitoExtension.class)
class DWPBenefitControllerV2Test {

    @Mock
    private IdentityAndEligibilityService service;
    @InjectMocks
    private DWPEligibilityControllerV2 controller;

    @Test
    void shouldReturnResponse() {
        //Given
        IdentityAndEligibilityResponse response = anIdMatchedEligibilityConfirmedUCResponseWithAllMatches();
        DWPEligibilityRequest request = DWPEligibilityRequestTestDataFactory.aValidDWPEligibilityRequest();
        given(service.checkIdentityAndEligibility(any())).willReturn(response);

        //When
        IdentityAndEligibilityResponse controllerResponse = controller.getBenefits(request);

        //Then
        assertThat(controllerResponse).isEqualTo(response);
        verify(service).checkIdentityAndEligibility(request);
    }

}

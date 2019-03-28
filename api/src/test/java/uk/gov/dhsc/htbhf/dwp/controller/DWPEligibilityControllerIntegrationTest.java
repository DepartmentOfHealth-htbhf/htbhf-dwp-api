package uk.gov.dhsc.htbhf.dwp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.dhsc.htbhf.dwp.converter.EligibilityRequestToDWPEligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.DWPEligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;
import uk.gov.dhsc.htbhf.dwp.service.EligibilityService;
import uk.gov.dhsc.htbhf.errorhandler.ErrorResponse;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.OK;
import static uk.gov.dhsc.htbhf.assertions.IntegrationTestAssertions.assertValidationErrorInResponse;
import static uk.gov.dhsc.htbhf.dwp.factory.DWPEligibilityRequestTestDataFactory.aValidDWPEligibilityRequest;
import static uk.gov.dhsc.htbhf.dwp.factory.EligibilityRequestTestDataFactory.anEligibilityRequestWithPerson;
import static uk.gov.dhsc.htbhf.dwp.factory.PersonDTOTestDataFactory.aPersonWithNino;
import static uk.gov.dhsc.htbhf.dwp.helper.EligibilityRequestTestFactory.anEligibilityRequest;
import static uk.gov.dhsc.htbhf.dwp.helper.EligibilityResponseTestFactory.anEligibilityResponse;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DWPEligibilityControllerIntegrationTest {

    private static final URI ENDPOINT = URI.create("/v1/dwp/eligibility");

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private EligibilityService eligibilityService;

    @MockBean
    private EligibilityRequestToDWPEligibilityRequest converter;

    @Test
    void shouldReturnEligibilityResponse() {
        EligibilityRequest eligibilityRequest = anEligibilityRequest();
        DWPEligibilityRequest dwpEligibilityRequest = aValidDWPEligibilityRequest();
        given(converter.convert(any())).willReturn(dwpEligibilityRequest);
        given(eligibilityService.checkEligibility(any())).willReturn(anEligibilityResponse());

        ResponseEntity<EligibilityResponse> response = restTemplate.postForEntity(ENDPOINT, eligibilityRequest, EligibilityResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo(anEligibilityResponse());
        verify(converter).convert(eligibilityRequest);
        verify(eligibilityService).checkEligibility(dwpEligibilityRequest);
    }

    @Test
    void shouldReturnBadRequestForInvalidRequest() {
        PersonDTO person = aPersonWithNino(null);
        EligibilityRequest request = anEligibilityRequestWithPerson(person);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(ENDPOINT, request, ErrorResponse.class);

        assertValidationErrorInResponse(response, "person.nino", "must not be null");
    }

}

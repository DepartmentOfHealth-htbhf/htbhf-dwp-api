package uk.gov.dhsc.htbhf.dwp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;
import uk.gov.dhsc.htbhf.dwp.service.EligibilityService;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static uk.gov.dhsc.htbhf.dwp.helper.EligibilityRequestTestFactory.*;
import static uk.gov.dhsc.htbhf.dwp.helper.EligibilityResponseTestFactory.anEligibilityResponse;
import static uk.gov.dhsc.htbhf.dwp.helper.PersonTestFactory.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DWPEligibilityControllerIntegrationTest {

    private static final URI ENDPOINT = URI.create("/v1/dwp/eligibility");

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private EligibilityService eligibilityService;

    @Test
    void shouldReturnEligibilityResponse() {
        EligibilityRequest eligibilityRequest = anEligibilityRequest();
        given(eligibilityService.checkEligibility(any())).willReturn(anEligibilityResponse());

        ResponseEntity<EligibilityResponse> response = restTemplate.postForEntity(ENDPOINT, eligibilityRequest, EligibilityResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo(anEligibilityResponse());
        verify(eligibilityService).checkEligibility(eligibilityRequest);
    }

    @Test
    void shouldReturnBadRequestForMissingNino() {
        PersonDTO person = aPersonWithNoNino();
        EligibilityRequest request = buildDefaultRequest().person(person).build();

        var benefit = restTemplate.postForEntity(ENDPOINT, request, EligibilityResponse.class);

        assertThat(benefit.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldReturnBadRequestForInvalidNino() {
        PersonDTO person = aPersonWithAnInvalidNino();
        EligibilityRequest request = buildDefaultRequest().person(person).build();

        var benefit = restTemplate.postForEntity(ENDPOINT, request, EligibilityResponse.class);

        assertThat(benefit.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldReturnBadRequestForMissingDateOfBirth() {
        PersonDTO person = aPersonWithNoDateOfBirth();
        EligibilityRequest request = buildDefaultRequest().person(person).build();

        var benefit = restTemplate.postForEntity(ENDPOINT, request, EligibilityResponse.class);

        assertThat(benefit.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldReturnBadRequestForMissingAddress() {
        PersonDTO person = aPersonWithNoAddress();
        EligibilityRequest request = buildDefaultRequest().person(person).build();

        var benefit = restTemplate.postForEntity(ENDPOINT, request, EligibilityResponse.class);

        assertThat(benefit.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldReturnBadRequestForMissingPerson() {
        EligibilityRequest request = buildDefaultRequest().person(null).build();

        var benefit = restTemplate.postForEntity(ENDPOINT, request, EligibilityResponse.class);

        assertThat(benefit.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldReturnBadRequestForMissingIncomeThreshold() {
        EligibilityRequest request = buildDefaultRequest().ucMonthlyIncomeThreshold(null).build();

        var benefit = restTemplate.postForEntity(ENDPOINT, request, EligibilityResponse.class);

        assertThat(benefit.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldReturnBadRequestForMissingStartDate() {
        EligibilityRequest request = buildDefaultRequest().eligibleStartDate(null).build();

        var benefit = restTemplate.postForEntity(ENDPOINT, request, EligibilityResponse.class);

        assertThat(benefit.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldReturnBadRequestForMissingEndDate() {
        EligibilityRequest request = buildDefaultRequest().eligibleEndDate(null).build();

        var benefit = restTemplate.postForEntity(ENDPOINT, request, EligibilityResponse.class);

        assertThat(benefit.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

}

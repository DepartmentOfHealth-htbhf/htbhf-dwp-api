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
import uk.gov.dhsc.htbhf.errorhandler.ErrorResponse;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static uk.gov.dhsc.htbhf.dwp.helper.EligibilityRequestTestFactory.anEligibilityRequest;
import static uk.gov.dhsc.htbhf.dwp.helper.EligibilityRequestTestFactory.buildDefaultRequest;
import static uk.gov.dhsc.htbhf.dwp.helper.EligibilityResponseTestFactory.anEligibilityResponse;
import static uk.gov.dhsc.htbhf.dwp.helper.PersonTestFactory.aPersonWithAnInvalidNino;
import static uk.gov.dhsc.htbhf.dwp.helper.PersonTestFactory.aPersonWithNoAddress;
import static uk.gov.dhsc.htbhf.dwp.helper.PersonTestFactory.aPersonWithNoDateOfBirth;
import static uk.gov.dhsc.htbhf.dwp.helper.PersonTestFactory.aPersonWithNoNino;

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

        ResponseEntity<EligibilityResponse> benefit = restTemplate.postForEntity(ENDPOINT, eligibilityRequest, EligibilityResponse.class);

        assertThat(benefit.getStatusCode()).isEqualTo(OK);
        assertThat(benefit.getBody()).isEqualTo(anEligibilityResponse());
        verify(eligibilityService).checkEligibility(eligibilityRequest);
    }

    @Test
    void shouldReturnBadRequestForMissingNino() {
        PersonDTO person = aPersonWithNoNino();
        EligibilityRequest request = buildDefaultRequest().person(person).build();

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(ENDPOINT, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertValidationError(response, "person.nino", "must not be null");
    }

    @Test
    void shouldReturnBadRequestForInvalidNino() {
        PersonDTO person = aPersonWithAnInvalidNino();
        EligibilityRequest request = buildDefaultRequest().person(person).build();

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(ENDPOINT, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertValidationError(response, "person.nino", "must match \"[a-zA-Z]{2}\\d{6}[a-dA-D]\"");
    }

    @Test
    void shouldReturnBadRequestForMissingDateOfBirth() {
        PersonDTO person = aPersonWithNoDateOfBirth();
        EligibilityRequest request = buildDefaultRequest().person(person).build();

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(ENDPOINT, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertValidationError(response, "person.dateOfBirth", "must not be null");
    }

    @Test
    void shouldReturnBadRequestForMissingAddress() {
        PersonDTO person = aPersonWithNoAddress();
        EligibilityRequest request = buildDefaultRequest().person(person).build();

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(ENDPOINT, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertValidationError(response, "person.address", "must not be null");
    }

    @Test
    void shouldReturnBadRequestForMissingPerson() {
        EligibilityRequest request = buildDefaultRequest().person(null).build();

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(ENDPOINT, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertValidationError(response, "person", "must not be null");
    }

    @Test
    void shouldReturnBadRequestForMissingIncomeThreshold() {
        EligibilityRequest request = buildDefaultRequest().ucMonthlyIncomeThreshold(null).build();

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(ENDPOINT, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertValidationError(response, "ucMonthlyIncomeThreshold", "must not be null");
    }

    @Test
    void shouldReturnBadRequestForMissingStartDate() {
        EligibilityRequest request = buildDefaultRequest().eligibleStartDate(null).build();

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(ENDPOINT, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertValidationError(response, "eligibleStartDate", "must not be null");
    }

    @Test
    void shouldReturnBadRequestForMissingEndDate() {
        EligibilityRequest request = buildDefaultRequest().eligibleEndDate(null).build();

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(ENDPOINT, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertValidationError(response, "eligibleEndDate", "must not be null");
    }

    private void assertValidationError(ResponseEntity<ErrorResponse> response, String field, String errorMessage) {
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFieldErrors()).hasSize(1);
        assertThat(response.getBody().getFieldErrors().get(0).getField()).isEqualTo(field);
        assertThat(response.getBody().getFieldErrors().get(0).getMessage()).isEqualTo(errorMessage);
        assertThat(response.getBody().getRequestId()).isNotNull();
        assertThat(response.getBody().getTimestamp()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("There were validation issues with the request.");
    }

}

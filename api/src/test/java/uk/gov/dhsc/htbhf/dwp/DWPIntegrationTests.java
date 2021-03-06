package uk.gov.dhsc.htbhf.dwp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.dhsc.htbhf.dwp.model.IdentityAndEligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.model.VerificationOutcome;
import uk.gov.dhsc.htbhf.dwp.repository.UCHouseholdRepository;
import uk.gov.dhsc.htbhf.errorhandler.ErrorResponse;

import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static uk.gov.dhsc.htbhf.TestConstants.HOMER_NINO;
import static uk.gov.dhsc.htbhf.TestConstants.MAGGIE_AND_LISA_DOBS;
import static uk.gov.dhsc.htbhf.assertions.IntegrationTestAssertions.assertValidationErrorInResponse;
import static uk.gov.dhsc.htbhf.dwp.testhelper.HttpRequestTestDataFactory.aValidEligibilityHttpEntity;
import static uk.gov.dhsc.htbhf.dwp.testhelper.HttpRequestTestDataFactory.anEligibilityHttpEntityWithNinoAndSurname;
import static uk.gov.dhsc.htbhf.dwp.testhelper.HttpRequestTestDataFactory.anInvalidEligibilityHttpEntity;
import static uk.gov.dhsc.htbhf.dwp.testhelper.IdAndEligibilityResponseTestDataFactory.anIdMatchFailedResponse;
import static uk.gov.dhsc.htbhf.dwp.testhelper.IdAndEligibilityResponseTestDataFactory.anIdMatchedEligibilityConfirmedUCResponseWithAllMatches;
import static uk.gov.dhsc.htbhf.dwp.testhelper.UCHouseholdTestDataFactory.aUCHousehold;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureEmbeddedDatabase
@AutoConfigureWireMock(port = 8120)
public class DWPIntegrationTests {

    private static final String DOB_OF_CHILDREN_FIELD_NAME = "dobOfChildrenUnder4";

    private static final URI ENDPOINT = URI.create("/v2/dwp/eligibility");

    private static final String DWP_URL = "/v2/dwp/benefits";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UCHouseholdRepository ucHouseholdRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void clearDatabase() {
        ucHouseholdRepository.deleteAll();
        WireMock.reset();
    }

    @Test
    void shouldReturnStubResponseWhenUserNotInDatabase() throws JsonProcessingException {
        //Given
        IdentityAndEligibilityResponse response = anIdMatchedEligibilityConfirmedUCResponseWithAllMatches();
        stubDWPEndpointWithSuccessfulResponse(response);
        HttpEntity request = aValidEligibilityHttpEntity();

        //When
        ResponseEntity<IdentityAndEligibilityResponse> responseEntity = restTemplate.exchange(ENDPOINT,
                HttpMethod.GET, request, IdentityAndEligibilityResponse.class);

        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(response);
        verifyCallToDWP();
    }

    @Test
    void shouldReturnBadRequestResponseWithInvalidRequest() {
        //Given
        HttpEntity request = anInvalidEligibilityHttpEntity();

        //When
        ResponseEntity<ErrorResponse> responseEntity = restTemplate.exchange(ENDPOINT, HttpMethod.GET, request, ErrorResponse.class);

        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertValidationErrorInResponse(responseEntity, "person.nino",
                "must match \"^(?!BG|GB|NK|KN|TN|NT|ZZ)[A-CEGHJ-PR-TW-Z][A-CEGHJ-NPR-TW-Z](\\d{6})[A-D]$\"");
        verifyNoCallToDWP();
    }

    @Test
    void shouldReturnSuccessfulResponseWhenUserInDatabaseAndEligible() {
        //Given
        ucHouseholdRepository.save(aUCHousehold());
        HttpEntity request = aValidEligibilityHttpEntity();

        //When
        ResponseEntity<IdentityAndEligibilityResponse> responseEntity = restTemplate.exchange(ENDPOINT,
                HttpMethod.GET, request, IdentityAndEligibilityResponse.class);

        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertResponseBodyMatches(responseEntity, anIdMatchedEligibilityConfirmedUCResponseWithAllMatches(VerificationOutcome.NOT_SET,
                MAGGIE_AND_LISA_DOBS));
        verifyNoCallToDWP();
    }

    @Test
    void shouldReturnSuccessfulResponseWhenUserInDatabaseAndNotEligible() {
        //Given
        ucHouseholdRepository.save(aUCHousehold());
        HttpEntity request = anEligibilityHttpEntityWithNinoAndSurname(HOMER_NINO, "Doe");

        //When
        ResponseEntity<IdentityAndEligibilityResponse> responseEntity = restTemplate.exchange(ENDPOINT,
                HttpMethod.GET, request, IdentityAndEligibilityResponse.class);

        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertResponseBodyMatches(responseEntity, anIdMatchFailedResponse());
        verifyNoCallToDWP();
    }

    private void stubDWPEndpointWithSuccessfulResponse(IdentityAndEligibilityResponse response) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(response);
        stubFor(get(urlEqualTo(DWP_URL)).willReturn(okJson(json)));
    }

    private void verifyNoCallToDWP() {
        verify(exactly(0), getRequestedFor(urlEqualTo(DWP_URL)));
    }

    private void verifyCallToDWP() {
        verify(exactly(1), getRequestedFor(urlEqualTo(DWP_URL)));
    }

    private void assertResponseBodyMatches(ResponseEntity<IdentityAndEligibilityResponse> responseEntity, IdentityAndEligibilityResponse expectedResponse) {
        IdentityAndEligibilityResponse serviceResponse = responseEntity.getBody();
        assertThat(serviceResponse).isNotNull();
        assertThat(serviceResponse).isEqualToIgnoringGivenFields(expectedResponse, DOB_OF_CHILDREN_FIELD_NAME);
        assertThat(serviceResponse.getDobOfChildrenUnder4()).containsExactlyInAnyOrderElementsOf(expectedResponse.getDobOfChildrenUnder4());
    }

}

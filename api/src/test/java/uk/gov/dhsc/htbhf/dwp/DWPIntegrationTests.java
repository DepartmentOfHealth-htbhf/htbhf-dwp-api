package uk.gov.dhsc.htbhf.dwp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.dhsc.htbhf.dwp.model.ChildDTO;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;
import uk.gov.dhsc.htbhf.dwp.repository.LegacyHouseholdRepository;
import uk.gov.dhsc.htbhf.dwp.repository.UCHouseholdRepository;
import uk.gov.dhsc.htbhf.eligibility.model.EligibilityStatus;
import uk.gov.dhsc.htbhf.errorhandler.ErrorResponse;

import java.net.URI;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.http.HttpStatus.OK;
import static uk.gov.dhsc.htbhf.assertions.IntegrationTestAssertions.assertValidationErrorInResponse;
import static uk.gov.dhsc.htbhf.dwp.testhelper.EligibilityRequestTestDataFactory.aValidEligibilityRequest;
import static uk.gov.dhsc.htbhf.dwp.testhelper.EligibilityRequestTestDataFactory.anEligibilityRequestWithPerson;
import static uk.gov.dhsc.htbhf.dwp.testhelper.EligibilityResponseTestDataFactory.aValidUCEligibilityResponse;
import static uk.gov.dhsc.htbhf.dwp.testhelper.EligibilityResponseTestDataFactory.aValidUCEligibilityResponseBuilder;
import static uk.gov.dhsc.htbhf.dwp.testhelper.LegacyHouseholdTestDataFactory.aLegacyHousehold;
import static uk.gov.dhsc.htbhf.dwp.testhelper.PersonDTOTestDataFactory.aPersonWithNino;
import static uk.gov.dhsc.htbhf.dwp.testhelper.PersonDTOTestDataFactory.buildDefaultPerson;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.HOMER_NINO;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.SIMPSON_LEGACY_HOUSEHOLD_IDENTIFIER;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.SIMPSON_UC_HOUSEHOLD_IDENTIFIER;
import static uk.gov.dhsc.htbhf.dwp.testhelper.UCHouseholdTestDataFactory.aUCHousehold;
import static uk.gov.dhsc.htbhf.eligibility.model.EligibilityStatus.ELIGIBLE;
import static uk.gov.dhsc.htbhf.eligibility.model.EligibilityStatus.NO_MATCH;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 8120)
public class DWPIntegrationTests {

    private static final URI ENDPOINT = URI.create("/v1/dwp/eligibility");

    private static final String DWP_URL = "/v1/dwp/benefits";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LegacyHouseholdRepository legacyHouseholdRepository;

    @Autowired
    private UCHouseholdRepository ucHouseholdRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void clearDatabase() {
        ucHouseholdRepository.deleteAll();
        legacyHouseholdRepository.deleteAll();
        WireMock.reset();
    }

    @Test
    void shouldReturnEligibilityResponseWhenNotInDatabase() throws JsonProcessingException {
        //Given
        EligibilityResponse dwpEligibilityResponse = aValidUCEligibilityResponse();
        stubDWPEndpointWithSuccessfulResponse(dwpEligibilityResponse);

        //When
        ResponseEntity<EligibilityResponse> response = callService(aValidEligibilityRequest());

        //Then
        assertResponseCorrectWithHouseholdDetails(response, SIMPSON_UC_HOUSEHOLD_IDENTIFIER, ELIGIBLE);
        verify(exactly(1), postRequestedFor(urlEqualTo(DWP_URL)));
    }

    @ParameterizedTest(name = "Should return eligible response for claimant [{0}] Simpson stored in UC household table")
    @CsvSource({"Homer, EB123456C",
            "Marge, EB123456D"})
    void shouldReturnEligibleWhenMatchesUCHouseholdInDatabase(String parentName, String nino) {
        //Given
        ucHouseholdRepository.save(aUCHousehold());
        PersonDTO person = buildDefaultPerson().firstName(parentName).nino(nino).build();
        EligibilityRequest eligibilityRequest = anEligibilityRequestWithPerson(person);

        //When
        ResponseEntity<EligibilityResponse> response = callService(eligibilityRequest);

        //Then
        assertResponseCorrectWithHouseholdDetails(response, SIMPSON_UC_HOUSEHOLD_IDENTIFIER, ELIGIBLE);
        verifyNoCallToDWP();
    }

    @Test
    void shouldReturnNoMatchWhenMatchesNinoButNotNameInUCDatabase() {
        //Given
        ucHouseholdRepository.save(aUCHousehold());
        PersonDTO person = buildDefaultPerson().lastName("noMatch").nino(HOMER_NINO).build();
        EligibilityRequest eligibilityRequest = anEligibilityRequestWithPerson(person);

        //When
        ResponseEntity<EligibilityResponse> response = callService(eligibilityRequest);

        //Then
        assertResponseCorrectWithStatusOnly(response, NO_MATCH);
        verifyNoCallToDWP();
    }

    @ParameterizedTest(name = "Should return eligible response for claimant [{0}] Simpson stored in Legacy household table")
    @CsvSource({"Homer, EB123456C",
            "Marge, EB123456D"})
    void shouldReturnEligibleWhenMatchesLegacyHouseholdInDatabase(String parentName, String nino) {
        //Given
        legacyHouseholdRepository.save(aLegacyHousehold());
        PersonDTO homer = buildDefaultPerson().firstName(parentName).nino(nino).build();
        EligibilityRequest eligibilityRequest = anEligibilityRequestWithPerson(homer);

        //When
        ResponseEntity<EligibilityResponse> response = callService(eligibilityRequest);

        //Then
        assertResponseCorrectWithHouseholdDetails(response, SIMPSON_LEGACY_HOUSEHOLD_IDENTIFIER, ELIGIBLE);
        verifyNoCallToDWP();
    }

    @Test
    void shouldReturnNoMatchWhenMatchesNinoButNotNameInLegacyDatabase() {
        //Given
        legacyHouseholdRepository.save(aLegacyHousehold());
        PersonDTO homer = buildDefaultPerson().lastName("noMatch").nino(HOMER_NINO).build();
        EligibilityRequest eligibilityRequest = anEligibilityRequestWithPerson(homer);

        //When
        ResponseEntity<EligibilityResponse> response = callService(eligibilityRequest);

        //Then
        assertResponseCorrectWithStatusOnly(response, NO_MATCH);
        verifyNoCallToDWP();
        legacyHouseholdRepository.deleteAll();
    }

    @Test
    void shouldReturnBadRequestForInvalidRequest() {
        PersonDTO person = aPersonWithNino(null);
        EligibilityRequest request = anEligibilityRequestWithPerson(person);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(ENDPOINT, request, ErrorResponse.class);

        assertValidationErrorInResponse(response, "person.nino", "must not be null");
    }

    private void stubDWPEndpointWithSuccessfulResponse(EligibilityResponse eligibilityResponse) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(eligibilityResponse);
        stubFor(post(urlEqualTo(DWP_URL)).willReturn(okJson(json)));
    }

    private ResponseEntity<EligibilityResponse> callService(EligibilityRequest eligibilityRequest) {
        return restTemplate.exchange(buildRequestEntity(eligibilityRequest), EligibilityResponse.class);
    }

    private void assertResponseCorrectWithHouseholdDetails(ResponseEntity<EligibilityResponse> response, String householdIdentifier, EligibilityStatus status) {
        EligibilityResponse expectedResponse = aValidUCEligibilityResponseBuilder()
                .householdIdentifier(householdIdentifier)
                .eligibilityStatus(status)
                .build();
        assertResponseCorrect(response, expectedResponse);
    }

    private void assertResponseCorrectWithStatusOnly(ResponseEntity<EligibilityResponse> response, EligibilityStatus status) {
        EligibilityResponse expectedResponse = EligibilityResponse.builder()
                .eligibilityStatus(status)
                .build();
        assertResponseCorrect(response, expectedResponse);
    }

    private void assertResponseCorrect(ResponseEntity<EligibilityResponse> response, EligibilityResponse expectedResponse) {
        assertThat(response.getStatusCode()).isEqualTo(OK);
        EligibilityResponse eligibilityResponse = response.getBody();
        assertThat(eligibilityResponse).isNotNull();
        assertEligibilityResponse(expectedResponse, eligibilityResponse);
    }

    // checks that two eligibility responses are equal whilst ignoring the order of the children
    private void assertEligibilityResponse(EligibilityResponse expectedResponse, EligibilityResponse eligibilityResponse) {
        assertThat(eligibilityResponse).isEqualToIgnoringGivenFields(expectedResponse, "children");
        assertChildren(eligibilityResponse.getChildren(), expectedResponse.getChildren());
    }

    private void assertChildren(List<ChildDTO> expected, List<ChildDTO> actual) {
        if (expected == null) {
            assertThat(actual).isNull();
        } else {
            assertThat(expected).containsOnlyElementsOf(actual);
        }
    }

    private RequestEntity buildRequestEntity(Object requestObject) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new RequestEntity<>(requestObject, headers, HttpMethod.POST, ENDPOINT);
    }

    private void verifyNoCallToDWP() {
        verify(exactly(0), postRequestedFor(urlEqualTo(DWP_URL)));
    }

}

package uk.gov.dhsc.htbhf.dwp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;
import uk.gov.dhsc.htbhf.dwp.repository.LegacyHouseholdRepository;
import uk.gov.dhsc.htbhf.dwp.repository.UCHouseholdRepository;
import uk.gov.dhsc.htbhf.eligibility.model.EligibilityStatus;

import java.net.URI;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.http.HttpStatus.OK;
import static uk.gov.dhsc.htbhf.dwp.testhelper.DWPEligibilityRequestTestDataFactory.aValidDWPEligibilityRequest;
import static uk.gov.dhsc.htbhf.dwp.testhelper.EligibilityRequestTestDataFactory.aValidEligibilityRequest;
import static uk.gov.dhsc.htbhf.dwp.testhelper.EligibilityRequestTestDataFactory.anEligibilityRequestWithPerson;
import static uk.gov.dhsc.htbhf.dwp.testhelper.EligibilityResponseTestDataFactory.aValidUCEligibilityResponse;
import static uk.gov.dhsc.htbhf.dwp.testhelper.EligibilityResponseTestDataFactory.aValidUCEligibilityResponseBuilder;
import static uk.gov.dhsc.htbhf.dwp.testhelper.LegacyHouseholdTestDataFactory.aLegacyHousehold;
import static uk.gov.dhsc.htbhf.dwp.testhelper.PersonDTOTestDataFactory.buildDefaultPerson;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.HOMER_NINO;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.SIMPSON_LEGACY_HOUSEHOLD_IDENTIFIER;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.SIMPSON_UC_HOUSEHOLD_IDENTIFIER;
import static uk.gov.dhsc.htbhf.dwp.testhelper.UCHouseholdTestDataFactory.aUCHousehold;
import static uk.gov.dhsc.htbhf.eligibility.model.EligibilityStatus.ELIGIBLE;
import static uk.gov.dhsc.htbhf.eligibility.model.EligibilityStatus.NO_MATCH;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DWPIntegrationTests {

    private static final URI ENDPOINT = URI.create("/v1/dwp/eligibility");

    private static final String DWP_URL = "http://localhost:8120/v1/dwp/benefits";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LegacyHouseholdRepository legacyHouseholdRepository;

    @Autowired
    private UCHouseholdRepository ucHouseholdRepository;

    @MockBean
    private RestTemplate restTemplateWithIdHeaders;

    @BeforeEach
    @AfterEach
    void clearDatabase() {
        ucHouseholdRepository.deleteAll();
        legacyHouseholdRepository.deleteAll();
    }

    @Test
    void shouldReturnEligibilityResponseWhenNotInDatabase() {
        //Given
        ResponseEntity<EligibilityResponse> dwpEligibilityResponse = new ResponseEntity<>(aValidUCEligibilityResponse(), OK);
        given(restTemplateWithIdHeaders.postForEntity(anyString(), any(), eq(EligibilityResponse.class))).willReturn(dwpEligibilityResponse);

        //When
        ResponseEntity<EligibilityResponse> response = callService(aValidEligibilityRequest());

        //Then
        assertResponseCorrectWithHouseholdDetails(response, SIMPSON_UC_HOUSEHOLD_IDENTIFIER, ELIGIBLE);
        verify(restTemplateWithIdHeaders).postForEntity(DWP_URL, aValidDWPEligibilityRequest(), EligibilityResponse.class);
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
        verifyZeroInteractions(restTemplateWithIdHeaders);
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
        verifyZeroInteractions(restTemplateWithIdHeaders);
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
        verifyZeroInteractions(restTemplateWithIdHeaders);
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
        verifyZeroInteractions(restTemplateWithIdHeaders);
        legacyHouseholdRepository.deleteAll();
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
        assertThat(eligibilityResponse).isEqualTo(expectedResponse);
    }

    private RequestEntity buildRequestEntity(Object requestObject) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new RequestEntity<>(requestObject, headers, HttpMethod.POST, ENDPOINT);
    }

}

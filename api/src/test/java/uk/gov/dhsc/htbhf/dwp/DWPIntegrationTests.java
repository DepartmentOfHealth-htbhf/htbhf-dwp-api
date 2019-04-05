package uk.gov.dhsc.htbhf.dwp;

import org.junit.jupiter.api.Disabled;
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
import uk.gov.dhsc.htbhf.dwp.entity.LegacyHouseholdFactory;
import uk.gov.dhsc.htbhf.dwp.entity.UCHouseholdFactory;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityStatus;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;
import uk.gov.dhsc.htbhf.dwp.repository.LegacyHouseholdRepository;
import uk.gov.dhsc.htbhf.dwp.repository.UCHouseholdRepository;

import java.net.URI;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.http.HttpStatus.OK;
import static uk.gov.dhsc.htbhf.dwp.entity.LegacyHouseholdFactory.aLegacyHousehold;
import static uk.gov.dhsc.htbhf.dwp.entity.UCHouseholdFactory.aUCHousehold;
import static uk.gov.dhsc.htbhf.dwp.factory.DWPEligibilityRequestTestDataFactory.aValidDWPEligibilityRequest;
import static uk.gov.dhsc.htbhf.dwp.factory.EligibilityRequestTestDataFactory.aValidEligibilityRequest;
import static uk.gov.dhsc.htbhf.dwp.factory.EligibilityRequestTestDataFactory.anEligibilityRequestWithPerson;
import static uk.gov.dhsc.htbhf.dwp.factory.PersonDTOTestDataFactory.aValidPersonBuilder;
import static uk.gov.dhsc.htbhf.dwp.helper.EligibilityResponseTestFactory.aValidEligibilityResponseBuilder;
import static uk.gov.dhsc.htbhf.dwp.helper.EligibilityResponseTestFactory.anEligibilityResponse;
import static uk.gov.dhsc.htbhf.dwp.model.EligibilityStatus.ELIGIBLE;
import static uk.gov.dhsc.htbhf.dwp.model.EligibilityStatus.NOMATCH;

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

    @Test
    void shouldReturnEligibilityResponseWhenNotInDatabase() {
        //Given
        ResponseEntity<EligibilityResponse> dwpEligibilityResponse = new ResponseEntity<>(anEligibilityResponse(), OK);
        given(restTemplateWithIdHeaders.postForEntity(anyString(), any(), eq(EligibilityResponse.class))).willReturn(dwpEligibilityResponse);

        //When
        ResponseEntity<EligibilityResponse> response = callService(aValidEligibilityRequest());

        //Then
        assertResponseCorrectWithHouseholdDetails(response, "dwpHousehold1", ELIGIBLE);
        verify(restTemplateWithIdHeaders).postForEntity(DWP_URL, aValidDWPEligibilityRequest(), EligibilityResponse.class);
    }

    @Disabled("To be enabled when the code is fixed in the next PR")
    @ParameterizedTest(name = "Should return eligible response for claimant [{0}] Simpson stored in UC household table")
    @CsvSource({"Homer, QQ123456C",
            "Marge, QQ123456D"})
    void shouldReturnEligibleWhenMatchesUCHouseholdInDatabase(String parentName, String nino) {
        //Given
        ucHouseholdRepository.save(aUCHousehold());
        PersonDTO person = aValidPersonBuilder().firstName(parentName).nino(nino).build();
        EligibilityRequest eligibilityRequest = anEligibilityRequestWithPerson(person);

        //When
        ResponseEntity<EligibilityResponse> response = callService(eligibilityRequest);

        //Then
        assertResponseCorrectWithHouseholdDetails(response, UCHouseholdFactory.SIMPSON_HOUSEHOLD_IDENTIFIER, ELIGIBLE);
        verifyZeroInteractions(restTemplateWithIdHeaders);
        ucHouseholdRepository.deleteAll();
    }

    @Test
    void shouldReturnNoMatchWhenMatchesNinoButNotNameInUCDatabase() {
        //Given
        ucHouseholdRepository.save(aUCHousehold());
        PersonDTO person = aValidPersonBuilder().lastName("noMatch").nino(UCHouseholdFactory.HOMER_NINO).build();
        EligibilityRequest eligibilityRequest = anEligibilityRequestWithPerson(person);

        //When
        ResponseEntity<EligibilityResponse> response = callService(eligibilityRequest);

        //Then
        assertResponseCorrectWithStatusOnly(response, NOMATCH);
        verifyZeroInteractions(restTemplateWithIdHeaders);
        ucHouseholdRepository.deleteAll();
    }

    @Disabled("To be enabled when the code is fixed in the next PR")
    @ParameterizedTest(name = "Should return eligible response for claimant [{0}] Simpson stored in Legacy household table")
    @CsvSource({"Homer, QQ123456A",
            "Marge, QQ123456B"})
    void shouldReturnEligibleWhenMatchesLegacyHouseholdInDatabase(String parentName, String nino) {
        //Given
        legacyHouseholdRepository.save(aLegacyHousehold());
        PersonDTO homer = aValidPersonBuilder().firstName(parentName).nino(nino).build();
        EligibilityRequest eligibilityRequest = anEligibilityRequestWithPerson(homer);

        //When
        ResponseEntity<EligibilityResponse> response = callService(eligibilityRequest);

        //Then
        assertResponseCorrectWithHouseholdDetails(response, LegacyHouseholdFactory.SIMPSON_HOUSEHOLD_IDENTIFIER, ELIGIBLE);
        verifyZeroInteractions(restTemplateWithIdHeaders);
        legacyHouseholdRepository.deleteAll();
    }

    @Test
    void shouldReturnNoMatchWhenMatchesNinoButNotNameInLegacyDatabase() {
        //Given
        legacyHouseholdRepository.save(aLegacyHousehold());
        PersonDTO homer = aValidPersonBuilder().lastName("noMatch").nino(LegacyHouseholdFactory.HOMER_NINO).build();
        EligibilityRequest eligibilityRequest = anEligibilityRequestWithPerson(homer);

        //When
        ResponseEntity<EligibilityResponse> response = callService(eligibilityRequest);

        //Then
        assertResponseCorrectWithStatusOnly(response, NOMATCH);
        verifyZeroInteractions(restTemplateWithIdHeaders);
        legacyHouseholdRepository.deleteAll();
    }

    private ResponseEntity<EligibilityResponse> callService(EligibilityRequest eligibilityRequest) {
        return restTemplate.exchange(buildRequestEntity(eligibilityRequest), EligibilityResponse.class);
    }

    private void assertResponseCorrectWithHouseholdDetails(ResponseEntity<EligibilityResponse> response, String householdIdentifier, EligibilityStatus status) {
        EligibilityResponse expectedResponse = aValidEligibilityResponseBuilder()
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

package uk.gov.dhsc.htbhf.dwp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import uk.gov.dhsc.htbhf.dwp.entity.LegacyHouseholdFactory;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.repository.LegacyHouseholdRepository;
import uk.gov.dhsc.htbhf.dwp.repository.UCHouseholdRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.OK;
import static uk.gov.dhsc.htbhf.dwp.entity.UCHouseholdFactory.aHousehold;
import static uk.gov.dhsc.htbhf.dwp.helper.EligibilityRequestTestFactory.anEligibilityRequest;
import static uk.gov.dhsc.htbhf.dwp.helper.EligibilityResponseTestFactory.anEligibilityResponse;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class EligibilityServiceTest {

    private static final String ENDPOINT = "/v1/dwp/benefits";

    @Value("${dwp.base-uri}")
    private String dwpUri;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private UCHouseholdRepository ucHouseholdRepository;

    @MockBean
    private LegacyHouseholdRepository legacyHouseholdRepository;

    @Autowired
    private EligibilityService eligibilityService;

    @Test
    void shouldReturnResponseFromUCDatabaseIfFound() {
        EligibilityRequest eligibilityRequest = anEligibilityRequest();
        UCHousehold household = aHousehold();
        given(ucHouseholdRepository.findHouseholdByAdultWithNino(anyString())).willReturn(Optional.of(household));

        var response = eligibilityService.checkEligibility(eligibilityRequest);

        assertThat(response.getNumberOfChildrenUnderFour()).isEqualTo(2);
        assertThat(response.getNumberOfChildrenUnderOne()).isEqualTo(1);
        assertThat(response.getHouseholdIdentifier()).isEqualTo(household.getHouseholdIdentifier());
        assertThat(response.getEarningsThresholdExceeded()).isEqualTo(household.getEarningsThresholdExceeded());

        verify(ucHouseholdRepository).findHouseholdByAdultWithNino(eligibilityRequest.getPerson().getNino());
        verify(legacyHouseholdRepository, never()).findHouseholdByAdultWithNino(anyString());
        verify(restTemplate, never()).postForEntity(anyString(), any(), any());
    }

    @Test
    void shouldReturnResponseFromLegacyDatabaseIfFound() {
        EligibilityRequest eligibilityRequest = anEligibilityRequest();
        LegacyHousehold household = LegacyHouseholdFactory.aHousehold();
        given(ucHouseholdRepository.findHouseholdByAdultWithNino(anyString())).willReturn(Optional.empty());
        given(legacyHouseholdRepository.findHouseholdByAdultWithNino(anyString())).willReturn(Optional.of(household));

        var response = eligibilityService.checkEligibility(eligibilityRequest);

        assertThat(response.getNumberOfChildrenUnderFour()).isEqualTo(2);
        assertThat(response.getNumberOfChildrenUnderOne()).isEqualTo(1);
        assertThat(response.getHouseholdIdentifier()).isEqualTo(household.getHouseholdIdentifier());

        verify(ucHouseholdRepository).findHouseholdByAdultWithNino(anyString());
        verify(legacyHouseholdRepository).findHouseholdByAdultWithNino(eligibilityRequest.getPerson().getNino());
        verify(restTemplate, never()).postForEntity(anyString(), any(), any());
    }

    @Test
    void shouldCallDWPServiceWhenHouseholdNotFoundInDatabase() {
        EligibilityRequest eligibilityRequest = anEligibilityRequest();
        given(ucHouseholdRepository.findHouseholdByAdultWithNino(anyString())).willReturn(Optional.empty());
        given(legacyHouseholdRepository.findHouseholdByAdultWithNino(anyString())).willReturn(Optional.empty());
        given(restTemplate.postForEntity(anyString(), any(), any()))
                .willReturn(new ResponseEntity<>(anEligibilityResponse(), OK));

        var response = eligibilityService.checkEligibility(eligibilityRequest);

        assertThat(response).isEqualTo(anEligibilityResponse());
        verify(ucHouseholdRepository).findHouseholdByAdultWithNino(eligibilityRequest.getPerson().getNino());
        verify(legacyHouseholdRepository).findHouseholdByAdultWithNino(eligibilityRequest.getPerson().getNino());
        verify(restTemplate).postForEntity(dwpUri + ENDPOINT, eligibilityRequest, EligibilityResponse.class);
    }
}

package uk.gov.dhsc.htbhf.dwp.service.v1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import uk.gov.dhsc.htbhf.dwp.entity.v1.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.factory.v1.EligibilityResponseFactory;
import uk.gov.dhsc.htbhf.dwp.model.v1.DWPEligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.v1.EligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.repository.v1.UCHouseholdRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.http.HttpStatus.OK;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.HOMER_NINO;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v1.DWPEligibilityRequestTestDataFactory.aValidDWPEligibilityRequest;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v1.EligibilityResponseTestDataFactory.aNoMatchEligibilityResponse;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v1.EligibilityResponseTestDataFactory.aValidUCEligibilityResponse;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v1.UCHouseholdTestDataFactory.aUCHousehold;
import static uk.gov.dhsc.htbhf.eligibility.model.EligibilityStatus.ELIGIBLE;

@ExtendWith(MockitoExtension.class)
class EligibilityServiceTest {

    private static final String ENDPOINT = "/v1/dwp/benefits";

    @Value("${dwp.base-uri}")
    private String dwpUri;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UCHouseholdRepository ucHouseholdRepository;

    @Mock
    private HouseholdVerifier householdVerifier;

    @Mock
    private EligibilityResponseFactory eligibilityResponseFactory;

    @InjectMocks
    private EligibilityService eligibilityService;

    @Test
    void shouldReturnResponseFromUCDatabaseIfFound() {
        DWPEligibilityRequest eligibilityRequest = aValidDWPEligibilityRequest();
        UCHousehold household = aUCHousehold();
        given(ucHouseholdRepository.findHouseholdByAdultWithNino(anyString())).willReturn(Optional.of(household));
        given(householdVerifier.detailsMatch(any(UCHousehold.class), any())).willReturn(true);
        given(eligibilityResponseFactory.createEligibilityResponse(any(UCHousehold.class), any()))
                .willReturn(aValidUCEligibilityResponse());

        EligibilityResponse response = eligibilityService.checkEligibility(eligibilityRequest);

        assertThat(response).isEqualTo(aValidUCEligibilityResponse());
        verify(ucHouseholdRepository).findHouseholdByAdultWithNino(HOMER_NINO);
        verify(householdVerifier).detailsMatch(household, eligibilityRequest.getPerson());
        verify(eligibilityResponseFactory).createEligibilityResponse(household, ELIGIBLE);
        verifyZeroInteractions(restTemplate);
    }

    @Test
    void shouldReturnNoMatchWhenFoundInUCDatabaseAndDetailsDontMatch() {
        DWPEligibilityRequest eligibilityRequest = aValidDWPEligibilityRequest();
        UCHousehold household = aUCHousehold();
        given(ucHouseholdRepository.findHouseholdByAdultWithNino(anyString())).willReturn(Optional.of(household));
        given(householdVerifier.detailsMatch(any(UCHousehold.class), any())).willReturn(false);

        EligibilityResponse response = eligibilityService.checkEligibility(eligibilityRequest);

        assertThat(response).isEqualTo(aNoMatchEligibilityResponse());
        verify(ucHouseholdRepository).findHouseholdByAdultWithNino(HOMER_NINO);
        verify(householdVerifier).detailsMatch(household, eligibilityRequest.getPerson());
        verifyZeroInteractions(restTemplate);
    }

    @Test
    void shouldCallDWPServiceWhenHouseholdNotFoundInDatabase() {
        DWPEligibilityRequest eligibilityRequest = aValidDWPEligibilityRequest();
        given(ucHouseholdRepository.findHouseholdByAdultWithNino(anyString())).willReturn(Optional.empty());
        given(restTemplate.postForEntity(anyString(), any(), any()))
                .willReturn(new ResponseEntity<>(aValidUCEligibilityResponse(), OK));

        EligibilityResponse response = eligibilityService.checkEligibility(eligibilityRequest);

        assertThat(response).isEqualTo(aValidUCEligibilityResponse());
        verify(ucHouseholdRepository).findHouseholdByAdultWithNino(HOMER_NINO);
        verify(restTemplate).postForEntity(dwpUri + ENDPOINT, eligibilityRequest, EligibilityResponse.class);
    }
}

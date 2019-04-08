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
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.model.DWPEligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.repository.LegacyHouseholdRepository;
import uk.gov.dhsc.htbhf.dwp.repository.UCHouseholdRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.http.HttpStatus.OK;
import static uk.gov.dhsc.htbhf.dwp.testhelper.DWPEligibilityRequestTestDataFactory.aValidDWPEligibilityRequest;
import static uk.gov.dhsc.htbhf.dwp.testhelper.EligibilityResponseTestDataFactory.aNoMatchEligibilityResponse;
import static uk.gov.dhsc.htbhf.dwp.testhelper.EligibilityResponseTestDataFactory.aValidLegacyEligibilityResponse;
import static uk.gov.dhsc.htbhf.dwp.testhelper.EligibilityResponseTestDataFactory.aValidUCEligibilityResponse;
import static uk.gov.dhsc.htbhf.dwp.testhelper.LegacyHouseholdTestDataFactory.aLegacyHousehold;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.HOMER_NINO;
import static uk.gov.dhsc.htbhf.dwp.testhelper.UCHouseholdTestDataFactory.aUCHousehold;

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

    @MockBean
    private HouseholdVerifier householdVerifier;

    @Autowired
    private EligibilityService eligibilityService;

    @Test
    void shouldReturnResponseFromUCDatabaseIfFound() {
        DWPEligibilityRequest eligibilityRequest = aValidDWPEligibilityRequest();
        UCHousehold household = aUCHousehold();
        given(ucHouseholdRepository.findHouseholdByAdultWithNino(anyString())).willReturn(Optional.of(household));
        given(householdVerifier.detailsMatch(any(UCHousehold.class), any())).willReturn(true);

        EligibilityResponse response = eligibilityService.checkEligibility(eligibilityRequest);

        assertThat(response).isEqualTo(aValidUCEligibilityResponse());

        verify(ucHouseholdRepository).findHouseholdByAdultWithNino(HOMER_NINO);
        verify(householdVerifier).detailsMatch(household, eligibilityRequest.getPerson());
        verifyZeroInteractions(legacyHouseholdRepository, restTemplate);
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
        verifyZeroInteractions(legacyHouseholdRepository, restTemplate);
    }

    @Test
    void shouldReturnResponseFromLegacyDatabaseIfFound() {
        DWPEligibilityRequest eligibilityRequest = aValidDWPEligibilityRequest();
        LegacyHousehold household = aLegacyHousehold();
        given(ucHouseholdRepository.findHouseholdByAdultWithNino(anyString())).willReturn(Optional.empty());
        given(legacyHouseholdRepository.findHouseholdByAdultWithNino(anyString())).willReturn(Optional.of(household));
        given(householdVerifier.detailsMatch(any(LegacyHousehold.class), any())).willReturn(true);

        EligibilityResponse response = eligibilityService.checkEligibility(eligibilityRequest);

        assertThat(response).isEqualTo(aValidLegacyEligibilityResponse());

        verify(ucHouseholdRepository).findHouseholdByAdultWithNino(HOMER_NINO);
        verify(legacyHouseholdRepository).findHouseholdByAdultWithNino(eligibilityRequest.getPerson().getNino());
        verify(householdVerifier).detailsMatch(household, eligibilityRequest.getPerson());
        verifyZeroInteractions(restTemplate);
    }

    @Test
    void shouldReturnNoMatchWhenFoundInLegacyDatabaseAndDetailsDontMatch() {
        DWPEligibilityRequest eligibilityRequest = aValidDWPEligibilityRequest();
        LegacyHousehold household = aLegacyHousehold();
        given(ucHouseholdRepository.findHouseholdByAdultWithNino(anyString())).willReturn(Optional.empty());
        given(legacyHouseholdRepository.findHouseholdByAdultWithNino(anyString())).willReturn(Optional.of(household));
        given(householdVerifier.detailsMatch(any(LegacyHousehold.class), any())).willReturn(false);

        EligibilityResponse response = eligibilityService.checkEligibility(eligibilityRequest);

        assertThat(response).isEqualTo(aNoMatchEligibilityResponse());

        verify(ucHouseholdRepository).findHouseholdByAdultWithNino(HOMER_NINO);
        verify(legacyHouseholdRepository).findHouseholdByAdultWithNino(HOMER_NINO);
        verify(householdVerifier).detailsMatch(household, eligibilityRequest.getPerson());
        verifyZeroInteractions(restTemplate);
    }

    @Test
    void shouldCallDWPServiceWhenHouseholdNotFoundInDatabase() {
        DWPEligibilityRequest eligibilityRequest = aValidDWPEligibilityRequest();
        given(ucHouseholdRepository.findHouseholdByAdultWithNino(anyString())).willReturn(Optional.empty());
        given(legacyHouseholdRepository.findHouseholdByAdultWithNino(anyString())).willReturn(Optional.empty());
        given(restTemplate.postForEntity(anyString(), any(), any()))
                .willReturn(new ResponseEntity<>(aValidUCEligibilityResponse(), OK));

        EligibilityResponse response = eligibilityService.checkEligibility(eligibilityRequest);

        assertThat(response).isEqualTo(aValidUCEligibilityResponse());
        verify(ucHouseholdRepository).findHouseholdByAdultWithNino(HOMER_NINO);
        verify(legacyHouseholdRepository).findHouseholdByAdultWithNino(HOMER_NINO);
        verify(restTemplate).postForEntity(dwpUri + ENDPOINT, eligibilityRequest, EligibilityResponse.class);
    }
}

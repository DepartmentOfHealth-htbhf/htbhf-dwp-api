package uk.gov.dhsc.htbhf.dwp.service.v2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.factory.v2.IdentityAndEligibilityResponseFactory;
import uk.gov.dhsc.htbhf.dwp.http.GetRequestBuilder;
import uk.gov.dhsc.htbhf.dwp.model.*;
import uk.gov.dhsc.htbhf.dwp.repository.v1.UCHouseholdRepository;
import uk.gov.dhsc.htbhf.dwp.testhelper.DWPEligibilityRequestTestDataFactory;
import uk.gov.dhsc.htbhf.dwp.testhelper.v2.UCHouseholdTestDataFactoryV2;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.HttpStatus.OK;
import static uk.gov.dhsc.htbhf.TestConstants.HOMER_NINO;
import static uk.gov.dhsc.htbhf.TestConstants.LISA_DATE_OF_BIRTH;
import static uk.gov.dhsc.htbhf.TestConstants.MAGGIE_DATE_OF_BIRTH;
import static uk.gov.dhsc.htbhf.dwp.testhelper.HttpRequestTestDataFactory.aValidEligibilityHttpEntity;
import static uk.gov.dhsc.htbhf.dwp.testhelper.IdAndEligibilityResponseTestDataFactory.anIdMatchedEligibilityConfirmedUCResponseWithAllMatches;

@ExtendWith(MockitoExtension.class)
class IdentityAndEligibilityServiceTest {

    private static final String BASE_URI = "https://localhost:8120";
    private static final String FULL_URI = "https://localhost:8120/v2/dwp/benefits";

    @Mock
    private UCHouseholdRepository ucHouseholdRepository;
    @Mock
    private IdentityAndEligibilityResponseFactory responseFactory;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private GetRequestBuilder getRequestBuilder;

    private IdentityAndEligibilityService service;

    @BeforeEach
    void initService() {
        service = new IdentityAndEligibilityService(BASE_URI, restTemplate, ucHouseholdRepository, responseFactory, getRequestBuilder);
    }

    @Test
    void shouldReturnAllMatchResponseForMatchingHouseholdInDbCheckIdentityAndEligibility() {
        //Given
        DWPEligibilityRequest eligibilityRequest = DWPEligibilityRequestTestDataFactory.aValidDWPEligibilityRequest();
        UCHousehold ucHousehold = UCHouseholdTestDataFactoryV2.aUCHousehold();

        IdentityAndEligibilityResponse response = anIdMatchedEligibilityConfirmedUCResponseWithAllMatches();
        given(ucHouseholdRepository.findHouseholdByAdultWithNino(any())).willReturn(Optional.of(ucHousehold));
        given(responseFactory.determineIdentityAndEligibilityResponse(any(), any())).willReturn(response);

        //When
        IdentityAndEligibilityResponse serviceResponse = service.checkIdentityAndEligibility(eligibilityRequest);

        //Then
        assertEligibleResponse(serviceResponse);
        verify(ucHouseholdRepository).findHouseholdByAdultWithNino(HOMER_NINO);
        verify(responseFactory).determineIdentityAndEligibilityResponse(ucHousehold, eligibilityRequest);
        verifyNoInteractions(restTemplate, getRequestBuilder);
    }

    @Test
    void shouldReturnResponseFromApiCallForHouseholdNotInDatabase() {
        //Given
        DWPEligibilityRequest eligibilityRequest = DWPEligibilityRequestTestDataFactory.aValidDWPEligibilityRequest();
        given(ucHouseholdRepository.findHouseholdByAdultWithNino(any())).willReturn(Optional.empty());
        IdentityAndEligibilityResponse identityResponse = anIdMatchedEligibilityConfirmedUCResponseWithAllMatches();
        HttpEntity httpEntity = aValidEligibilityHttpEntity();
        given(getRequestBuilder.buildRequestWithHeaders(any())).willReturn(httpEntity);
        given(restTemplate.exchange(anyString(), any(), any(), eq(IdentityAndEligibilityResponse.class)))
                .willReturn(new ResponseEntity<>(identityResponse, OK));

        //When
        IdentityAndEligibilityResponse serviceResponse = service.checkIdentityAndEligibility(eligibilityRequest);

        //Then
        assertEligibleResponse(serviceResponse);
        verify(ucHouseholdRepository).findHouseholdByAdultWithNino(HOMER_NINO);
        verify(restTemplate).exchange(FULL_URI, HttpMethod.GET, httpEntity, IdentityAndEligibilityResponse.class);
        verify(getRequestBuilder).buildRequestWithHeaders(eligibilityRequest);
        verifyNoInteractions(responseFactory);
    }

    private void assertEligibleResponse(IdentityAndEligibilityResponse serviceResponse) {
        assertThat(serviceResponse.getIdentityStatus()).isEqualTo(IdentityOutcome.MATCHED);
        assertThat(serviceResponse.getEligibilityStatus()).isEqualTo(EligibilityOutcome.CONFIRMED);
        assertThat(serviceResponse.getQualifyingBenefits()).isEqualTo(QualifyingBenefits.UNIVERSAL_CREDIT);
        assertThat(serviceResponse.getMobilePhoneMatch()).isEqualTo(VerificationOutcome.MATCHED);
        assertThat(serviceResponse.getEmailAddressMatch()).isEqualTo(VerificationOutcome.MATCHED);
        assertThat(serviceResponse.getAddressLine1Match()).isEqualTo(VerificationOutcome.MATCHED);
        assertThat(serviceResponse.getPostcodeMatch()).isEqualTo(VerificationOutcome.MATCHED);
        assertThat(serviceResponse.getDeathVerificationFlag()).isEqualTo(DeathVerificationFlag.N_A);
        assertThat(serviceResponse.getPregnantChildDOBMatch()).isEqualTo(VerificationOutcome.NOT_SUPPLIED);
        assertThat(serviceResponse.getDobOfChildrenUnder4()).containsExactlyInAnyOrder(LISA_DATE_OF_BIRTH, MAGGIE_DATE_OF_BIRTH);
    }

}

package uk.gov.dhsc.htbhf.dwp.service.v2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.factory.v2.IdentityAndEligibilityResponseFactory;
import uk.gov.dhsc.htbhf.dwp.model.v2.*;
import uk.gov.dhsc.htbhf.dwp.repository.v1.UCHouseholdRepository;
import uk.gov.dhsc.htbhf.dwp.testhelper.v2.DWPEligibilityRequestV2TestDataFactory;
import uk.gov.dhsc.htbhf.dwp.testhelper.v2.UCHouseholdTestDataFactoryV2;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.HttpStatus.OK;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.HOMER_NINO_V2;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.LISA_DOB;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.MAGGIE_AND_LISA_DOBS;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.MAGGIE_DOB;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v2.IdentityAndEligibilityResponseTestDataFactory.anIdentityMatchedEligibilityConfirmedUCResponseWithAllMatches;

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

    private IdentityAndEligibilityService service;

    @BeforeEach
    void initService() {
        service = new IdentityAndEligibilityService(BASE_URI, restTemplate, ucHouseholdRepository, responseFactory);
    }

    @Test
    void shouldReturnAllMatchResponseForMatchingHouseholdInDbCheckIdentityAndEligibility() {
        //Given
        DWPEligibilityRequestV2 eligibilityRequest = DWPEligibilityRequestV2TestDataFactory.aValidDWPEligibilityRequestV2();
        UCHousehold ucHousehold = UCHouseholdTestDataFactoryV2.aUCHousehold();

        IdentityAndEligibilityResponse response = anIdentityMatchedEligibilityConfirmedUCResponseWithAllMatches(MAGGIE_AND_LISA_DOBS);
        given(ucHouseholdRepository.findHouseholdByAdultWithNino(any())).willReturn(Optional.of(ucHousehold));
        given(responseFactory.determineIdentityAndEligibilityResponse(any(), any())).willReturn(response);

        //When
        IdentityAndEligibilityResponse serviceResponse = service.checkIdentityAndEligibility(eligibilityRequest);

        //Then
        assertEligibileResponse(serviceResponse);
        verify(ucHouseholdRepository).findHouseholdByAdultWithNino(HOMER_NINO_V2);
        verify(responseFactory).determineIdentityAndEligibilityResponse(ucHousehold, eligibilityRequest);
        verifyNoInteractions(restTemplate);
    }

    @Test
    void shouldReturnResponseFromApiCallForHouseholdNotInDatabase() {
        //Given
        DWPEligibilityRequestV2 eligibilityRequest = DWPEligibilityRequestV2TestDataFactory.aValidDWPEligibilityRequestV2();
        given(ucHouseholdRepository.findHouseholdByAdultWithNino(any())).willReturn(Optional.empty());
        IdentityAndEligibilityResponse identityResponse = anIdentityMatchedEligibilityConfirmedUCResponseWithAllMatches(MAGGIE_AND_LISA_DOBS);
        given(restTemplate.postForEntity(anyString(), any(), any())).willReturn(new ResponseEntity<>(identityResponse, OK));

        //When
        IdentityAndEligibilityResponse serviceResponse = service.checkIdentityAndEligibility(eligibilityRequest);

        //Then
        assertEligibileResponse(serviceResponse);
        verify(ucHouseholdRepository).findHouseholdByAdultWithNino(HOMER_NINO_V2);
        verify(restTemplate).postForEntity(FULL_URI, eligibilityRequest, IdentityAndEligibilityResponse.class);
        verifyNoInteractions(responseFactory);
    }

    private void assertEligibileResponse(IdentityAndEligibilityResponse serviceResponse) {
        assertThat(serviceResponse.getIdentityStatus()).isEqualTo(IdentityOutcome.MATCHED);
        assertThat(serviceResponse.getEligibilityStatus()).isEqualTo(EligibilityOutcome.CONFIRMED);
        assertThat(serviceResponse.getQualifyingBenefits()).isEqualTo(QualifyingBenefits.UNIVERSAL_CREDIT);
        assertThat(serviceResponse.getMobilePhoneMatch()).isEqualTo(VerificationOutcome.MATCHED);
        assertThat(serviceResponse.getEmailAddressMatch()).isEqualTo(VerificationOutcome.MATCHED);
        assertThat(serviceResponse.getAddressLine1Match()).isEqualTo(VerificationOutcome.MATCHED);
        assertThat(serviceResponse.getPostcodeMatch()).isEqualTo(VerificationOutcome.MATCHED);
        assertThat(serviceResponse.getDeathVerificationFlag()).isEqualTo(DeathVerificationFlag.N_A);
        assertThat(serviceResponse.getPregnantChildDOBMatch()).isEqualTo(VerificationOutcome.NOT_SET);
        assertThat(serviceResponse.getDobOfChildrenUnder4()).containsExactlyInAnyOrder(LISA_DOB, MAGGIE_DOB);
    }

}

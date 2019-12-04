package uk.gov.dhsc.htbhf.dwp.factory.v2;

import org.junit.jupiter.api.Test;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.model.v2.DWPEligibilityRequestV2;
import uk.gov.dhsc.htbhf.dwp.model.v2.IdentityAndEligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.model.v2.PersonDTOV2;
import uk.gov.dhsc.htbhf.dwp.model.v2.VerificationOutcome;
import uk.gov.dhsc.htbhf.dwp.testhelper.v2.DWPEligibilityRequestV2TestDataFactory;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dhsc.htbhf.TestConstants.*;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v2.IdAndEligibilityResponseTestDataFactory.*;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v2.PersonDTOV2TestDataFactory.*;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v2.UCHouseholdTestDataFactoryV2.*;

class IdentityAndEligibilityResponseFactoryTest {

    private IdentityAndEligibilityResponseFactory factory = new IdentityAndEligibilityResponseFactory();

    @Test
    void shouldReturnEverythingMatchedResponse() {
        runTest(aUCHousehold(), aValidPersonDTOV2(), anIdMatchedEligibilityConfirmedUCResponseWithAllMatches(VerificationOutcome.NOT_SET,
                MAGGIE_AND_LISA_DOBS));
    }

    @Test
    void shouldReturnNotEligibleWhenEarningsThresholdExceededResponse() {
        UCHousehold household = aUCHouseholdWithEarningsThresholdExceeded();
        runTest(household, aValidPersonDTOV2(), anIdMatchedEligibilityNotConfirmedResponse());
    }

    @Test
    void shouldReturnEverythingMatchedWithpregnantChildDOBMatchNotSuppliedResponse() {
        PersonDTOV2 person = aPersonDTOV2WithPregnantDependantDob(null);
        runTest(aUCHousehold(), person, anIdMatchedEligibilityConfirmedUCResponseWithAllMatches(VerificationOutcome.NOT_SUPPLIED,
                MAGGIE_AND_LISA_DOBS));
    }

    @Test
    void shouldReturnIdentityStatusNotMatchedResponseForNotMatchingSurname() {
        runTest(aUCHousehold(), aPersonDTOV2WithSurname("Doe"), anIdMatchFailedResponse());
    }

    @Test
    void shouldReturnIdentityStatusNotMatchedResponseForNotMatchingDob() {
        runTest(aUCHousehold(), aPersonDTOV2WithDateOfBirth(TWENTY_YEAR_OLD), anIdMatchFailedResponse());
    }

    @Test
    void shouldReturnIdentityStatusMatchedResponseForNoDateOfBirthInDatabase() {
        runTest(aUCHouseholdWithAdultDateOfBirth(null), aPersonDTOV2WithDateOfBirth(TWENTY_YEAR_OLD),
                anIdMatchedEligibilityConfirmedUCResponseWithAllMatches(VerificationOutcome.NOT_SET, MAGGIE_AND_LISA_DOBS));
    }

    @Test
    void shouldReturnIdentityStatusNotMatchedResponseForNotMatchingNino() {
        runTest(aUCHousehold(), aPersonDTOV2WithNino("XX987654A"), anIdMatchFailedResponse());
    }

    @Test
    void shouldReturnAddressLine1NotMatchedResponse() {
        runTest(aUCHousehold(), aPersonDTOV2WithAddressLine1("Another Street"), anIdMatchedEligibilityConfirmedAddressNotMatchedResponse());
    }

    @Test
    void shouldReturnPostcodeNotMatchedResponse() {
        runTest(aUCHousehold(), aPersonDTOV2WithPostcode("BS14TQ"), anIdMatchedEligibilityConfirmedPostcodeNotMatchedResponse());
    }

    @Test
    void shouldReturnEmailNotMatchedResponse() {
        IdentityAndEligibilityResponse expectedResponse = anIdMatchedEligibilityConfirmedUCResponseWithMatches(
                VerificationOutcome.MATCHED, VerificationOutcome.NOT_MATCHED, MAGGIE_AND_LISA_DOBS);
        runTest(aUCHousehold(), aPersonDTOV2WithEmailAddress("a@b.com"), expectedResponse);
    }

    @Test
    void shouldReturnMobileNotMatchedResponse() {
        IdentityAndEligibilityResponse expectedResponse = anIdMatchedEligibilityConfirmedUCResponseWithMatches(
                VerificationOutcome.NOT_MATCHED, VerificationOutcome.MATCHED, MAGGIE_AND_LISA_DOBS);
        runTest(aUCHousehold(), aPersonDTOV2WithMobilePhoneNumber("+447999123123"), expectedResponse);
    }

    @Test
    void shouldReturnMobileNotSuppliedResponse() {
        IdentityAndEligibilityResponse expectedResponse = anIdMatchedEligibilityConfirmedUCResponseWithMatches(
                VerificationOutcome.NOT_SUPPLIED, VerificationOutcome.MATCHED, MAGGIE_AND_LISA_DOBS);
        runTest(aUCHousehold(), aPersonDTOV2WithMobilePhoneNumber(null), expectedResponse);
    }

    @Test
    void shouldReturnEmailNotSuppliedResponse() {
        IdentityAndEligibilityResponse expectedResponse = anIdMatchedEligibilityConfirmedUCResponseWithMatches(
                VerificationOutcome.MATCHED, VerificationOutcome.NOT_SUPPLIED, MAGGIE_AND_LISA_DOBS);
        runTest(aUCHousehold(), aPersonDTOV2WithEmailAddress(null), expectedResponse);
    }

    @Test
    void shouldReturnMobileNotHeldResponse() {
        IdentityAndEligibilityResponse expectedResponse = anIdMatchedEligibilityConfirmedUCResponseWithMatches(
                VerificationOutcome.NOT_HELD, VerificationOutcome.MATCHED, MAGGIE_AND_LISA_DOBS);
        UCHousehold household = aUCHouseholdWithAdultMobileAndEmail(null, HOMER_EMAIL);
        runTest(household, aValidPersonDTOV2(), expectedResponse);
    }

    @Test
    void shouldReturnEmailNotHeldResponse() {
        IdentityAndEligibilityResponse expectedResponse = anIdMatchedEligibilityConfirmedUCResponseWithMatches(
                VerificationOutcome.MATCHED, VerificationOutcome.NOT_HELD, MAGGIE_AND_LISA_DOBS);
        UCHousehold household = aUCHouseholdWithAdultMobileAndEmail(HOMER_MOBILE, null);
        runTest(household, aValidPersonDTOV2(), expectedResponse);
    }

    @Test
    void shouldReturnMatchedResponseFilteringOutChildren4OrOver() {
        LocalDate justUnderFourYearOld = LocalDate.now().minusYears(4).plusDays(1);
        //Note that Bart will be excluded because he is exactly 4 years old
        List<LocalDate> childrenDobsWithout5YearOld = List.of(LISA_DATE_OF_BIRTH, justUnderFourYearOld);
        IdentityAndEligibilityResponse expectedResponse = anIdMatchedEligibilityConfirmedUCResponseWithAllMatches(VerificationOutcome.NOT_SET,
                childrenDobsWithout5YearOld);

        UCHousehold household = aUCHouseholdWithChildren(
                aUCChild(BART_DATE_OF_BIRTH),
                aUCChild(LISA_DATE_OF_BIRTH),
                aUCChild(justUnderFourYearOld),
                aUCChild(FIVE_YEAR_OLD));
        runTest(household, aValidPersonDTOV2(), expectedResponse);
    }

    private void runTest(UCHousehold household, PersonDTOV2 person, IdentityAndEligibilityResponse expectedResponse) {
        //Given
        DWPEligibilityRequestV2 request = DWPEligibilityRequestV2TestDataFactory.aValidDWPEligibilityRequestV2WithPerson(person);

        //When
        IdentityAndEligibilityResponse response = factory.determineIdentityAndEligibilityResponse(household, request);

        //Then (cannot assert they are equal because the children dobs come out in a random order
        assertThat(response.getIdentityStatus()).isEqualTo(expectedResponse.getIdentityStatus());
        assertThat(response.getEligibilityStatus()).isEqualTo(expectedResponse.getEligibilityStatus());
        assertThat(response.getQualifyingBenefits()).isEqualTo(expectedResponse.getQualifyingBenefits());
        assertThat(response.getMobilePhoneMatch()).isEqualTo(expectedResponse.getMobilePhoneMatch());
        assertThat(response.getEmailAddressMatch()).isEqualTo(expectedResponse.getEmailAddressMatch());
        assertThat(response.getAddressLine1Match()).isEqualTo(expectedResponse.getAddressLine1Match());
        assertThat(response.getPostcodeMatch()).isEqualTo(expectedResponse.getPostcodeMatch());
        assertThat(response.getDeathVerificationFlag()).isEqualTo(expectedResponse.getDeathVerificationFlag());
        assertThat(response.getPregnantChildDOBMatch()).isEqualTo(expectedResponse.getPregnantChildDOBMatch());
        assertThat(response.getDobOfChildrenUnder4()).containsExactlyInAnyOrderElementsOf(expectedResponse.getDobOfChildrenUnder4());
    }
}

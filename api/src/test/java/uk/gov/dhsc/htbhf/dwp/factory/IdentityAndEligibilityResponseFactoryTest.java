package uk.gov.dhsc.htbhf.dwp.factory;

import org.junit.jupiter.api.Test;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.model.DWPEligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.IdentityAndEligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;
import uk.gov.dhsc.htbhf.dwp.model.VerificationOutcome;
import uk.gov.dhsc.htbhf.dwp.testhelper.DWPEligibilityRequestTestDataFactory;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dhsc.htbhf.TestConstants.*;
import static uk.gov.dhsc.htbhf.dwp.testhelper.IdAndEligibilityResponseTestDataFactory.*;
import static uk.gov.dhsc.htbhf.dwp.testhelper.PersonDTOTestDataFactory.*;
import static uk.gov.dhsc.htbhf.dwp.testhelper.UCHouseholdTestDataFactory.*;

class IdentityAndEligibilityResponseFactoryTest {

    private IdentityAndEligibilityResponseFactory factory = new IdentityAndEligibilityResponseFactory();

    @Test
    void shouldReturnEverythingMatchedResponse() {
        runTest(aUCHousehold(), aValidPersonDTO(), anIdMatchedEligibilityConfirmedUCResponseWithAllMatches(VerificationOutcome.NOT_SET,
                MAGGIE_AND_LISA_DOBS));
    }

    @Test
    void shouldReturnNotEligibleWhenEarningsThresholdExceededResponse() {
        UCHousehold household = aUCHouseholdWithEarningsThresholdExceeded();
        runTest(household, aValidPersonDTO(), anIdMatchedEligibilityNotConfirmedResponse());
    }

    @Test
    void shouldReturnEverythingMatchedWithpregnantChildDOBMatchNotSuppliedResponse() {
        PersonDTO person = aPersonDTOWithPregnantDependantDob(null);
        runTest(aUCHousehold(), person, anIdMatchedEligibilityConfirmedUCResponseWithAllMatches(VerificationOutcome.NOT_SUPPLIED,
                MAGGIE_AND_LISA_DOBS));
    }

    @Test
    void shouldReturnIdentityStatusNotMatchedResponseForNotMatchingSurname() {
        runTest(aUCHousehold(), aPersonDTOWithSurname("Doe"), anIdMatchFailedResponse());
    }

    @Test
    void shouldReturnIdentityStatusNotMatchedResponseForNotMatchingDob() {
        runTest(aUCHousehold(), aPersonDTOWithDateOfBirth(TWENTY_YEAR_OLD), anIdMatchFailedResponse());
    }

    @Test
    void shouldReturnIdentityStatusMatchedResponseForNoDateOfBirthInDatabase() {
        runTest(aUCHouseholdWithAdultDateOfBirth(null), aPersonDTOWithDateOfBirth(TWENTY_YEAR_OLD),
                anIdMatchedEligibilityConfirmedUCResponseWithAllMatches(VerificationOutcome.NOT_SET, MAGGIE_AND_LISA_DOBS));
    }

    @Test
    void shouldReturnIdentityStatusNotMatchedResponseForNotMatchingNino() {
        runTest(aUCHousehold(), aPersonDTOWithNino("XX987654A"), anIdMatchFailedResponse());
    }

    @Test
    void shouldReturnAddressLine1NotMatchedResponse() {
        runTest(aUCHousehold(), aPersonDTOWithAddressLine1("Another Street"), anIdMatchedEligibilityConfirmedAddressNotMatchedResponse());
    }

    @Test
    void shouldReturnPostcodeNotMatchedResponse() {
        runTest(aUCHousehold(), aPersonDTOWithPostcode("BS14TQ"), anIdMatchedEligibilityConfirmedPostcodeNotMatchedResponse());
    }

    @Test
    void shouldReturnEmailNotMatchedResponse() {
        IdentityAndEligibilityResponse expectedResponse = anIdMatchedEligibilityConfirmedUCResponseWithMatches(
                VerificationOutcome.MATCHED, VerificationOutcome.NOT_MATCHED, MAGGIE_AND_LISA_DOBS);
        runTest(aUCHousehold(), aPersonDTOWithEmailAddress("a@b.com"), expectedResponse);
    }

    @Test
    void shouldReturnMobileNotMatchedResponse() {
        IdentityAndEligibilityResponse expectedResponse = anIdMatchedEligibilityConfirmedUCResponseWithMatches(
                VerificationOutcome.NOT_MATCHED, VerificationOutcome.MATCHED, MAGGIE_AND_LISA_DOBS);
        runTest(aUCHousehold(), aPersonDTOWithMobilePhoneNumber("+447999123123"), expectedResponse);
    }

    @Test
    void shouldReturnMobileNotSuppliedResponse() {
        IdentityAndEligibilityResponse expectedResponse = anIdMatchedEligibilityConfirmedUCResponseWithMatches(
                VerificationOutcome.NOT_SUPPLIED, VerificationOutcome.MATCHED, MAGGIE_AND_LISA_DOBS);
        runTest(aUCHousehold(), aPersonDTOWithMobilePhoneNumber(null), expectedResponse);
    }

    @Test
    void shouldReturnEmailNotSuppliedResponse() {
        IdentityAndEligibilityResponse expectedResponse = anIdMatchedEligibilityConfirmedUCResponseWithMatches(
                VerificationOutcome.MATCHED, VerificationOutcome.NOT_SUPPLIED, MAGGIE_AND_LISA_DOBS);
        runTest(aUCHousehold(), aPersonDTOWithEmailAddress(null), expectedResponse);
    }

    @Test
    void shouldReturnMobileNotHeldResponse() {
        IdentityAndEligibilityResponse expectedResponse = anIdMatchedEligibilityConfirmedUCResponseWithMatches(
                VerificationOutcome.NOT_HELD, VerificationOutcome.MATCHED, MAGGIE_AND_LISA_DOBS);
        UCHousehold household = aUCHouseholdWithAdultMobileAndEmail(null, HOMER_EMAIL);
        runTest(household, aValidPersonDTO(), expectedResponse);
    }

    @Test
    void shouldReturnEmailNotHeldResponse() {
        IdentityAndEligibilityResponse expectedResponse = anIdMatchedEligibilityConfirmedUCResponseWithMatches(
                VerificationOutcome.MATCHED, VerificationOutcome.NOT_HELD, MAGGIE_AND_LISA_DOBS);
        UCHousehold household = aUCHouseholdWithAdultMobileAndEmail(HOMER_MOBILE, null);
        runTest(household, aValidPersonDTO(), expectedResponse);
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
        runTest(household, aValidPersonDTO(), expectedResponse);
    }

    private void runTest(UCHousehold household, PersonDTO person, IdentityAndEligibilityResponse expectedResponse) {
        //Given
        DWPEligibilityRequest request = DWPEligibilityRequestTestDataFactory.aValidDWPEligibilityRequestWithPerson(person);

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

package uk.gov.dhsc.htbhf.dwp.testhelper.v2;

import uk.gov.dhsc.htbhf.dwp.model.v2.*;

import java.time.LocalDate;
import java.util.List;

import static java.util.Collections.emptyList;
import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.NO_HOUSEHOLD_IDENTIFIER_PROVIDED;

public class IdentityAndEligibilityResponseTestDataFactory {

    public static IdentityAndEligibilityResponse anIdentityMatchFailedResponse() {
        return IdentityAndEligibilityResponse.builder()
                .identityStatus(IdentityOutcome.NOT_MATCHED)
                .eligibilityStatus(EligibilityOutcome.NOT_SET)
                .qualifyingBenefits(QualifyingBenefits.NOT_SET)
                .mobilePhoneMatch(VerificationOutcome.NOT_SET)
                .emailAddressMatch(VerificationOutcome.NOT_SET)
                .addressLine1Match(VerificationOutcome.NOT_SET)
                .postcodeMatch(VerificationOutcome.NOT_SET)
                .pregnantChildDOBMatch(VerificationOutcome.NOT_SET)
                .householdIdentifier(NO_HOUSEHOLD_IDENTIFIER_PROVIDED)
                .dobOfChildrenUnder4(emptyList())
                .deathVerificationFlag(DeathVerificationFlag.N_A)
                .build();
    }

    public static IdentityAndEligibilityResponse anIdentityMatchedEligibilityNotConfirmedResponse() {
        return IdentityAndEligibilityResponse.builder()
                .identityStatus(IdentityOutcome.MATCHED)
                .eligibilityStatus(EligibilityOutcome.NOT_CONFIRMED)
                .qualifyingBenefits(QualifyingBenefits.NOT_SET)
                .mobilePhoneMatch(VerificationOutcome.NOT_SET)
                .emailAddressMatch(VerificationOutcome.NOT_SET)
                .addressLine1Match(VerificationOutcome.NOT_SET)
                .postcodeMatch(VerificationOutcome.NOT_SET)
                .pregnantChildDOBMatch(VerificationOutcome.NOT_SET)
                .householdIdentifier(NO_HOUSEHOLD_IDENTIFIER_PROVIDED)
                .dobOfChildrenUnder4(emptyList())
                .deathVerificationFlag(DeathVerificationFlag.N_A)
                .build();
    }

    public static IdentityAndEligibilityResponse anIdentityMatchedEligibilityConfirmedPostcodeNotMatchedResponse() {
        return IdentityAndEligibilityResponse.builder()
                .identityStatus(IdentityOutcome.MATCHED)
                .eligibilityStatus(EligibilityOutcome.CONFIRMED)
                .qualifyingBenefits(QualifyingBenefits.NOT_SET)
                .mobilePhoneMatch(VerificationOutcome.NOT_SET)
                .emailAddressMatch(VerificationOutcome.NOT_SET)
                .addressLine1Match(VerificationOutcome.MATCHED)
                .postcodeMatch(VerificationOutcome.NOT_MATCHED)
                .pregnantChildDOBMatch(VerificationOutcome.NOT_SET)
                .householdIdentifier(NO_HOUSEHOLD_IDENTIFIER_PROVIDED)
                .dobOfChildrenUnder4(emptyList())
                .deathVerificationFlag(DeathVerificationFlag.N_A)
                .build();
    }

    public static IdentityAndEligibilityResponse anIdentityMatchedEligibilityConfirmedAddressNotMatchedResponse() {
        return IdentityAndEligibilityResponse.builder()
                .identityStatus(IdentityOutcome.MATCHED)
                .eligibilityStatus(EligibilityOutcome.CONFIRMED)
                .qualifyingBenefits(QualifyingBenefits.NOT_SET)
                .mobilePhoneMatch(VerificationOutcome.NOT_SET)
                .emailAddressMatch(VerificationOutcome.NOT_SET)
                .addressLine1Match(VerificationOutcome.NOT_MATCHED)
                .postcodeMatch(VerificationOutcome.MATCHED)
                .pregnantChildDOBMatch(VerificationOutcome.NOT_SET)
                .householdIdentifier(NO_HOUSEHOLD_IDENTIFIER_PROVIDED)
                .dobOfChildrenUnder4(emptyList())
                .deathVerificationFlag(DeathVerificationFlag.N_A)
                .build();
    }

    public static IdentityAndEligibilityResponse anIdentityMatchedEligibilityConfirmedUCResponseWithMatches(VerificationOutcome mobileVerification,
                                                                                                            VerificationOutcome emailVerification,
                                                                                                            List<LocalDate> childrenDobs) {
        return IdentityAndEligibilityResponse.builder()
                .identityStatus(IdentityOutcome.MATCHED)
                .eligibilityStatus(EligibilityOutcome.CONFIRMED)
                .qualifyingBenefits(QualifyingBenefits.UNIVERSAL_CREDIT)
                .mobilePhoneMatch(mobileVerification)
                .emailAddressMatch(emailVerification)
                .addressLine1Match(VerificationOutcome.MATCHED)
                .postcodeMatch(VerificationOutcome.MATCHED)
                .pregnantChildDOBMatch(VerificationOutcome.NOT_SET)
                .householdIdentifier(NO_HOUSEHOLD_IDENTIFIER_PROVIDED)
                .deathVerificationFlag(DeathVerificationFlag.N_A)
                .dobOfChildrenUnder4(childrenDobs)
                .build();
    }

    public static IdentityAndEligibilityResponse anIdentityMatchedEligibilityConfirmedUCResponseWithAllMatches(List<LocalDate> childrenDobs) {
        return anIdentityMatchedEligibilityConfirmedUCResponseWithAllMatches(VerificationOutcome.NOT_SET, childrenDobs);
    }

    public static IdentityAndEligibilityResponse anIdentityMatchedEligibilityConfirmedUCResponseWithAllMatches(VerificationOutcome pregnantChildDOBMatch,
                                                                                                               List<LocalDate> childrenDobs) {
        return IdentityAndEligibilityResponse.builder()
                .identityStatus(IdentityOutcome.MATCHED)
                .eligibilityStatus(EligibilityOutcome.CONFIRMED)
                .qualifyingBenefits(QualifyingBenefits.UNIVERSAL_CREDIT)
                .mobilePhoneMatch(VerificationOutcome.MATCHED)
                .emailAddressMatch(VerificationOutcome.MATCHED)
                .addressLine1Match(VerificationOutcome.MATCHED)
                .postcodeMatch(VerificationOutcome.MATCHED)
                .pregnantChildDOBMatch(pregnantChildDOBMatch)
                .householdIdentifier(NO_HOUSEHOLD_IDENTIFIER_PROVIDED)
                .deathVerificationFlag(DeathVerificationFlag.N_A)
                .dobOfChildrenUnder4(childrenDobs)
                .build();
    }

}

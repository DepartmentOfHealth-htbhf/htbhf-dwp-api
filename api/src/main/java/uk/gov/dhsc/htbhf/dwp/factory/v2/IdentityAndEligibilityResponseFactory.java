package uk.gov.dhsc.htbhf.dwp.factory.v2;

import org.springframework.stereotype.Component;
import uk.gov.dhsc.htbhf.dwp.entity.v1.uc.UCAdult;
import uk.gov.dhsc.htbhf.dwp.entity.v1.uc.UCChild;
import uk.gov.dhsc.htbhf.dwp.entity.v1.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.model.v2.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static uk.gov.dhsc.htbhf.dwp.factory.v2.IdentityVerificationUtils.areEqualIgnoringWhitespace;
import static uk.gov.dhsc.htbhf.dwp.factory.v2.IdentityVerificationUtils.determineVerificationOutcome;
import static uk.gov.dhsc.htbhf.dwp.factory.v2.IdentityVerificationUtils.firstSixCharacterMatch;
import static uk.gov.dhsc.htbhf.dwp.factory.v2.IdentityVerificationUtils.matchingAdult;

@Component
public class IdentityAndEligibilityResponseFactory {

    private static final String NO_HOUSEHOLD_IDENTIFIER_PROVIDED = "";

    /**
     * Builds a response based on the following criteria.
     * <ul>
     * <li> Identity status is MATCHED if NINO, Surname and DOB match the request, otherwise NOT_MATCHED</li>
     * <li> If the Identity Status is NOT_MATCH, then all other response enum values are NOT_SET.</li>
     * <li> Eligibility status is ELIGIBLE if the Id status is MATCHED, else NOT_SET.</li>
     * <li> Address Line 1 Match is MATCHED if the first 6 characters match (ignoring case), else NOT_MATCHED</li>
     * <li> Postcode match is MATCHED if the postcode matches ignoring whitespace and casing, else NOT_MATCHED</li>
     * <li> Mobile match is MATCHED if the mobile matches exactly, NOT_SUPPLIED if blank else NOT_MATCHED. If there is no
     * value in the database, then this is NOT_HELD</li>
     * <li> Email match is MATCHED if the email matches ignoring case, NOT_SUPPLIED if blank else NOT_MATCHED. If there is no
     * value in the database, then this is NOT_HELD</li>
     * <li> Qualifying Benefits are UNIVERSAL_CREDIT if the id is matched (including the address)</li>
     * <li> Household identifier will always be blank.</li>
     * <li> Pregnant dependant DOB will be NOT_SUPPLIED if not provided in the request, else NOT_SET</li>
     * <li> The dob of children under 4 will simply be returned from the db if the id status is MATCHED. Any dates
     * of birth which have become over 4 in the database are filtered out.</li>
     * </ul>
     *
     * @param household household to check
     * @param request   request to check
     * @return the response based on what's the request and the household in the database.
     */
    public IdentityAndEligibilityResponse determineIdentityAndEligibilityResponse(UCHousehold household, DWPEligibilityRequestV2 request) {

        IdentityAndEligibilityResponse.IdentityAndEligibilityResponseBuilder builder = setupDefaultBuilder();
        PersonDTOV2 person = request.getPerson();
        IdentityOutcome identityStatus = determineIdentityStatus(household, person, builder);
        EligibilityOutcome eligibilityStatus = determineEligibilityStatus(identityStatus, builder);

        if (IdentityOutcome.NOT_MATCHED == identityStatus || EligibilityOutcome.NOT_CONFIRMED == eligibilityStatus) {
            return builder.build();
        }

        UCAdult matchingAdult = household.getAdults().stream().filter(adult -> matchingAdult(adult, person)).findFirst().get();

        VerificationOutcome addressLine1VerificationOutcome = determineAddressLine1VerificationOutcome(matchingAdult, person, builder);
        VerificationOutcome postcodeVerificationOutcome = determinePostcodeVerificationOutcome(matchingAdult, person, builder);

        if (VerificationOutcome.NOT_MATCHED == addressLine1VerificationOutcome || VerificationOutcome.NOT_MATCHED == postcodeVerificationOutcome) {
            return builder.build();
        }

        builder.qualifyingBenefits(QualifyingBenefits.UNIVERSAL_CREDIT);
        setEmailVerificationOutcome(matchingAdult, person, builder);
        setMobileVerificationOutcome(matchingAdult, person, builder);
        setDobOfChildrenUnder4(household, builder);
        setPregnantDependantDob(person, builder);
        return builder.build();
    }

    private void setPregnantDependantDob(PersonDTOV2 person, IdentityAndEligibilityResponse.IdentityAndEligibilityResponseBuilder builder) {
        if (person.getPregnantDependentDob() == null) {
            builder.pregnantChildDOBMatch(VerificationOutcome.NOT_SUPPLIED);
        }
    }

    //Make sure we only include those children which are under 4
    private void setDobOfChildrenUnder4(UCHousehold household, IdentityAndEligibilityResponse.IdentityAndEligibilityResponseBuilder builder) {
        LocalDate fourYearsAgo = LocalDate.now().minusYears(4);
        List<LocalDate> childrenDobs = household.getChildren().stream()
                .filter(child -> child.getDateOfBirth().isAfter(fourYearsAgo))
                .map(UCChild::getDateOfBirth)
                .collect(Collectors.toList());
        builder.dobOfChildrenUnder4(childrenDobs);
    }

    private void setMobileVerificationOutcome(UCAdult matchingAdult, PersonDTOV2 person,
                                              IdentityAndEligibilityResponse.IdentityAndEligibilityResponseBuilder builder) {
        VerificationOutcome mobileVerificationOutcome = determineVerificationOutcome(
                matchingAdult.getMobilePhoneNumber(),
                person.getMobilePhoneNumber(),
                IdentityVerificationUtils::areEqual);
        builder.mobilePhoneMatch(mobileVerificationOutcome);
    }

    private void setEmailVerificationOutcome(UCAdult matchingAdult, PersonDTOV2 person,
                                             IdentityAndEligibilityResponse.IdentityAndEligibilityResponseBuilder builder) {
        VerificationOutcome emailVerificationOutcome = determineVerificationOutcome(
                matchingAdult.getEmailAddress(),
                person.getEmailAddress(),
                IdentityVerificationUtils::areEqual);
        builder.emailAddressMatch(emailVerificationOutcome);
    }

    private VerificationOutcome determinePostcodeVerificationOutcome(UCAdult matchingAdult, PersonDTOV2 person,
                                                                     IdentityAndEligibilityResponse.IdentityAndEligibilityResponseBuilder builder) {
        VerificationOutcome postcodeVerificationOutcome = (areEqualIgnoringWhitespace(matchingAdult.getPostcode(), person.getPostcode()))
                ? VerificationOutcome.MATCHED : VerificationOutcome.NOT_MATCHED;
        builder.postcodeMatch(postcodeVerificationOutcome);
        return postcodeVerificationOutcome;
    }

    private VerificationOutcome determineAddressLine1VerificationOutcome(UCAdult adult, PersonDTOV2 person,
                                                                         IdentityAndEligibilityResponse.IdentityAndEligibilityResponseBuilder builder) {
        VerificationOutcome addressLine1VerificationOutcome = (firstSixCharacterMatch(adult.getAddressLine1(), person.getAddressLine1()))
                ? VerificationOutcome.MATCHED : VerificationOutcome.NOT_MATCHED;
        builder.addressLine1Match(addressLine1VerificationOutcome);
        return addressLine1VerificationOutcome;
    }

    private EligibilityOutcome determineEligibilityStatus(IdentityOutcome identityStatus,
                                                          IdentityAndEligibilityResponse.IdentityAndEligibilityResponseBuilder builder) {
        EligibilityOutcome eligibilityStatus = (identityStatus == IdentityOutcome.NOT_MATCHED)
                ? EligibilityOutcome.NOT_SET : EligibilityOutcome.CONFIRMED;
        //TODO MRS 06/11/2019: Add incomeThresholdExceeded back on household and test here for eligibility - if true, then return NOT_CONFIRMED. Separate PR.
        builder.eligibilityStatus(eligibilityStatus);
        return eligibilityStatus;
    }

    private IdentityOutcome determineIdentityStatus(UCHousehold household, PersonDTOV2 person,
                                                    IdentityAndEligibilityResponse.IdentityAndEligibilityResponseBuilder builder) {
        IdentityOutcome identityStatus = household.getAdults().stream().anyMatch(adult -> matchingAdult(adult, person))
                ? IdentityOutcome.MATCHED : IdentityOutcome.NOT_MATCHED;
        builder.identityStatus(identityStatus);
        return identityStatus;
    }

    private IdentityAndEligibilityResponse.IdentityAndEligibilityResponseBuilder setupDefaultBuilder() {
        return IdentityAndEligibilityResponse.builder()
                .identityStatus(IdentityOutcome.NOT_MATCHED)
                .eligibilityStatus(EligibilityOutcome.NOT_SET)
                .pregnantChildDOBMatch(VerificationOutcome.NOT_SET)
                .qualifyingBenefits(QualifyingBenefits.NOT_SET)
                .addressLine1Match(VerificationOutcome.NOT_SET)
                .postcodeMatch(VerificationOutcome.NOT_SET)
                .mobilePhoneMatch(VerificationOutcome.NOT_SET)
                .emailAddressMatch(VerificationOutcome.NOT_SET)
                .deathVerificationFlag(DeathVerificationFlag.N_A)
                .householdIdentifier(NO_HOUSEHOLD_IDENTIFIER_PROVIDED)
                .dobOfChildrenUnder4(emptyList());
    }
}

package uk.gov.dhsc.htbhf.dwp.factory.v2;

import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.dhsc.htbhf.dwp.entity.v1.uc.UCAdult;
import uk.gov.dhsc.htbhf.dwp.entity.v1.uc.UCChild;
import uk.gov.dhsc.htbhf.dwp.entity.v1.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.model.v2.*;

import java.time.LocalDate;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Component
public class IdentityAndEligibilityResponseFactory {

    private static final String NO_HOUSEHOLD_IDENTIFIER_PROVIDED = "";

    /**
     * Builds a response based on the following criteria.
     * <ul>
     * <li> Identity status is MATCHED if NINO, Surname and DOB match the request</li>
     * <li> Eligibility status is ELIGIBLE if the Id status is MATCHED, else INELIGIBLE.</li>
     * <li> Address Line 1 Match is MATCHED if the first 6 characters match, else NOT_MATCHED</li>
     * <li> Postcode match is MATCHED if the postcode matches ignoring whitespace and casing</li>
     * <li> Mobile match is MATCHED if the mobile matches exactly, NOT_SUPPLIED if blank else NOT_MATCHED</li>
     * <li> Email match is MATCHED if the email matches ignoring case, NOT_SUPPLIED if blank else NOT_MATCHED</li>
     * <li> Qualifying Benefits are UNIVERSAL_CREDIT if the id is matched (including the address)</li>
     * <li> Household identifier will always be blank.</li>
     * <li> Pregnant dependant DOB will be NOT_SUPPLIED if not provided in the request, else NOT_SET</li>
     * <li> The dob of children under 4 will simply be returned from the db if the id status is MATCHED</li>
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
        return builder.build();
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
                this::areEqual);
        builder.mobilePhoneMatch(mobileVerificationOutcome);
    }

    private void setEmailVerificationOutcome(UCAdult matchingAdult, PersonDTOV2 person,
                                             IdentityAndEligibilityResponse.IdentityAndEligibilityResponseBuilder builder) {
        VerificationOutcome emailVerificationOutcome = determineVerificationOutcome(
                matchingAdult.getEmailAddress(),
                person.getEmailAddress(),
                this::areEqual);
        builder.emailAddressMatch(emailVerificationOutcome);
    }

    private VerificationOutcome determineVerificationOutcome(String ucSeededValue, String requestValue, BiPredicate<String, String> verificationCheck) {
        if (ucSeededValue == null) {
            return VerificationOutcome.NOT_HELD;
        }
        if (requestValue == null) {
            return VerificationOutcome.NOT_SUPPLIED;
        }
        if (verificationCheck.test(ucSeededValue, requestValue)) {
            return VerificationOutcome.MATCHED;
        }
        return VerificationOutcome.NOT_MATCHED;
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

    //TODO MRS 05/11/2019: Some of these methods should be refactored out into a IdentityVerifier class or similar so they can be easily tested
    private Boolean areEqualIgnoringWhitespace(String s1, String s2) {
        return areEqual(s1.replaceAll("\\s+", ""), s2.replaceAll("\\s+", ""));
    }

    private boolean firstSixCharacterMatch(String s1, String s2) {
        return areEqual(StringUtils.left(s1, 6), StringUtils.left(s2, 6));
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

    private boolean matchingAdult(UCAdult adult, PersonDTOV2 person) {
        return areEqual(adult.getNino(), person.getNino())
                && areEqual(adult.getSurname(), person.getSurname())
                && dateOfBirthMatches(adult, person);
    }

    private boolean areEqual(String s1, String s2) {
        return s1.trim().equalsIgnoreCase(s2.trim());
    }

    private boolean dateOfBirthMatches(UCAdult ucAdult, PersonDTOV2 person) {
        if (ucAdult.getDateOfBirth() == null) {
            return true;
        }
        return ucAdult.getDateOfBirth().equals(person.getDateOfBirth());
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

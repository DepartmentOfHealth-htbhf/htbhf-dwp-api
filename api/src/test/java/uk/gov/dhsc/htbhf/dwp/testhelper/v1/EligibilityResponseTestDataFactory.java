package uk.gov.dhsc.htbhf.dwp.testhelper.v1;

import uk.gov.dhsc.htbhf.dwp.model.v1.ChildDTO;
import uk.gov.dhsc.htbhf.dwp.model.v1.EligibilityResponse;

import java.util.List;

import static java.util.Arrays.asList;
import static uk.gov.dhsc.htbhf.TestConstants.LISA_DATE_OF_BIRTH;
import static uk.gov.dhsc.htbhf.TestConstants.MAGGIE_DATE_OF_BIRTH;
import static uk.gov.dhsc.htbhf.TestConstants.SIMPSON_LEGACY_HOUSEHOLD_IDENTIFIER;
import static uk.gov.dhsc.htbhf.TestConstants.SIMPSON_UC_HOUSEHOLD_IDENTIFIER;
import static uk.gov.dhsc.htbhf.eligibility.model.EligibilityStatus.ELIGIBLE;
import static uk.gov.dhsc.htbhf.eligibility.model.EligibilityStatus.NO_MATCH;

public class EligibilityResponseTestDataFactory {

    public static EligibilityResponse aValidUCEligibilityResponse() {
        return aValidUCEligibilityResponseBuilder()
                .build();
    }

    public static EligibilityResponse aValidLegacyEligibilityResponse() {
        return aValidUCEligibilityResponseBuilder()
                .householdIdentifier(SIMPSON_LEGACY_HOUSEHOLD_IDENTIFIER)
                .build();
    }

    public static EligibilityResponse aNoMatchEligibilityResponse() {
        return EligibilityResponse.builder()
                .eligibilityStatus(NO_MATCH)
                .build();
    }

    public static EligibilityResponse.EligibilityResponseBuilder aValidUCEligibilityResponseBuilder() {
        return EligibilityResponse.builder()
                .eligibilityStatus(ELIGIBLE)
                .numberOfChildrenUnderOne(1)
                .numberOfChildrenUnderFour(2)
                .householdIdentifier(SIMPSON_UC_HOUSEHOLD_IDENTIFIER)
                .children(createChildren());
    }

    private static List<ChildDTO> createChildren() {
        ChildDTO childUnderOne = ChildDTO.builder().dateOfBirth(MAGGIE_DATE_OF_BIRTH).build();
        ChildDTO childUnderFour = ChildDTO.builder().dateOfBirth(LISA_DATE_OF_BIRTH).build();
        return asList(childUnderOne, childUnderFour);
    }
}
package uk.gov.dhsc.htbhf.dwp.factory;

import uk.gov.dhsc.htbhf.dwp.entity.Child;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;
import uk.gov.dhsc.htbhf.eligibility.model.EligibilityStatus;

import java.time.LocalDate;
import java.util.Set;

public class EligibilityResponseFactory {

    public static EligibilityResponse createEligibilityResponse(LegacyHousehold household, EligibilityStatus eligibilityStatus) {
        return EligibilityResponse.builder()
                .numberOfChildrenUnderOne(getNumberOfChildrenUnderOne(household.getChildren()))
                .numberOfChildrenUnderFour(getNumberOfChildrenUnderFour(household.getChildren()))
                .householdIdentifier(household.getHouseholdIdentifier())
                .eligibilityStatus(eligibilityStatus)
                .build();
    }

    public static EligibilityResponse createEligibilityResponse(UCHousehold household, EligibilityStatus eligibilityStatus) {
        return EligibilityResponse.builder()
                .householdIdentifier(household.getHouseholdIdentifier())
                .numberOfChildrenUnderFour(getNumberOfChildrenUnderFour(household.getChildren()))
                .numberOfChildrenUnderOne(getNumberOfChildrenUnderOne(household.getChildren()))
                .eligibilityStatus(eligibilityStatus)
                .build();
    }

    private static Integer getNumberOfChildrenUnderOne(Set<? extends Child> children) {
        return getNumberOfChildrenUnderAgeInYears(children, 1);
    }

    private static Integer getNumberOfChildrenUnderFour(Set<? extends Child> children) {
        return getNumberOfChildrenUnderAgeInYears(children, 4);
    }

    private static Integer getNumberOfChildrenUnderAgeInYears(Set<? extends Child> children, Integer ageInYears) {
        LocalDate pastDate = LocalDate.now().minusYears(ageInYears);
        return Math.toIntExact(children.stream()
                .filter(child -> child.getDateOfBirth().isAfter(pastDate))
                .count());
    }
}

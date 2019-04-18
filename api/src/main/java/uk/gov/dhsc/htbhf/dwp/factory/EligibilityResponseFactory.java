package uk.gov.dhsc.htbhf.dwp.factory;

import org.springframework.stereotype.Component;
import uk.gov.dhsc.htbhf.dwp.entity.Child;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.model.ChildDTO;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;
import uk.gov.dhsc.htbhf.eligibility.model.EligibilityStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Generates an {@link EligibilityResponse} for a given household ({@link LegacyHousehold} or {@link UCHousehold}) and {@link EligibilityStatus}.
 */
@Component
public class EligibilityResponseFactory {

    public EligibilityResponse createEligibilityResponse(LegacyHousehold household, EligibilityStatus eligibilityStatus) {
        return EligibilityResponse.builder()
                .numberOfChildrenUnderOne(getNumberOfChildrenUnderOne(household.getChildren()))
                .numberOfChildrenUnderFour(getNumberOfChildrenUnderFour(household.getChildren()))
                .householdIdentifier(household.getHouseholdIdentifier())
                .children(getChildrenUnderFour(household))
                .eligibilityStatus(eligibilityStatus)
                .build();
    }

    public EligibilityResponse createEligibilityResponse(UCHousehold household, EligibilityStatus eligibilityStatus) {
        return EligibilityResponse.builder()
                .householdIdentifier(household.getHouseholdIdentifier())
                .numberOfChildrenUnderFour(getNumberOfChildrenUnderFour(household.getChildren()))
                .numberOfChildrenUnderOne(getNumberOfChildrenUnderOne(household.getChildren()))
                .children(getChildrenUnderFour(household))
                .eligibilityStatus(eligibilityStatus)
                .build();
    }

    private List<ChildDTO> getChildrenUnderFour(UCHousehold household) {
        return household.getChildren().stream()
                .filter(child -> isUnderFour(child.getDateOfBirth()))
                .map(child -> ChildDTO.builder().dateOfBirth(child.getDateOfBirth()).build())
                .collect(Collectors.toList());
    }

    private List<ChildDTO> getChildrenUnderFour(LegacyHousehold household) {
        return household.getChildren().stream()
                .filter(child -> isUnderFour(child.getDateOfBirth()))
                .map(child -> ChildDTO.builder().dateOfBirth(child.getDateOfBirth()).build())
                .collect(Collectors.toList());
    }

    private boolean isUnderFour(LocalDate dateOfBirth) {
        return dateOfBirth.isAfter(LocalDate.now().minusYears(4));
    }

    private Integer getNumberOfChildrenUnderOne(Set<? extends Child> children) {
        return getNumberOfChildrenUnderAgeInYears(children, 1);
    }

    private Integer getNumberOfChildrenUnderFour(Set<? extends Child> children) {
        return getNumberOfChildrenUnderAgeInYears(children, 4);
    }

    private Integer getNumberOfChildrenUnderAgeInYears(Set<? extends Child> children, Integer ageInYears) {
        LocalDate pastDate = LocalDate.now().minusYears(ageInYears);
        return Math.toIntExact(children.stream()
                .filter(child -> child.getDateOfBirth().isAfter(pastDate))
                .count());
    }
}

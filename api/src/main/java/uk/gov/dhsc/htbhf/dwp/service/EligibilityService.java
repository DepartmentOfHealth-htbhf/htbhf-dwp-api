package uk.gov.dhsc.htbhf.dwp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.gov.dhsc.htbhf.dwp.entity.Child;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.repository.LegacyHouseholdRepository;
import uk.gov.dhsc.htbhf.dwp.repository.UCHouseholdRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Service
public class EligibilityService {

    private static final String ENDPOINT = "/v1/dwp/benefits";
    private final String uri;
    private final RestTemplate restTemplate;
    private final UCHouseholdRepository ucHouseholdRepository;
    private final LegacyHouseholdRepository legacyHouseholdRepository;

    public EligibilityService(@Value("${dwp.base-uri}") String baseUri,
                              RestTemplate restTemplate,
                              UCHouseholdRepository ucHouseholdRepository,
                              LegacyHouseholdRepository legacyHouseholdRepository) {
        this.uri = baseUri + ENDPOINT;
        this.restTemplate = restTemplate;
        this.ucHouseholdRepository = ucHouseholdRepository;
        this.legacyHouseholdRepository = legacyHouseholdRepository;
    }

    public EligibilityResponse checkEligibility(EligibilityRequest eligibilityRequest) {
        String nino = eligibilityRequest.getPerson().getNino();
        Optional<UCHousehold> ucHousehold = ucHouseholdRepository.findHouseholdByAdultWithNino(nino);
        if (ucHousehold.isPresent()) {
            return createEligibilityResponse(ucHousehold.get());
        }

        Optional<LegacyHousehold> legacyHousehold = legacyHouseholdRepository.findHouseholdByAdultWithNino(nino);
        if (legacyHousehold.isPresent()) {
            return createEligibilityResponse(legacyHousehold.get());
        }

        var response = restTemplate.postForEntity(uri, eligibilityRequest, EligibilityResponse.class);
        return response.getBody();
    }

    private EligibilityResponse createEligibilityResponse(LegacyHousehold household) {
        return EligibilityResponse.builder()
                .numberOfChildrenUnderOne(getNumberOfChildrenUnderOne(household.getChildren()))
                .numberOfChildrenUnderFour(getNumberOfChildrenUnderFour(household.getChildren()))
                .householdIdentifier(household.getHouseholdIdentifier())
                .build();
    }

    private EligibilityResponse createEligibilityResponse(UCHousehold household) {
        return EligibilityResponse.builder()
                .earningsThresholdExceeded(household.getEarningsThresholdExceeded())
                .householdIdentifier(household.getHouseholdIdentifier())
                .numberOfChildrenUnderFour(getNumberOfChildrenUnderFour(household.getChildren()))
                .numberOfChildrenUnderOne(getNumberOfChildrenUnderOne(household.getChildren()))
                .build();
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

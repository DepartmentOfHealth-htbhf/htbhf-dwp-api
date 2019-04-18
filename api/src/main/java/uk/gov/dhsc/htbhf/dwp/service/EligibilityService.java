package uk.gov.dhsc.htbhf.dwp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.factory.EligibilityResponseFactory;
import uk.gov.dhsc.htbhf.dwp.model.DWPEligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.repository.LegacyHouseholdRepository;
import uk.gov.dhsc.htbhf.dwp.repository.UCHouseholdRepository;

import java.util.Optional;

import static uk.gov.dhsc.htbhf.eligibility.model.EligibilityStatus.ELIGIBLE;
import static uk.gov.dhsc.htbhf.eligibility.model.EligibilityStatus.NO_MATCH;

@Service
@Slf4j
public class EligibilityService {

    private static final String ENDPOINT = "/v1/dwp/benefits";
    private final String uri;
    private final RestTemplate restTemplate;
    private final UCHouseholdRepository ucHouseholdRepository;
    private final LegacyHouseholdRepository legacyHouseholdRepository;
    private final HouseholdVerifier householdVerifier;
    private final EligibilityResponseFactory eligibilityResponseFactory;

    public EligibilityService(@Value("${dwp.base-uri}") String baseUri,
                              RestTemplate restTemplate,
                              UCHouseholdRepository ucHouseholdRepository,
                              LegacyHouseholdRepository legacyHouseholdRepository,
                              HouseholdVerifier householdVerifier,
                              EligibilityResponseFactory eligibilityResponseFactory) {
        this.uri = baseUri + ENDPOINT;
        this.restTemplate = restTemplate;
        this.ucHouseholdRepository = ucHouseholdRepository;
        this.legacyHouseholdRepository = legacyHouseholdRepository;
        this.householdVerifier = householdVerifier;
        this.eligibilityResponseFactory = eligibilityResponseFactory;
    }

    /**
     * Checks if a given request is eligible. First check the Universal credit database,
     * then the legacy database, then call the dwp api.
     * Checking UC database first as most data is held there.
     *
     * @param eligibilityRequest The eligibility request
     * @return The eligibility response
     */
    public EligibilityResponse checkEligibility(DWPEligibilityRequest eligibilityRequest) {
        String nino = eligibilityRequest.getPerson().getNino();
        Optional<UCHousehold> ucHousehold = ucHouseholdRepository.findHouseholdByAdultWithNino(nino);
        if (ucHousehold.isPresent()) {
            log.debug("Matched UC household: {}", ucHousehold.get().getHouseholdIdentifier());
            return getEligibilityResponse(eligibilityRequest, ucHousehold.get());
        }

        Optional<LegacyHousehold> legacyHousehold = legacyHouseholdRepository.findHouseholdByAdultWithNino(nino);
        if (legacyHousehold.isPresent()) {
            log.debug("Matched legacy household: {}", legacyHousehold.get().getHouseholdIdentifier());
            return getEligibilityResponse(eligibilityRequest, legacyHousehold.get());
        }

        log.debug("No match found in db - calling DWP to check eligibility");
        var response = restTemplate.postForEntity(uri, eligibilityRequest, EligibilityResponse.class);
        return response.getBody();
    }

    private EligibilityResponse getEligibilityResponse(DWPEligibilityRequest eligibilityRequest, UCHousehold ucHousehold) {
        return householdVerifier.detailsMatch(ucHousehold, eligibilityRequest.getPerson())
                ? eligibilityResponseFactory.createEligibilityResponse(ucHousehold, ELIGIBLE)
                : EligibilityResponse.builder().eligibilityStatus(NO_MATCH).build();
    }

    private EligibilityResponse getEligibilityResponse(DWPEligibilityRequest eligibilityRequest, LegacyHousehold legacyHousehold) {
        return householdVerifier.detailsMatch(legacyHousehold, eligibilityRequest.getPerson())
                ? eligibilityResponseFactory.createEligibilityResponse(legacyHousehold, ELIGIBLE)
                : EligibilityResponse.builder().eligibilityStatus(NO_MATCH).build();
    }
}

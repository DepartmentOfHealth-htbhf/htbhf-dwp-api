package uk.gov.dhsc.htbhf.dwp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.repository.LegacyHouseholdRepository;
import uk.gov.dhsc.htbhf.dwp.repository.UCHouseholdRepository;

import java.util.Optional;

import static uk.gov.dhsc.htbhf.dwp.factory.EligibilityResponseFactory.createEligibilityResponse;
import static uk.gov.dhsc.htbhf.dwp.model.EligibilityStatus.NOMATCH;


@Service
public class EligibilityService {

    private static final String ENDPOINT = "/v1/dwp/benefits";
    private final String uri;
    private final RestTemplate restTemplate;
    private final UCHouseholdRepository ucHouseholdRepository;
    private final LegacyHouseholdRepository legacyHouseholdRepository;
    private final HouseholdVerifier householdVerifier;

    public EligibilityService(@Value("${dwp.base-uri}") String baseUri,
                              RestTemplate restTemplate,
                              UCHouseholdRepository ucHouseholdRepository,
                              LegacyHouseholdRepository legacyHouseholdRepository,
                              HouseholdVerifier householdVerifier) {
        this.uri = baseUri + ENDPOINT;
        this.restTemplate = restTemplate;
        this.ucHouseholdRepository = ucHouseholdRepository;
        this.legacyHouseholdRepository = legacyHouseholdRepository;
        this.householdVerifier = householdVerifier;
    }

    /**
     * Checks if a given request is eligible. First check the Universal credit database,
     * then the legacy database, then call the dwp api.
     * Checking UC database first as most data is held there.
     */
    public EligibilityResponse checkEligibility(EligibilityRequest eligibilityRequest) {
        String nino = eligibilityRequest.getPerson().getNino();
        Optional<UCHousehold> ucHousehold = ucHouseholdRepository.findHouseholdByAdultWithNino(nino);
        if (ucHousehold.isPresent()) {
            return householdVerifier.detailsMatch(ucHousehold.get(), eligibilityRequest.getPerson())
                    ? createEligibilityResponse(ucHousehold.get())
                    : EligibilityResponse.builder().eligibilityStatus(NOMATCH).build();
        }

        Optional<LegacyHousehold> legacyHousehold = legacyHouseholdRepository.findHouseholdByAdultWithNino(nino);
        if (legacyHousehold.isPresent()) {
            return householdVerifier.detailsMatch(legacyHousehold.get(), eligibilityRequest.getPerson())
                    ? createEligibilityResponse(legacyHousehold.get())
                    : EligibilityResponse.builder().eligibilityStatus(NOMATCH).build();
        }

        var response = restTemplate.postForEntity(uri, eligibilityRequest, EligibilityResponse.class);
        return response.getBody();
    }
}

package uk.gov.dhsc.htbhf.dwp.service.v2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.factory.v2.IdentityAndEligibilityResponseFactory;
import uk.gov.dhsc.htbhf.dwp.model.v2.DWPEligibilityRequestV2;
import uk.gov.dhsc.htbhf.dwp.model.v2.IdentityAndEligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.repository.v1.UCHouseholdRepository;

import java.util.Optional;

@Service
@Slf4j
public class IdentityAndEligibilityService {

    private static final String DWP_ENDPOINT = "/v2/dwp/benefits";
    private final String uri;
    private final UCHouseholdRepository ucHouseholdRepository;
    private final IdentityAndEligibilityResponseFactory responseFactory;
    private final RestTemplate restTemplate;

    public IdentityAndEligibilityService(@Value("${dwp.base-uri}") String baseUri,
                                         RestTemplate restTemplate,
                                         UCHouseholdRepository ucHouseholdRepository,
                                         IdentityAndEligibilityResponseFactory responseFactory) {
        this.uri = baseUri + DWP_ENDPOINT;
        this.ucHouseholdRepository = ucHouseholdRepository;
        this.responseFactory = responseFactory;
        this.restTemplate = restTemplate;
    }

    /**
     * Checks the identity and eligibility of the given request. First check the local database,
     * then call the dwp api if no entries are found.
     *
     * @param eligibilityRequest The eligibility request
     * @return The eligibility response
     */
    public IdentityAndEligibilityResponse checkIdentityAndEligibility(DWPEligibilityRequestV2 eligibilityRequest) {
        String nino = eligibilityRequest.getPerson().getNino();
        Optional<UCHousehold> ucHousehold = ucHouseholdRepository.findHouseholdByAdultWithNino(nino);
        if (ucHousehold.isPresent()) {
            log.debug("Matched UC household: {}", ucHousehold.get().getId());
            return responseFactory.determineIdentityAndEligibilityResponse(ucHousehold.get(), eligibilityRequest);
        }

        log.debug("No match found in db - calling DWP to check identity and eligibility");
        ResponseEntity<IdentityAndEligibilityResponse> response = restTemplate.postForEntity(uri, eligibilityRequest, IdentityAndEligibilityResponse.class);
        return response.getBody();
    }

}

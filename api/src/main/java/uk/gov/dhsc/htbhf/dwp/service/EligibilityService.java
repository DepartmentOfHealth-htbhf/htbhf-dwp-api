package uk.gov.dhsc.htbhf.dwp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;

@Service
public class EligibilityService {

    private final String uri;
    private final RestTemplate restTemplate;

    public EligibilityService(@Value("${dwp.uri}") String uri, RestTemplate restTemplate) {
        this.uri = uri;
        this.restTemplate = restTemplate;
    }

    public EligibilityResponse checkEligibility(EligibilityRequest eligibilityRequest) {
        var response = restTemplate.postForEntity(uri, eligibilityRequest, EligibilityResponse.class);
        return response.getBody();
    }
}

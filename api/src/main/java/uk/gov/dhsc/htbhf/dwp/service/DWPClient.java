package uk.gov.dhsc.htbhf.dwp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;

@Component
public class DWPClient {

    private final String uri;
    private final RestTemplate restTemplate;

    public DWPClient(@Value("${dwp.uri}") String uri, RestTemplate restTemplate) {
        this.uri = uri;
        this.restTemplate = restTemplate;
    }

    public EligibilityResponse checkEligibility(EligibilityRequest eligibilityRequest) {
        var response = restTemplate.postForEntity(uri, eligibilityRequest, EligibilityResponse.class);
        return response.getBody();
    }
}

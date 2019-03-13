package uk.gov.dhsc.htbhf.dwp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.OK;
import static uk.gov.dhsc.htbhf.dwp.helper.EligibilityRequestTestFactory.anEligibilityRequest;
import static uk.gov.dhsc.htbhf.dwp.helper.EligibilityResponseTestFactory.anEligibilityResponse;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class DWPClientTest {

    @Value("${dwp.uri}")
    private String dwpUri;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private DWPClient dwpClient;

    @Test
    void shouldCallEndpoint() {
        var eligibilityRequest = anEligibilityRequest();
        given(restTemplate.postForEntity(anyString(), any(), any()))
                .willReturn(new ResponseEntity<>(anEligibilityResponse(), OK));

        var result = dwpClient.checkEligibility(eligibilityRequest);

        assertThat(result).isEqualTo(anEligibilityResponse());
        verify(restTemplate).postForEntity(dwpUri, eligibilityRequest, EligibilityResponse.class);
    }

}

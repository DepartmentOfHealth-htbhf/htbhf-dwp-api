package uk.gov.dhsc.htbhf.dwp.controller.v2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.dhsc.htbhf.dwp.model.v2.IdentityAndEligibilityResponse;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v2.HttpRequestTestDataFactory.aValidEligibilityHttpEntity;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DWPBenefitControllerV2Test {

    private static final URI ENDPOINT = URI.create("/v2/dwp/eligibility");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnResponse() {
        //Given
        HttpEntity request = aValidEligibilityHttpEntity();

        //When
        ResponseEntity<IdentityAndEligibilityResponse> responseEntity = restTemplate.exchange(ENDPOINT,
                HttpMethod.GET, request, IdentityAndEligibilityResponse.class);

        //Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        IdentityAndEligibilityResponse expectedResponse = IdentityAndEligibilityResponse.builder().build();
        assertThat(responseEntity.getBody()).isEqualTo(expectedResponse);
    }

}

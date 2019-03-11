package uk.gov.dhsc.htbhf.dwp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;

import java.net.URI;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DWPEligibilityControllerIntegrationTest {

    private static final URI ENDPOINT = URI.create("/v1/dwp/eligibility");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnNoBenefitsAndNoChildrenForMatchingNino() {
        PersonDTO person = PersonDTO.builder()
                .nino("QQ123456A")
                .dateOfBirth(LocalDate.of(1990,1,1))
                .build();

        ResponseEntity<String> response = restTemplate.postForEntity(ENDPOINT, person, String.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNull();
    }

}
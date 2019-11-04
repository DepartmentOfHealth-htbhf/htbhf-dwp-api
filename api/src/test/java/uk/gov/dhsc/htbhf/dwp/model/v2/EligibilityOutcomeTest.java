package uk.gov.dhsc.htbhf.dwp.model.v2;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class EligibilityOutcomeTest {

    @ParameterizedTest
    @CsvSource({
            "CONFIRMED, confirmed",
            "NOT_CONFIRMED, not_confirmed",
            "NOT_SET, not_set"
    })
    void shouldGetResponseValue(EligibilityOutcome eligibilityOutcome, String expectedResponseValue) {
        assertThat(eligibilityOutcome.getResponseValue()).isEqualTo(expectedResponseValue);
    }

}

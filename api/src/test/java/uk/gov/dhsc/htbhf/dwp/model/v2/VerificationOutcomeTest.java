package uk.gov.dhsc.htbhf.dwp.model.v2;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class VerificationOutcomeTest {

    @ParameterizedTest
    @CsvSource({
            "MATCHED, matched",
            "NOT_MATCHED, not_matched",
            "NOT_HELD, not_held",
            "NOT_SUPPLIED, not_supplied",
            "INVALID_FORMAT, invalid_format",
            "NOT_SET, not_set"
    })
    void shouldGetResponseValue(VerificationOutcome verificationOutcome, String expectedResponseValue) {
        assertThat(verificationOutcome.getResponseValue()).isEqualTo(expectedResponseValue);
    }

}

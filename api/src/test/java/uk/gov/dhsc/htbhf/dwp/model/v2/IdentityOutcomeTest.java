package uk.gov.dhsc.htbhf.dwp.model.v2;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class IdentityOutcomeTest {

    @ParameterizedTest
    @CsvSource({
            "MATCHED, matched",
            "NOT_MATCHED, not_matched"
    })
    void shouldGetResponseValue(IdentityOutcome identityOutcome, String expectedResponseValue) {
        assertThat(identityOutcome.getResponseValue()).isEqualTo(expectedResponseValue);
    }

}

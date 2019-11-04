package uk.gov.dhsc.htbhf.dwp.model.v2;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DeathVerificationFlagTest {

    @ParameterizedTest
    @CsvSource({
            "N_A, n/a",
            "DEATH_NOT_VERIFIED, death_not_verified",
            "LIMITED_SUPPORTING_DOCUMENTATION, limited_supporting_documentation",
            "PARTIAL_SUPPORTING_DOCUMENTATION, partial_supporting_documentation",
            "FULL_SUPPORTING_DOCUMENTATION, full_supporting_documentation"
    })
    void shouldGetResponseValue(DeathVerificationFlag deathVerificationFlag, String expectedResponseValue) {
        assertThat(deathVerificationFlag.getResponseValue()).isEqualTo(expectedResponseValue);
    }

}

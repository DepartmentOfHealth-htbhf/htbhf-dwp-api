package uk.gov.dhsc.htbhf.dwp.model.v2;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class QualifyingBenefitsTest {

    @ParameterizedTest
    @CsvSource({
            "UNIVERSAL_CREDIT, universal_credit",
            "EMPLOYMENT_AND_SUPPORT_ALLOWANCE, employment_and_support_allowance",
            "INCOME_SUPPORT, income_support",
            "JOBSEEKERS_ALLOWANCE, jobseekers_allowance",
            "PENSION_CREDIT, pension_credit",
            "NOT_SET, not_set"
    })
    void shouldGetResponseValue(QualifyingBenefits qualifyingBenefits, String expectedResponseValue) {
        assertThat(qualifyingBenefits.getResponseValue()).isEqualTo(expectedResponseValue);
    }

}

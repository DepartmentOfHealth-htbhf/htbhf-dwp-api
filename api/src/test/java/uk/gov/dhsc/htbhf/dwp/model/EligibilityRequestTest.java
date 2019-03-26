package uk.gov.dhsc.htbhf.dwp.model;

import org.junit.jupiter.api.Test;
import uk.gov.dhsc.htbhf.assertions.AbstractValidationTest;

import java.util.Set;
import javax.validation.ConstraintViolation;

import static uk.gov.dhsc.htbhf.assertions.ConstraintViolationAssert.assertThat;
import static uk.gov.dhsc.htbhf.dwp.factory.EligibilityRequestTestDataFactory.*;

class EligibilityRequestTest extends AbstractValidationTest {

    @Test
    public void shouldValidateEligibilityRequestSuccessfully() {
        var request = aValidEligibilityRequest();

        Set<ConstraintViolation<EligibilityRequest>> violations = validator.validate(request);

        assertThat(violations).hasNoViolations();
    }

    @Test
    public void shouldFailToValidateEligibilityRequestWithInvalidPerson() {
        var request = anEligibilityRequestWithInvalidPerson();

        Set<ConstraintViolation<EligibilityRequest>> violations = validator.validate(request);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "person.nino");
    }

    @Test
    public void shouldFailToValidateEligibilityRequestWithNoPerson() {
        var request = anEligibilityRequestWithPerson(null);

        Set<ConstraintViolation<EligibilityRequest>> violations = validator.validate(request);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "person");
    }

    @Test
    public void shouldFailToValidateEligibilityRequestWithNoUcMonthlyIncomeThreshold() {
        var request = anEligibilityRequestWithUcMonthlyIncomeThreshold(null);

        Set<ConstraintViolation<EligibilityRequest>> violations = validator.validate(request);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "ucMonthlyIncomeThreshold");
    }

    @Test
    public void shouldFailToValidateEligibilityRequestWithNoEligibleStartDate() {
        var request = anEligibilityRequestWithEligibleStartDate(null);

        Set<ConstraintViolation<EligibilityRequest>> violations = validator.validate(request);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "eligibleStartDate");
    }

    @Test
    public void shouldFailToValidateEligibilityRequestWithNoEligibleEndDate() {
        var request = anEligibilityRequestWithEligibleEndDate(null);

        Set<ConstraintViolation<EligibilityRequest>> violations = validator.validate(request);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "eligibleEndDate");
    }

}
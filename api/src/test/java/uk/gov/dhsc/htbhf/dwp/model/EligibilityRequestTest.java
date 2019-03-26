package uk.gov.dhsc.htbhf.dwp.model;

import org.junit.jupiter.api.Test;
import uk.gov.dhsc.htbhf.assertions.AbstractValidationTest;

import java.util.Set;
import javax.validation.ConstraintViolation;

import static uk.gov.dhsc.htbhf.assertions.ConstraintViolationAssert.assertThat;
import static uk.gov.dhsc.htbhf.dwp.factory.EligibilityRequestTestDataFactory.*;
import static uk.gov.dhsc.htbhf.dwp.factory.PersonDTOTestDataFactory.aPersonWithNino;

class EligibilityRequestTest extends AbstractValidationTest {

    @Test
    public void shouldValidateEligibilityRequestSuccessfully() {
        EligibilityRequest request = aValidEligibilityRequest();

        Set<ConstraintViolation<EligibilityRequest>> violations = validator.validate(request);

        assertThat(violations).hasNoViolations();
    }

    @Test
    public void shouldFailToValidateEligibilityRequestWithInvalidPerson() {
        PersonDTO person = aPersonWithNino(null);
        EligibilityRequest request = anEligibilityRequestWithPerson(person);

        Set<ConstraintViolation<EligibilityRequest>> violations = validator.validate(request);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "person.nino");
    }

    @Test
    public void shouldFailToValidateEligibilityRequestWithNoPerson() {
        EligibilityRequest request = anEligibilityRequestWithPerson(null);

        Set<ConstraintViolation<EligibilityRequest>> violations = validator.validate(request);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "person");
    }

    @Test
    public void shouldFailToValidateEligibilityRequestWithNoUcMonthlyIncomeThreshold() {
        EligibilityRequest request = anEligibilityRequestWithUcMonthlyIncomeThreshold(null);

        Set<ConstraintViolation<EligibilityRequest>> violations = validator.validate(request);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "ucMonthlyIncomeThreshold");
    }

    @Test
    public void shouldFailToValidateEligibilityRequestWithNoEligibleStartDate() {
        EligibilityRequest request = anEligibilityRequestWithEligibleStartDate(null);

        Set<ConstraintViolation<EligibilityRequest>> violations = validator.validate(request);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "eligibleStartDate");
    }

    @Test
    public void shouldFailToValidateEligibilityRequestWithNoEligibleEndDate() {
        EligibilityRequest request = anEligibilityRequestWithEligibleEndDate(null);

        Set<ConstraintViolation<EligibilityRequest>> violations = validator.validate(request);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "eligibleEndDate");
    }

}
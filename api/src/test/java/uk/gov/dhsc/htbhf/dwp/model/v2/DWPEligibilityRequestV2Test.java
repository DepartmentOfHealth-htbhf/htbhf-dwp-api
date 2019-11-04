package uk.gov.dhsc.htbhf.dwp.model.v2;

import org.junit.jupiter.api.Test;
import uk.gov.dhsc.htbhf.assertions.AbstractValidationTest;

import java.util.Set;
import javax.validation.ConstraintViolation;

import static uk.gov.dhsc.htbhf.assertions.ConstraintViolationAssert.assertThat;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v2.DWPEligibilityRequestV2TestDataFactory.aValidDWPEligibilityRequestV2;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v2.DWPEligibilityRequestV2TestDataFactory.aValidDWPEligibilityRequestV2WithEligibilityEndDate;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v2.DWPEligibilityRequestV2TestDataFactory.aValidDWPEligibilityRequestV2WithPerson;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v2.PersonDTOV2TestDataFactory.aPersonDTOV2WithSurname;

class DWPEligibilityRequestV2Test extends AbstractValidationTest {

    @Test
    void shouldSuccessfullyValidateRequest() {
        DWPEligibilityRequestV2 request = aValidDWPEligibilityRequestV2();
        Set<ConstraintViolation<DWPEligibilityRequestV2>> violations = validator.validate(request);
        assertThat(violations).hasNoViolations();
    }

    @Test
    void shouldFailValidationWithNoPerson() {
        DWPEligibilityRequestV2 request = aValidDWPEligibilityRequestV2WithPerson(null);
        Set<ConstraintViolation<DWPEligibilityRequestV2>> violations = validator.validate(request);
        assertThat(violations).hasSingleConstraintViolation("must not be null", "person");
    }

    @Test
    void shouldFailValidationWithInvalidPerson() {
        PersonDTOV2 person = aPersonDTOV2WithSurname(null);
        DWPEligibilityRequestV2 request = aValidDWPEligibilityRequestV2WithPerson(person);
        Set<ConstraintViolation<DWPEligibilityRequestV2>> violations = validator.validate(request);
        assertThat(violations).hasSingleConstraintViolation("must not be null", "person.surname");
    }

    @Test
    void shouldFailValidationWithInvalidEligibilityEndDate() {
        DWPEligibilityRequestV2 request = aValidDWPEligibilityRequestV2WithEligibilityEndDate(null);
        Set<ConstraintViolation<DWPEligibilityRequestV2>> violations = validator.validate(request);
        assertThat(violations).hasSingleConstraintViolation("must not be null", "eligibilityEndDate");
    }

}

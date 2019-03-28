package uk.gov.dhsc.htbhf.dwp.model;

import org.junit.jupiter.api.Test;
import uk.gov.dhsc.htbhf.assertions.AbstractValidationTest;

import java.time.LocalDate;
import java.util.Set;
import javax.validation.ConstraintViolation;

import static uk.gov.dhsc.htbhf.assertions.ConstraintViolationAssert.assertThat;
import static uk.gov.dhsc.htbhf.dwp.factory.AddressDTOTestDataFactory.anAddressWithAddressLine1;
import static uk.gov.dhsc.htbhf.dwp.factory.DWPPersonDTOTestDataFactory.*;

class DWPPersonDTOTest extends AbstractValidationTest {

    @Test
    public void shouldValidateDWPPersonSuccessfully() {
        DWPPersonDTO person = aValidDWPPerson();

        Set<ConstraintViolation<DWPPersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasNoViolations();
    }

    @Test
    public void shouldFailToValidateWithNoFirstName() {
        DWPPersonDTO person = aDWPPersonWithForename(null);

        Set<ConstraintViolation<DWPPersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "forename");
    }

    @Test
    public void shouldFailToValidateWithNoSurName() {
        DWPPersonDTO person = aDWPPersonWithSurname(null);

        Set<ConstraintViolation<DWPPersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "surname");
    }

    @Test
    public void shouldFailToValidateWithNoDateOfBirth() {
        DWPPersonDTO person = aDWPPersonWithDateOfBirth(null);

        Set<ConstraintViolation<DWPPersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "dateOfBirth");
    }

    @Test
    public void shouldFailToValidateWithDateOfBirthInFuture() {
        DWPPersonDTO person = aDWPPersonWithDateOfBirth(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<DWPPersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasSingleConstraintViolation("must be a past date", "dateOfBirth");
    }

    @Test
    public void shouldFailToValidateWithNoAddress() {
        DWPPersonDTO person = aDWPPersonWithAddress(null);

        Set<ConstraintViolation<DWPPersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "address");
    }

    @Test
    public void shouldFailToValidateWithInvalidAddress() {
        DWPPersonDTO person = aDWPPersonWithAddress(anAddressWithAddressLine1(null));

        Set<ConstraintViolation<DWPPersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "address.addressLine1");
    }
}

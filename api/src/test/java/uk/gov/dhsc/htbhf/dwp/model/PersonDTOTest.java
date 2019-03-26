package uk.gov.dhsc.htbhf.dwp.model;

import org.junit.jupiter.api.Test;
import uk.gov.dhsc.htbhf.assertions.AbstractValidationTest;

import java.time.LocalDate;
import java.util.Set;
import javax.validation.ConstraintViolation;

import static uk.gov.dhsc.htbhf.assertions.ConstraintViolationAssert.assertThat;
import static uk.gov.dhsc.htbhf.dwp.factory.AddressDTOTestDataFactory.aValidAddressBuilder;
import static uk.gov.dhsc.htbhf.dwp.factory.PersonDTOTestDataFactory.*;

class PersonDTOTest extends AbstractValidationTest {

    @Test
    public void shouldValidatePersonSuccessfully() {
        var person = aValidPerson();

        Set<ConstraintViolation<PersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasNoViolations();
    }

    @Test
    public void shouldFailToValidateWithNoForename() {
        var person = aPersonWithForename(null);

        Set<ConstraintViolation<PersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "forename");
    }

    @Test
    public void shouldFailToValidateWithNoSurname() {
        var person = aPersonWithSurname(null);

        Set<ConstraintViolation<PersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "surname");
    }

    @Test
    public void shouldFailToValidateWithNoNino() {
        var person = aPersonWithNino(null);

        Set<ConstraintViolation<PersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "nino");
    }

    @Test
    public void shouldFailToValidateWithNoDateOfBirth() {
        var person = aPersonWithDateOfBirth(null);

        Set<ConstraintViolation<PersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "dateOfBirth");
    }

    @Test
    public void shouldFailToValidateWithDateOfBirthInFuture() {
        var person = aPersonWithDateOfBirth(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<PersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasSingleConstraintViolation("must be a past date", "dateOfBirth");
    }

    @Test
    public void shouldFailToValidateWithNoAddress() {
        var person = aPersonWithAddress(null);

        Set<ConstraintViolation<PersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "address");
    }

    @Test
    public void shouldFailToValidateWithInvalidAddress() {
        var address = aValidAddressBuilder().addressLine1(null).build();
        var person = aPersonWithAddress(address);

        Set<ConstraintViolation<PersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "address.addressLine1");
    }
}
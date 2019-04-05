package uk.gov.dhsc.htbhf.dwp.model;

import org.junit.jupiter.api.Test;
import uk.gov.dhsc.htbhf.assertions.AbstractValidationTest;

import java.time.LocalDate;
import java.util.Set;
import javax.validation.ConstraintViolation;

import static uk.gov.dhsc.htbhf.assertions.ConstraintViolationAssert.assertThat;
import static uk.gov.dhsc.htbhf.dwp.testhelper.AddressDTOTestDataFactory.anAddressWithAddressLine1;
import static uk.gov.dhsc.htbhf.dwp.testhelper.PersonDTOTestDataFactory.*;

class PersonDTOTest extends AbstractValidationTest {

    @Test
    public void shouldValidatePersonSuccessfully() {
        PersonDTO person = aValidPerson();

        Set<ConstraintViolation<PersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasNoViolations();
    }

    @Test
    public void shouldFailToValidateWithNoFirstName() {
        PersonDTO person = aPersonWithFirstName(null);

        Set<ConstraintViolation<PersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "firstName");
    }

    @Test
    public void shouldFailToValidateWithNoLastName() {
        PersonDTO person = aPersonWithLastName(null);

        Set<ConstraintViolation<PersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "lastName");
    }

    @Test
    public void shouldFailToValidateWithNoNino() {
        PersonDTO person = aPersonWithNino(null);

        Set<ConstraintViolation<PersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "nino");
    }

    @Test
    public void shouldFailToValidateWithNoDateOfBirth() {
        PersonDTO person = aPersonWithDateOfBirth(null);

        Set<ConstraintViolation<PersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "dateOfBirth");
    }

    @Test
    public void shouldFailToValidateWithDateOfBirthInFuture() {
        PersonDTO person = aPersonWithDateOfBirth(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<PersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasSingleConstraintViolation("must be a past date", "dateOfBirth");
    }

    @Test
    public void shouldFailToValidateWithNoAddress() {
        PersonDTO person = aPersonWithAddress(null);

        Set<ConstraintViolation<PersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "address");
    }

    @Test
    public void shouldFailToValidateWithInvalidAddress() {
        AddressDTO address = anAddressWithAddressLine1(null);
        PersonDTO person = aPersonWithAddress(address);

        Set<ConstraintViolation<PersonDTO>> violations = validator.validate(person);

        assertThat(violations).hasSingleConstraintViolation("must not be null", "address.addressLine1");
    }
}

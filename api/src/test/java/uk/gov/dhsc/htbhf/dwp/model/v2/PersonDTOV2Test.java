package uk.gov.dhsc.htbhf.dwp.model.v2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.dhsc.htbhf.assertions.AbstractValidationTest;

import java.time.LocalDate;
import java.util.Set;
import javax.validation.ConstraintViolation;

import static uk.gov.dhsc.htbhf.assertions.ConstraintViolationAssert.assertThat;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v2.PersonDTOV2TestDataFactory.*;

class PersonDTOV2Test extends AbstractValidationTest {

    @Test
    void shouldSuccessfullyValidatePersonWithMandatoryFieldsOnly() {
        PersonDTOV2 personDTOV2 = aValidPersonDTOV2WithMandatoryFieldsOnly();
        Set<ConstraintViolation<PersonDTOV2>> violations = validator.validate(personDTOV2);
        assertThat(violations).hasNoViolations();
    }

    @Test
    void shouldSuccessfullyValidatePersonWithAllFields() {
        PersonDTOV2 personDTOV2 = aValidPersonDTOV2();
        Set<ConstraintViolation<PersonDTOV2>> violations = validator.validate(personDTOV2);
        assertThat(violations).hasNoViolations();
    }

    @Test
    void shouldFailValidationWithMissingSurname() {
        PersonDTOV2 personDTOV2 = aPersonDTOV2WithSurname(null);
        Set<ConstraintViolation<PersonDTOV2>> violations = validator.validate(personDTOV2);
        assertThat(violations).hasSingleConstraintViolation("must not be null", "surname");
    }

    @Test
    void shouldFailValidationWithMissingNino() {
        PersonDTOV2 personDTOV2 = aPersonDTOV2WithNino(null);
        Set<ConstraintViolation<PersonDTOV2>> violations = validator.validate(personDTOV2);
        assertThat(violations).hasSingleConstraintViolation("must not be null", "nino");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "YYHU456781",
            "888888888",
            "ABCDEFGHI",
            "ZQQ123456CZ",
            "QQ123456T",
            "ZZ999999D"
    })
    void shouldFailValidationWithInvalidNino(String nino) {
        PersonDTOV2 personDTOV2 = aPersonDTOV2WithNino(nino);
        Set<ConstraintViolation<PersonDTOV2>> violations = validator.validate(personDTOV2);
        assertThat(violations).hasSingleConstraintViolation(
                "must match \"^(?!BG|GB|NK|KN|TN|NT|ZZ)[A-CEGHJ-PR-TW-Z][A-CEGHJ-NPR-TW-Z](\\d{6})[A-D]$\"", "nino");
    }

    @Test
    void shouldFailValidationWithMissingDateOfBirth() {
        PersonDTOV2 personDTOV2 = aPersonDTOV2WithDateOfBirth(null);
        Set<ConstraintViolation<PersonDTOV2>> violations = validator.validate(personDTOV2);
        assertThat(violations).hasSingleConstraintViolation("must not be null", "dateOfBirth");
    }

    @Test
    void shouldFailValidationWithDateOfBirthInFuture() {
        PersonDTOV2 personDTOV2 = aPersonDTOV2WithDateOfBirth(LocalDate.now().plusYears(1));
        Set<ConstraintViolation<PersonDTOV2>> violations = validator.validate(personDTOV2);
        assertThat(violations).hasSingleConstraintViolation("must be a past date", "dateOfBirth");
    }

    @Test
    void shouldFailValidationWithMissingAddressLine1() {
        PersonDTOV2 personDTOV2 = aPersonDTOV2WithAddressLine1(null);
        Set<ConstraintViolation<PersonDTOV2>> violations = validator.validate(personDTOV2);
        assertThat(violations).hasSingleConstraintViolation("must not be null", "addressLine1");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "AA1122BB",
            "A",
            "11AA21",
    })
    void shouldFailValidationWithInvalidPostcode(String postcode) {
        PersonDTOV2 personDTOV2 = aPersonDTOV2WithPostcode(postcode);
        Set<ConstraintViolation<PersonDTOV2>> violations = validator.validate(personDTOV2);
        assertThat(violations).hasSingleConstraintViolation(
                "must match \"^(GIR ?0AA|[A-PR-UWYZ]([0-9]{1,2}|([A-HK-Y][0-9]([0-9ABEHMNPRV-Y])?)|[0-9][A-HJKPS-UW]) ?[0-9][ABD-HJLNP-UW-Z]{2})$\"",
                "postcode");
    }

    @Test
    void shouldFailValidationWithMissingPostcode() {
        PersonDTOV2 personDTOV2 = aPersonDTOV2WithPostcode(null);
        Set<ConstraintViolation<PersonDTOV2>> violations = validator.validate(personDTOV2);
        assertThat(violations).hasSingleConstraintViolation("must not be null", "postcode");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "plainaddress",
            "#@%^%#$@#$@#.com",
            "@domain.com"
    })
    void shouldFailValidationWithInvalidEmailAddress(String emailAddress) {
        PersonDTOV2 personDTOV2 = aPersonDTOV2WithEmailAddress(emailAddress);
        Set<ConstraintViolation<PersonDTOV2>> violations = validator.validate(personDTOV2);
        assertThat(violations).hasSingleConstraintViolation(
                "must match \"(^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$)\"",
                "emailAddress");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "plainaddress",
            "#@%^%#$@#$@#.com",
            "@domain.com"
    })
    void shouldFailValidationWithInvalidMobilePhoneNumber(String mobilePhoneNumber) {
        PersonDTOV2 personDTOV2 = aPersonDTOV2WithMobilePhoneNumber(mobilePhoneNumber);
        Set<ConstraintViolation<PersonDTOV2>> violations = validator.validate(personDTOV2);
        assertThat(violations).hasSingleConstraintViolation(
                "must match \"^\\+44\\d{9,10}$\"",
                "mobilePhoneNumber");
    }

    @Test
    void shouldFailValidationWithPregnantDependantDobInFuture() {
        PersonDTOV2 personDTOV2 = aPersonDTOV2WithPregnantDependantDob(LocalDate.now().plusYears(1));
        Set<ConstraintViolation<PersonDTOV2>> violations = validator.validate(personDTOV2);
        assertThat(violations).hasSingleConstraintViolation("must be a past date", "pregnantDependentDob");
    }

}

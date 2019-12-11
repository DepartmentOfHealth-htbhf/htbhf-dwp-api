package uk.gov.dhsc.htbhf.dwp.factory.v2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCAdult;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;
import uk.gov.dhsc.htbhf.dwp.model.VerificationOutcome;

import java.util.function.BiPredicate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dhsc.htbhf.TestConstants.*;
import static uk.gov.dhsc.htbhf.dwp.testhelper.PersonDTOTestDataFactory.aPersonDTOWithDateOfBirth;
import static uk.gov.dhsc.htbhf.dwp.testhelper.PersonDTOTestDataFactory.aPersonDTOWithNino;
import static uk.gov.dhsc.htbhf.dwp.testhelper.PersonDTOTestDataFactory.aPersonDTOWithSurname;
import static uk.gov.dhsc.htbhf.dwp.testhelper.PersonDTOTestDataFactory.aValidPersonDTO;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v2.UCHouseholdTestDataFactoryV2.HOMER;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v2.UCHouseholdTestDataFactoryV2.aUCAdult;

class IdentityVerificationUtilsTest {

    @ParameterizedTest(name = "Seeded value={0}, request value={1}, predicateReturn={2}")
    @MethodSource("determineVerificationOutcomeParams")
    void shouldDetermineVerificationOutcome(String seededValue, String requestValue, boolean predicateReturn, VerificationOutcome expectedOutcome) {
        //Given
        BiPredicate<String, String> biPredicate = (s, s2) -> predicateReturn;
        //When
        VerificationOutcome outcome = IdentityVerificationUtils.determineVerificationOutcome(seededValue, requestValue, biPredicate);
        //Then
        assertThat(outcome).isEqualTo(expectedOutcome);
    }

    @ParameterizedTest
    @MethodSource("verifyEqualsIgnoringWhitespaceParams")
    void shouldVerifyAreEqualIgnoringWhitespace(String string1, String string2) {
        assertThat(IdentityVerificationUtils.areEqualIgnoringWhitespace(string1, string2)).isTrue();
    }

    @Test
    void shouldVerifyAreNotEqualIgnoringWhitespace() {
        assertThat(IdentityVerificationUtils.areEqualIgnoringWhitespace("b a", "a b")).isFalse();
    }

    @ParameterizedTest
    @CsvSource({"abcdefgh, abcdefghyik, true",
            "abcdefgh, abcDEFGhyik, true",
            "abcd, abcd, true",
            "abcd, abcdefghij, false",
            "abcd, zxyhe, false",
            "ab cdef, abcdef, false",
            "abcd, abcdefghijk, false"})
    void shouldMatchFirstSixCharacters(String string1, String string2, boolean match) {
        assertThat(IdentityVerificationUtils.firstSixCharacterMatch(string1, string2)).isEqualTo(match);
    }

    @Test
    void shouldMatchAdult() {
        assertThat(IdentityVerificationUtils.matchingAdult(HOMER, aValidPersonDTO())).isTrue();
    }

    @Test
    void shouldFailToMatchAdultWithDifferentSurname() {
        PersonDTO person = aPersonDTOWithSurname("Doe");
        assertThat(IdentityVerificationUtils.matchingAdult(HOMER, person)).isFalse();
    }

    @Test
    void shouldFailToMatchAdultWithDifferentNino() {
        PersonDTO person = aPersonDTOWithNino(MARGE_NINO);
        assertThat(IdentityVerificationUtils.matchingAdult(HOMER, person)).isFalse();
    }

    @Test
    void shouldFailToMatchAdultWithDifferentDateOfBirth() {
        PersonDTO person = aPersonDTOWithDateOfBirth(MARGE_DATE_OF_BIRTH);
        assertThat(IdentityVerificationUtils.matchingAdult(HOMER, person)).isFalse();
    }

    @Test
    void shouldMatchAdultWithNoDateOfBirthInUCAdult() {
        UCAdult homer = aUCAdult(SIMPSON_SURNAME, HOMER_NINO, null, HOMER_MOBILE, HOMER_EMAIL);
        assertThat(IdentityVerificationUtils.matchingAdult(homer, aValidPersonDTO())).isTrue();
    }

    @ParameterizedTest
    @MethodSource("areEqualParams")
    void shouldBeEqual(String string1, String string2) {
        assertThat(IdentityVerificationUtils.areEqual(string1, string2)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("areNotEqualParams")
    void shouldNotBeEqual(String string1, String string2) {
        assertThat(IdentityVerificationUtils.areEqual(string1, string2)).isFalse();
    }

    private static Stream<Arguments> areEqualParams() {
        return Stream.of(
                Arguments.of("ab", "ab"),
                Arguments.of(" ab", "ab"),
                Arguments.of("ab ", "ab"),
                Arguments.of("Ab", "ab"),
                Arguments.of("AB", "ab"),
                Arguments.of("   ", ""),
                Arguments.of(null, null)
        );
    }

    private static Stream<Arguments> areNotEqualParams() {
        return Stream.of(
                Arguments.of("a c", "ab"),
                Arguments.of(" a b", "ab"),
                Arguments.of("a b ", "ab"),
                Arguments.of("a   b", "ab"),
                Arguments.of("a   B", "ab"),
                Arguments.of("A B", "ab"),
                Arguments.of(null, "ab"),
                Arguments.of("A B", null)
        );
    }

    private static Stream<Arguments> determineVerificationOutcomeParams() {
        return Stream.of(
                Arguments.of("seeded", "request", true, VerificationOutcome.MATCHED),
                Arguments.of("seeded", "request", false, VerificationOutcome.NOT_MATCHED),
                Arguments.of(null, "request", true, VerificationOutcome.NOT_HELD),
                Arguments.of(null, "request", false, VerificationOutcome.NOT_HELD),
                Arguments.of(null, null, true, VerificationOutcome.NOT_SUPPLIED),
                Arguments.of(null, null, false, VerificationOutcome.NOT_SUPPLIED),
                Arguments.of("seeded", null, true, VerificationOutcome.NOT_SUPPLIED),
                Arguments.of("seeded", null, false, VerificationOutcome.NOT_SUPPLIED)
        );
    }

    private static Stream<Arguments> verifyEqualsIgnoringWhitespaceParams() {
        return Stream.of(
                Arguments.of("a b", "ab"),
                Arguments.of(" a b", "ab"),
                Arguments.of("a b ", "ab"),
                Arguments.of("a   b", "ab"),
                Arguments.of("a   B", "ab"),
                Arguments.of("a   B", "Ab"),
                Arguments.of("   ", "")
        );
    }
}

package uk.gov.dhsc.htbhf.dwp.converter;

import org.junit.jupiter.api.Test;
import uk.gov.dhsc.htbhf.dwp.model.DWPPersonDTO;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dhsc.htbhf.dwp.factory.PersonDTOTestDataFactory.aValidPerson;

public class PersonDTOToDWPPersonConverterTest {

    PersonDTOToDWPPersonConverter converter = new PersonDTOToDWPPersonConverter();

    @Test
    void shouldConvertPerson() {
        PersonDTO person = aValidPerson();

        DWPPersonDTO result = converter.convert(person);

        assertThat(result.getAddress()).isEqualTo(person.getAddress());
        assertThat(result.getDateOfBirth()).isEqualTo(person.getDateOfBirth());
        assertThat(result.getForename()).isEqualTo(person.getFirstName());
        assertThat(result.getSurname()).isEqualTo(person.getLastName());
        assertThat(result.getNino()).isEqualTo(person.getNino());
    }
}

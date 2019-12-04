package uk.gov.dhsc.htbhf.dwp.testhelper.v1;

import uk.gov.dhsc.htbhf.dwp.model.v1.AddressDTO;
import uk.gov.dhsc.htbhf.dwp.model.v1.PersonDTO;

import java.time.LocalDate;

import static uk.gov.dhsc.htbhf.TestConstants.HOMER_DATE_OF_BIRTH;
import static uk.gov.dhsc.htbhf.TestConstants.HOMER_FORENAME;
import static uk.gov.dhsc.htbhf.TestConstants.HOMER_NINO_V1;
import static uk.gov.dhsc.htbhf.TestConstants.SIMPSON_SURNAME;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v1.AddressDTOTestDataFactory.aValidAddress;

public class PersonDTOTestDataFactory {

    public static PersonDTO aValidPerson() {
        return buildDefaultPerson().build();
    }

    public static PersonDTO aPersonWithFirstName(String firstName) {
        return buildDefaultPerson().firstName(firstName).build();
    }

    public static PersonDTO aPersonWithLastName(String lastName) {
        return buildDefaultPerson().lastName(lastName).build();
    }

    public static PersonDTO aPersonWithNino(String nino) {
        return buildDefaultPerson().nino(nino).build();
    }

    public static PersonDTO aPersonWithDateOfBirth(LocalDate date) {
        return buildDefaultPerson().dateOfBirth(date).build();
    }

    public static PersonDTO aPersonWithAddress(AddressDTO address) {
        return buildDefaultPerson().address(address).build();
    }

    public static PersonDTO.PersonDTOBuilder buildDefaultPerson() {
        return PersonDTO.builder()
                .firstName(HOMER_FORENAME)
                .lastName(SIMPSON_SURNAME)
                .nino(HOMER_NINO_V1)
                .dateOfBirth(HOMER_DATE_OF_BIRTH)
                .address(aValidAddress());
    }

}

package uk.gov.dhsc.htbhf.dwp.factory;

import uk.gov.dhsc.htbhf.dwp.model.AddressDTO;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;

import java.time.LocalDate;

import static uk.gov.dhsc.htbhf.dwp.factory.AddressDTOTestDataFactory.aValidAddress;

public class PersonDTOTestDataFactory {

    private static final String FIRST_NAME = "Lisa";
    private static final String LAST_NAME = "Simpson";
    private static final String NINO = "EB123456C";
    private static final String DATE_OF_BIRTH = "1985-12-31";

    public static PersonDTO aValidPerson() {
        return aValidPersonBuilder().build();
    }

    public static PersonDTO aPersonWithFirstName(String firstName) {
        return aValidPersonBuilder().firstName(firstName).build();
    }

    public static PersonDTO aPersonWithLastName(String lastName) {
        return aValidPersonBuilder().lastName(lastName).build();
    }

    public static PersonDTO aPersonWithNino(String nino) {
        return aValidPersonBuilder().nino(nino).build();
    }

    public static PersonDTO aPersonWithDateOfBirth(LocalDate date) {
        return aValidPersonBuilder().dateOfBirth(date).build();
    }

    public static PersonDTO aPersonWithAddress(AddressDTO address) {
        return aValidPersonBuilder().address(address).build();
    }

    public static PersonDTO.PersonDTOBuilder aValidPersonBuilder() {
        return PersonDTO.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .nino(NINO)
                .dateOfBirth(LocalDate.parse(DATE_OF_BIRTH))
                .address(aValidAddress());
    }

}

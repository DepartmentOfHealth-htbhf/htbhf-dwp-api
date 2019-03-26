package uk.gov.dhsc.htbhf.dwp.factory;

import uk.gov.dhsc.htbhf.dwp.model.AddressDTO;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;

import java.time.LocalDate;

import static uk.gov.dhsc.htbhf.dwp.factory.AddressDTOTestDataFactory.aValidAddress;

public class PersonDTOTestDataFactory {

    private static final String FORENAME = "Lisa";
    private static final String SURNAME = "Simpson";
    private static final String NINO = "QQ123456C";
    private static final String DATE_OF_BIRTH = "1985-12-30";

    public static PersonDTO aValidPerson() {
        return aValidPersonBuilder().build();
    }

    public static PersonDTO aPersonWithForename(String forename) {
        return aValidPersonBuilder().forename(forename).build();
    }

    public static PersonDTO aPersonWithSurname(String surname) {
        return aValidPersonBuilder().surname(surname).build();
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
                .forename(FORENAME)
                .surname(SURNAME)
                .nino(NINO)
                .dateOfBirth(LocalDate.parse(DATE_OF_BIRTH))
                .address(aValidAddress());
    }

}

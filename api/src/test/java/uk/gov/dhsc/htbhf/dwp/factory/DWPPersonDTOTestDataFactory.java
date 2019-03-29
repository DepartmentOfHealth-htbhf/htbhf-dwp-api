package uk.gov.dhsc.htbhf.dwp.factory;

import uk.gov.dhsc.htbhf.dwp.model.AddressDTO;
import uk.gov.dhsc.htbhf.dwp.model.DWPPersonDTO;

import java.time.LocalDate;

import static uk.gov.dhsc.htbhf.dwp.factory.AddressDTOTestDataFactory.aValidAddress;

public class DWPPersonDTOTestDataFactory {

    private static final LocalDate DOB = LocalDate.parse("1985-12-31");
    private static final String NINO = "EB123456C";
    private static final String FORENAME = "Lisa";
    private static final String SURNAME = "Simpson";

    public static DWPPersonDTO aValidDWPPerson() {
        return buildDefaultDWPPerson().build();
    }

    public static DWPPersonDTO aDWPPersonWithForename(String forename) {
        return buildDefaultDWPPerson().forename(forename).build();
    }

    public static DWPPersonDTO aDWPPersonWithSurname(String surname) {
        return buildDefaultDWPPerson().surname(surname).build();
    }

    public static DWPPersonDTO aDWPPersonWithDateOfBirth(LocalDate date) {
        return buildDefaultDWPPerson().dateOfBirth(date).build();
    }

    public static DWPPersonDTO aDWPPersonWithNino(String nino) {
        return buildDefaultDWPPerson().nino(nino).build();
    }

    public static DWPPersonDTO aDWPPersonWithAddress(AddressDTO address) {
        return buildDefaultDWPPerson().address(address).build();
    }

    private static DWPPersonDTO.DWPPersonDTOBuilder buildDefaultDWPPerson() {
        return DWPPersonDTO.builder()
                .dateOfBirth(DOB)
                .nino(NINO)
                .address(aValidAddress())
                .forename(FORENAME)
                .surname(SURNAME);
    }
}

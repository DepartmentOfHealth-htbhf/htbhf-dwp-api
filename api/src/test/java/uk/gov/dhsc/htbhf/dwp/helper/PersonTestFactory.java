package uk.gov.dhsc.htbhf.dwp.helper;

import uk.gov.dhsc.htbhf.dwp.model.AddressDTO;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;

import java.time.LocalDate;

public class PersonTestFactory {

    private static final LocalDate DOB = LocalDate.parse("1985-12-31");
    private static final String ADDRESS_LINE_1 = "742 Evergreen Terrace";
    private static final String ADDRESS_LINE_2 = "123 Fake street";
    private static final String TOWN_OR_CITY = "Springfield";
    private static final String POSTCODE = "AA11AA";
    private static final String NINO = "EB123456C";
    private static final String FORENAME = "Lisa";
    private static final String SURNAME = "Simpson";

    public static PersonDTO aPerson() {
        String nino = "IA000000C";
        return buildDefaultPerson().nino(nino).build();
    }

    /**
     * Creates a {@link PersonDTO} request object with no nino.
     */
    public static PersonDTO aPersonWithNoNino() {
        return buildDefaultPerson().nino(null).build();
    }

    /**
     * Creates a {@link PersonDTO} request object with an invalid nino.
     */
    public static PersonDTO aPersonWithAnInvalidNino() {
        return buildDefaultPerson().nino("ab123").build();
    }

    /**
     * Creates a {@link PersonDTO} request object with no date of birth.
     */
    public static PersonDTO aPersonWithNoDateOfBirth() {
        return buildDefaultPerson().dateOfBirth(null).build();
    }

    /**
     * Creates a {@link PersonDTO} request object with no address.
     */
    public static PersonDTO aPersonWithNoAddress() {
        return buildDefaultPerson().address(null).build();
    }

    private static PersonDTO.PersonDTOBuilder buildDefaultPerson() {
        return PersonDTO.builder()
                .dateOfBirth(DOB)
                .nino(NINO)
                .address(aValidAddress())
                .forename(FORENAME)
                .surname(SURNAME);
    }

    private static AddressDTO aValidAddress() {
        return AddressDTO.builder()
                .addressLine1(ADDRESS_LINE_1)
                .addressLine2(ADDRESS_LINE_2)
                .townOrCity(TOWN_OR_CITY)
                .postcode(POSTCODE)
                .build();
    }
}

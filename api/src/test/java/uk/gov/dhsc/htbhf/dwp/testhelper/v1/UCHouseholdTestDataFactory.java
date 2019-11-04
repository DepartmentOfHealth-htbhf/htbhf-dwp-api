package uk.gov.dhsc.htbhf.dwp.testhelper.v1;

import uk.gov.dhsc.htbhf.dwp.entity.v1.uc.UCAdult;
import uk.gov.dhsc.htbhf.dwp.entity.v1.uc.UCChild;
import uk.gov.dhsc.htbhf.dwp.entity.v1.uc.UCHousehold;

import java.time.LocalDate;

import static uk.gov.dhsc.htbhf.dwp.testhelper.v1.TestConstants.*;

public class UCHouseholdTestDataFactory {

    public static UCHousehold aUCHousehold() {
        return aUCHouseholdWithNoAdultsOrChildren()
                .build()
                .addAdult(aUCAdult(SIMPSON_SURNAME, HOMER_NINO))
                .addAdult(aUCAdult(SIMPSON_SURNAME, MARGE_NINO))
                .addChild(aUCChild(BART_DOB))
                .addChild(aUCChild(LISA_DOB))
                .addChild(aUCChild(MAGGIE_DOB));
    }

    public static UCHousehold.UCHouseholdBuilder aUCHouseholdWithNoAdultsOrChildren() {
        return UCHousehold.builder()
                .householdIdentifier(SIMPSON_UC_HOUSEHOLD_IDENTIFIER)
                .build()
                .toBuilder();
    }

    public static UCChild aUCChild(LocalDate dateOfBirth) {
        return UCChild.builder()
                .dateOfBirth(dateOfBirth)
                .build();
    }

    public static UCAdult aUCAdult(String surname, String nino) {
        return UCAdult.builder()
                .surname(surname)
                .nino(nino)
                .addressLine1(SIMPSONS_ADDRESS_LINE_1)
                .postcode(SIMPSONS_POSTCODE)
                .build();
    }

    public static UCAdult aUCAdultWithNino(String nino) {
        return aUCAdult(SIMPSON_SURNAME, nino);
    }

}

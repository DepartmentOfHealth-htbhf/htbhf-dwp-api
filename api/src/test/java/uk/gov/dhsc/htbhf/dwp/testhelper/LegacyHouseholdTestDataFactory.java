package uk.gov.dhsc.htbhf.dwp.testhelper;

import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyAdult;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyChild;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;

import java.time.LocalDate;

import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.*;

public class LegacyHouseholdTestDataFactory {

    public static LegacyHousehold aLegacyHousehold() {
        return aLegacyHouseholdWithNoAdultsOrChildren()
                .build()
                .addAdult(aLegacyAdult(HOMER_FORENAME, SIMPSON_SURNAME, HOMER_NINO))
                .addAdult(aLegacyAdult(MARGE_FORENAME, SIMPSON_SURNAME, MARGE_NINO))
                .addChild(aLegacyChild(BART_FORENAME, SIMPSON_SURNAME, 48))
                .addChild(aLegacyChild(LISA_FORENAME, SIMPSON_SURNAME, 24))
                .addChild(aLegacyChild(MAGGIE_FORENAME, SIMPSON_SURNAME, 6));
    }

    public static LegacyHousehold.LegacyHouseholdBuilder aLegacyHouseholdWithNoAdultsOrChildren() {
        return LegacyHousehold.builder()
                .fileImportNumber(1)
                .householdIdentifier(SIMPSON_LEGACY_HOUSEHOLD_IDENTIFIER)
                .addressLine1(SIMPSONS_ADDRESS_LINE_1)
                .townOrCity(SIMPSONS_TOWN)
                .postcode(SIMPSONS_POSTCODE)
                .build()
                .toBuilder();
    }

    public static LegacyChild aLegacyChild(String forename, String surname, int ageInMonths) {
        return LegacyChild.builder()
                .forename(forename)
                .surname(surname)
                .dateOfBirth(LocalDate.now().minusMonths(ageInMonths))
                .build();
    }

    public static LegacyAdult aLegacyAdult(String forename, String surname, String nino) {
        return LegacyAdult.builder()
                .forename(forename)
                .surname(surname)
                .nino(nino)
                .build();
    }

    public static LegacyAdult aLegacyAdultWithNino(String nino) {
        return LegacyAdult.builder()
                .forename(HOMER_FORENAME)
                .surname(SIMPSON_SURNAME)
                .nino(nino)
                .build();
    }

}

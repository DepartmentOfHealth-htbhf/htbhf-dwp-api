package uk.gov.dhsc.htbhf.dwp.entity;

import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyAdult;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyChild;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;

import java.time.LocalDate;

public class LegacyHouseholdFactory {

    public static final String HOMER_NINO = "QQ123456A";
    public static final String SIMPSON_HOUSEHOLD_IDENTIFIER = "legacyHouseholdIdentifier";

    public static LegacyHousehold aLegacyHousehold() {
        return aLegacyHouseholdWithNoAdultsOrChildren()
                .build()
                .addAdult(aLegacyAdult("Homer", "Simpson", HOMER_NINO))
                .addAdult(aLegacyAdult("Marge", "Simpson", "QQ123456B"))
                .addChild(aLegacyChild("Bart", "Simpson", 48))
                .addChild(aLegacyChild("Lisa", "Simpson", 24))
                .addChild(aLegacyChild("Maggie", "Simpson", 6));
    }

    public static LegacyHousehold.LegacyHouseholdBuilder aLegacyHouseholdWithNoAdultsOrChildren() {
        return LegacyHousehold.builder()
                .fileImportNumber(1)
                .householdIdentifier(SIMPSON_HOUSEHOLD_IDENTIFIER)
                .addressLine1("742 Evergreen Terrace")
                .townOrCity("Springfield")
                .postcode("AA11AA")
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
                .forename("Bart")
                .surname("Simpson")
                .nino(nino)
                .build();
    }

}

package uk.gov.dhsc.htbhf.dwp.entity;

import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyAdult;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyChild;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;

import java.time.LocalDate;

public class LegacyHouseholdFactory {

    public static LegacyHousehold aHousehold() {
        return LegacyHousehold.builder()
                .fileImportNumber(1)
                .householdIdentifier("aHouseholdIdentifier")
                .addressLine1("742 Evergreen Terrace")
                .townOrCity("Springfield")
                .postcode("AA11AA")
                .build()
                .addAdult(anAdult("Homer", "Simpson", "QQ123456A"))
                .addAdult(anAdult("Marge", "Simpson", "QQ123456B"))
                .addChild(aChild("Bart", "Simpson", 48))
                .addChild(aChild("Lisa", "Simpson", 24))
                .addChild(aChild("Maggie", "Simpson", 6));
    }

    public static LegacyChild aChild(String forename, String surname, int ageInMonths) {
        return LegacyChild.builder()
                .forename(forename)
                .surname(surname)
                .dateOfBirth(LocalDate.now().minusMonths(ageInMonths))
                .build();
    }

    public static LegacyAdult anAdult(String forename, String surname, String nino) {
        return LegacyAdult.builder()
                .forename(forename)
                .surname(surname)
                .nino(nino)
                .build();
    }

}
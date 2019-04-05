package uk.gov.dhsc.htbhf.dwp.testhelper;

import uk.gov.dhsc.htbhf.dwp.entity.uc.UCAdult;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCChild;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;

import java.time.LocalDate;

import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.*;

public class UCHouseholdTestDataFactory {

    public static UCHousehold aUCHousehold() {
        return aUCHouseholdWithNoAdultsOrChildren()
                .build()
                .addAdult(aUCAdult("Homer", "Simpson", HOMER_NINO))
                .addAdult(aUCAdult("Marge", "Simpson", MARGE_NINO))
                .addChild(aUCChild("Bart", "Simpson", 48))
                .addChild(aUCChild("Lisa", "Simpson", 24))
                .addChild(aUCChild("Maggie", "Simpson", 6));
    }

    public static UCHousehold.UCHouseholdBuilder aUCHouseholdWithNoAdultsOrChildren() {
        return UCHousehold.builder()
                .awardDate(LocalDate.now())
                .fileImportNumber(1)
                .householdIdentifier(SIMPSON_LEGACY_HOUSEHOLD_IDENTIFIER)
                .lastAssessmentPeriodStart(LocalDate.now().minusMonths(1))
                .lastAssessmentPeriodEnd(LocalDate.now())
                .earningsThresholdExceeded(false)
                .householdMemberPregnant(true)
                .childrenUnderFour(2)
                .build()
                .toBuilder();
    }

    public static UCChild aUCChild(String forename, String surname, int ageInMonths) {
        return UCChild.builder()
                .forename(forename)
                .surname(surname)
                .dateOfBirth(LocalDate.now().minusMonths(ageInMonths))
                .build();
    }

    public static UCAdult aUCAdult(String forename, String surname, String nino) {
        return UCAdult.builder()
                .forename(forename)
                .surname(surname)
                .nino(nino)
                .addressLine1(SIMPSONS_ADDRESS_LINE_1)
                .townOrCity(SIMPSONS_TOWN)
                .postcode(SIMPSONS_POSTCODE)
                .build();
    }

    public static UCAdult aUCAdultWithNino(String nino) {
        return aUCAdult(HOMER_FORENAME, SIMPSON_SURNAME, nino);
    }

}

package uk.gov.dhsc.htbhf.dwp.entity;

import uk.gov.dhsc.htbhf.dwp.entity.uc.UCAdult;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCChild;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;

import java.time.LocalDate;

public class UCHouseholdFactory {

    public static UCHousehold aHousehold() {
        return aHouseholdWithNoAdultsOrChildren()
                .build()
                .addAdult(anAdult("Homer", "Simpson", "QQ123456C"))
                .addAdult(anAdult("Marge", "Simpson", "QQ123456D"))
                .addChild(aChild("Bart", "Simpson", 48))
                .addChild(aChild("Lisa", "Simpson", 24))
                .addChild(aChild("Maggie", "Simpson", 6));
    }

    public static UCHousehold.UCHouseholdBuilder aHouseholdWithNoAdultsOrChildren() {
        return UCHousehold.builder()
                .awardDate(LocalDate.now())
                .fileImportNumber(1)
                .householdIdentifier("aHouseholdIdentifier")
                .lastAssessmentPeriodStart(LocalDate.now().minusMonths(1))
                .lastAssessmentPeriodEnd(LocalDate.now())
                .earningsThresholdExceeded(false)
                .householdMemberPregnant(true)
                .childrenUnderFour(2)
                .build()
                .toBuilder();
    }

    public static UCChild aChild(String forename, String surname, int ageInMonths) {
        return UCChild.builder()
                .forename(forename)
                .surname(surname)
                .dateOfBirth(LocalDate.now().minusMonths(ageInMonths))
                .build();
    }

    public static UCAdult anAdult(String forename, String surname, String nino) {
        return UCAdult.builder()
                .forename(forename)
                .surname(surname)
                .nino(nino)
                .addressLine1("742 Evergreen Terrace")
                .townOrCity("Springfield")
                .postcode("AA11AA")
                .build();
    }

    public static UCAdult anAdultWithNino(String nino) {
        return UCAdult.builder()
                .forename("Homer")
                .surname("Simpson")
                .nino(nino)
                .addressLine1("742 Evergreen Terrace")
                .townOrCity("Springfield")
                .postcode("AA11AA")
                .build();
    }

}

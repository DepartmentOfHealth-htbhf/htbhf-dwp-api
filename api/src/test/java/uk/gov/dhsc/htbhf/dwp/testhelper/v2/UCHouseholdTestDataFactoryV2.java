package uk.gov.dhsc.htbhf.dwp.testhelper.v2;

import uk.gov.dhsc.htbhf.dwp.entity.uc.UCAdult;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCChild;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;

import java.time.LocalDate;
import java.util.Arrays;

import static uk.gov.dhsc.htbhf.TestConstants.*;

public class UCHouseholdTestDataFactoryV2 {

    public static final UCAdult HOMER = aUCAdult(SIMPSON_SURNAME, HOMER_NINO_V2, HOMER_DATE_OF_BIRTH, HOMER_MOBILE, HOMER_EMAIL);
    private static final UCAdult MARGE = aUCAdult(SIMPSON_SURNAME, MARGE_NINO_V2, MARGE_DATE_OF_BIRTH, MARGE_MOBILE, MARGE_EMAIL);
    private static final UCChild BART = aUCChild(BART_DATE_OF_BIRTH);
    private static final UCChild LISA = aUCChild(LISA_DATE_OF_BIRTH);
    private static final UCChild MAGGIE = aUCChild(MAGGIE_DATE_OF_BIRTH);

    public static UCHousehold aUCHousehold() {
        return aUCHouseholdWithNoAdultsOrChildren()
                .build()
                .addAdult(HOMER)
                .addAdult(MARGE)
                .addChild(BART)
                .addChild(LISA)
                .addChild(MAGGIE);
    }

    public static UCHousehold aUCHouseholdWithEarningsThresholdExceeded() {
        return aUCHouseholdWithNoAdultsOrChildrenAndEarningsThresholdExceeded()
                .build()
                .addAdult(HOMER)
                .addAdult(MARGE)
                .addChild(BART)
                .addChild(LISA)
                .addChild(MAGGIE);
    }

    public static UCHousehold aUCHouseholdWithChildren(UCChild... child) {
        UCHousehold ucHouseholdWithAdults = aUCHouseholdWithNoAdultsOrChildren()
                .build()
                .addAdult(HOMER)
                .addAdult(MARGE);
        Arrays.stream(child).forEach(ucHouseholdWithAdults::addChild);
        return ucHouseholdWithAdults;
    }

    public static UCHousehold aUCHouseholdWithAdultMobileAndEmail(String mobile, String email) {
        return aUCHouseholdWithNoAdultsOrChildren()
                .build()
                .addAdult(aUCAdult(SIMPSON_SURNAME, HOMER_NINO_V2, HOMER_DATE_OF_BIRTH, mobile, email))
                .addAdult(MARGE)
                .addChild(BART)
                .addChild(LISA)
                .addChild(MAGGIE);
    }

    public static UCHousehold aUCHouseholdWithAdultDateOfBirth(LocalDate dateOfBirth) {
        return aUCHouseholdWithNoAdultsOrChildren()
                .build()
                .addAdult(aUCAdult(SIMPSON_SURNAME, HOMER_NINO_V2, dateOfBirth, HOMER_MOBILE, HOMER_EMAIL))
                .addAdult(MARGE)
                .addChild(BART)
                .addChild(LISA)
                .addChild(MAGGIE);
    }

    private static UCHousehold.UCHouseholdBuilder aUCHouseholdWithNoAdultsOrChildren() {
        return UCHousehold.builder()
                .householdIdentifier(SIMPSON_UC_HOUSEHOLD_IDENTIFIER)
                .build()
                .toBuilder();
    }

    private static UCHousehold.UCHouseholdBuilder aUCHouseholdWithNoAdultsOrChildrenAndEarningsThresholdExceeded() {
        return UCHousehold.builder()
                .householdIdentifier(SIMPSON_UC_HOUSEHOLD_IDENTIFIER)
                .earningsThresholdExceeded(true)
                .build()
                .toBuilder();
    }

    public static UCChild aUCChild(LocalDate dateOfBirth) {
        return UCChild.builder()
                .dateOfBirth(dateOfBirth)
                .build();
    }

    public static UCAdult aUCAdult(String surname, String nino, LocalDate dateOfBirth, String mobile, String email) {
        return UCAdult.builder()
                .surname(surname)
                .nino(nino)
                .dateOfBirth(dateOfBirth)
                .addressLine1(SIMPSONS_ADDRESS_LINE_1)
                .postcode(SIMPSONS_POSTCODE)
                .mobilePhoneNumber(mobile)
                .emailAddress(email)
                .build();
    }

}

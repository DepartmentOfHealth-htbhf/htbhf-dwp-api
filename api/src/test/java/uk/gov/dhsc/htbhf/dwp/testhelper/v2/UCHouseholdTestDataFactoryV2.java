package uk.gov.dhsc.htbhf.dwp.testhelper.v2;

import uk.gov.dhsc.htbhf.dwp.entity.v1.uc.UCAdult;
import uk.gov.dhsc.htbhf.dwp.entity.v1.uc.UCChild;
import uk.gov.dhsc.htbhf.dwp.entity.v1.uc.UCHousehold;

import java.time.LocalDate;
import java.util.Arrays;

import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.*;

public class UCHouseholdTestDataFactoryV2 {

    private static final UCAdult HOMER = aUCAdult(HOMER_NINO_V2, HOMER_DOB, HOMER_MOBILE, HOMER_EMAIL);
    private static final UCAdult MARGE = aUCAdult(MARGE_NINO_V2, MARGE_DOB, MARGE_MOBILE, MARGE_EMAIL);
    private static final UCChild BART = aUCChild(BART_DOB);
    private static final UCChild LISA = aUCChild(LISA_DOB);
    private static final UCChild MAGGIE = aUCChild(MAGGIE_DOB);

    public static UCHousehold aUCHousehold() {
        return aUCHouseholdWithNoAdultsOrChildren()
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
                .addAdult(aUCAdult(HOMER_NINO_V2, HOMER_DOB, mobile, email))
                .addAdult(MARGE)
                .addChild(BART)
                .addChild(LISA)
                .addChild(MAGGIE);
    }

    public static UCHousehold aUCHouseholdWithAdultDateOfBirth(LocalDate dateOfBirth) {
        return aUCHouseholdWithNoAdultsOrChildren()
                .build()
                .addAdult(aUCAdult(HOMER_NINO_V2, dateOfBirth, HOMER_MOBILE, HOMER_EMAIL))
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

    public static UCChild aUCChild(LocalDate dateOfBirth) {
        return UCChild.builder()
                .dateOfBirth(dateOfBirth)
                .build();
    }

    private static UCAdult aUCAdult(String nino, LocalDate dateOfBirth, String mobile, String email) {
        return UCAdult.builder()
                .surname(SIMPSON_SURNAME)
                .nino(nino)
                .dateOfBirth(dateOfBirth)
                .addressLine1(SIMPSONS_ADDRESS_LINE_1)
                .postcode(SIMPSONS_POSTCODE)
                .mobilePhoneNumber(mobile)
                .emailAddress(email)
                .build();
    }

}

package uk.gov.dhsc.htbhf.dwp.testhelper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class TestConstants {

    public static final String HOMER_NINO = "EB123456C";
    public static final String MARGE_NINO = "EB123456D";
    public static final String HOMER_NINO_V2 = "MC123456D";
    public static final String MARGE_NINO_V2 = "MC123456A";
    public static final String LISA_FORENAME = "Lisa";
    public static final String HOMER_FORENAME = "Homer";
    public static final String MARGE_FORENAME = "Marge";
    public static final String MAGGIE_FORENAME = "Maggie";
    public static final String BART_FORENAME = "BART";
    public static final String SIMPSON_SURNAME = "Simpson";
    public static final String HOMER_DOB_STRING = "1985-12-31";
    public static final String MARGE_DOB_STRING = "1975-12-31";
    public static final LocalDate HOMER_DOB = LocalDate.parse(HOMER_DOB_STRING);
    public static final LocalDate MARGE_DOB = LocalDate.parse(MARGE_DOB_STRING);
    public static final LocalDate MAGGIE_DOB = LocalDate.now().minusMonths(6);
    public static final String MAGGIE_DOB_STRING = MAGGIE_DOB.format(DateTimeFormatter.ISO_LOCAL_DATE);
    public static final LocalDate LISA_DOB = LocalDate.now().minusMonths(24);
    public static final LocalDate BART_DOB = LocalDate.now().minusMonths(48);
    public static final LocalDate FIVE_YEAR_OLD = LocalDate.now().minusYears(5);
    public static final LocalDate TWENTY_YEAR_OLD = LocalDate.now().minusYears(20);
    public static final List<LocalDate> MAGGIE_AND_LISA_DOBS = List.of(MAGGIE_DOB, LISA_DOB);
    public static final String HOMER_EMAIL = "homer@simpson.com";
    public static final String MARGE_EMAIL = "marge@simpson.com";
    public static final String HOMER_MOBILE = "+447700900000";
    public static final String MARGE_MOBILE = "+447700900001";

    public static final String SIMPSONS_ADDRESS_LINE_1 = "742 Evergreen Terrace";
    public static final String SIMPSONS_ADDRESS_LINE_2 = "Mystery Spot";
    public static final String SIMPSONS_TOWN = "Springfield";
    public static final String SIMPSONS_POSTCODE = "AA1 1AA";

    public static final LocalDate ELIGIBLE_END_DATE = LocalDate.parse("2019-03-01");
    public static final LocalDate ELIGIBLE_START_DATE = LocalDate.parse("2019-02-14");
    public static final BigDecimal UC_MONTHLY_INCOME_THRESHOLD = BigDecimal.valueOf(408);
    public static final int UC_MONTHLY_INCOME_THRESHOLD_IN_PENCE = 40800;
    public static final String SIMPSON_UC_HOUSEHOLD_IDENTIFIER = "ucHouseholdIdentifier";
    public static final String SIMPSON_LEGACY_HOUSEHOLD_IDENTIFIER = "legacyHouseholdIdentifier";
    public static final String NO_HOUSEHOLD_IDENTIFIER_PROVIDED = "";
}

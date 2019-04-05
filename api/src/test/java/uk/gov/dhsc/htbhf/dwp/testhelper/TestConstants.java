package uk.gov.dhsc.htbhf.dwp.testhelper;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class TestConstants {

    public static final String HOMER_NINO = "EB123456C";
    public static final String MARGE_NINO = "EB123456D";
    public static final String LISA_FORENAME = "Lisa";
    public static final String HOMER_FORENAME = "Homer";
    public static final String MARGE_FORENAME = "Marge";
    public static final String MAGGIE_FORENAME = "Maggie";
    public static final String BART_FORENAME = "BART";
    public static final String SIMPSON_SURNAME = "Simpson";
    public static final LocalDate HOMER_DOB = LocalDate.parse("1985-12-31");

    public static final String SIMPSONS_ADDRESS_LINE_1 = "742 Evergreen Terrace";
    public static final String SIMPSONS_ADDRESS_LINE_2 = "Suburb";
    public static final String SIMPSONS_TOWN = "Springfield";
    public static final String SIMPSONS_POSTCODE = "AA1 1AA";

    public static final LocalDate ELIGIBLE_END_DATE = LocalDate.parse("2019-03-01");
    public static final LocalDate ELIGIBLE_START_DATE = LocalDate.parse("2019-02-14");
    public static final BigDecimal UC_MONTHLY_INCOME_THRESHOLD = BigDecimal.valueOf(408);
    public static final String SIMPSON_UC_HOUSEHOLD_IDENTIFIER = "ucHouseholdIdentifier";
    public static final String SIMPSON_LEGACY_HOUSEHOLD_IDENTIFIER = "legacyHouseholdIdentifier";
}

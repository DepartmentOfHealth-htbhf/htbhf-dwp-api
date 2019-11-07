package uk.gov.dhsc.htbhf.dwp.service.v2;

public enum HeaderName {
    SURNAME("surname"),
    NINO("nino"),
    DATE_OF_BIRTH("dateOfBirth"),
    ELIGIBILITY_END_DATE("eligibilityEndDate"),
    ADDRESS_LINE_1("addressLine1"),
    POSTCODE("postcode"),
    EMAIL_ADDRESS("emailAddress"),
    MOBILE_PHONE_NUMBER("mobilePhoneNumber"),
    PREGNANT_DEPENDENT_DOB("pregnantDependentDob"),
    UC_MONTHLY_INCOME_THRESHOLD("ucMonthlyIncomeThreshold");

    private String header;

    HeaderName(String header) {
        this.header = header;
    }

    public String getHeader() {
        return this.header;
    }
}

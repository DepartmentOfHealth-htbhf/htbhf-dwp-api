package uk.gov.dhsc.htbhf.dwp.model.v2;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum QualifyingBenefits {
    UNIVERSAL_CREDIT,
    EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
    INCOME_SUPPORT,
    JOBSEEKERS_ALLOWANCE,
    PENSION_CREDIT,
    NOT_SET;

    @JsonValue
    public String getResponseValue() {
        return this.name().toLowerCase(Locale.getDefault());
    }
}

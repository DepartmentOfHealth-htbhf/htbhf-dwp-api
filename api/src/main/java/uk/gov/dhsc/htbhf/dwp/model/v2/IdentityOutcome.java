package uk.gov.dhsc.htbhf.dwp.model.v2;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum IdentityOutcome {
    MATCHED,
    NOT_MATCHED;

    @JsonValue
    public String getResponseValue() {
        return this.name().toLowerCase(Locale.getDefault());
    }
}

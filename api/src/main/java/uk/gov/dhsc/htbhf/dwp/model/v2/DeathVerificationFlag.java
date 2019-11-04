package uk.gov.dhsc.htbhf.dwp.model.v2;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DeathVerificationFlag {

    N_A("n/a"),
    DEATH_NOT_VERIFIED("death_not_verified"),
    LIMITED_SUPPORTING_DOCUMENTATION("limited_supporting_documentation"),
    PARTIAL_SUPPORTING_DOCUMENTATION("partial_supporting_documentation"),
    FULL_SUPPORTING_DOCUMENTATION("full_supporting_documentation");

    private String responseValue;

    @JsonValue
    public String getResponseValue() {
        return responseValue;
    }
}

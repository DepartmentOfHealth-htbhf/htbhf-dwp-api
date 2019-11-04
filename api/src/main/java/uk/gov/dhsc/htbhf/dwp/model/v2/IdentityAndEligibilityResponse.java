package uk.gov.dhsc.htbhf.dwp.model.v2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
public class IdentityAndEligibilityResponse {

    @JsonProperty("identityStatus")
    private IdentityOutcome identityStatus;

    @JsonProperty("eligibilityStatus")
    private final EligibilityOutcome eligibilityStatus;

    @JsonProperty("deathVerificationFlag")
    private final DeathVerificationFlag deathVerificationFlag;

    @JsonProperty("mobilePhoneMatch")
    private final VerificationOutcome mobilePhoneMatch;

    @JsonProperty("emailAddressMatch")
    private final VerificationOutcome emailAddressMatch;

    @JsonProperty("addressLine1Match")
    private final VerificationOutcome addressLine1Match;

    @JsonProperty("postcodeMatch")
    private final VerificationOutcome postcodeMatch;

    @JsonProperty("pregnantChildDOBMatch")
    private final VerificationOutcome pregnantChildDOBMatch;

    @JsonProperty("qualifyingBenefits")
    private final QualifyingBenefits qualifyingBenefits;

    @JsonProperty("householdIdentifier")
    private final String householdIdentifier;

    @JsonProperty("dobOfChildrenUnder4")
    private final List<LocalDate> dobOfChildrenUnder4;
}

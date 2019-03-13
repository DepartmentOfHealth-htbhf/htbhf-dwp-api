package uk.gov.dhsc.htbhf.dwp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
public class EligibilityResponse {

    @JsonProperty("eligibilityStatus")
    private EligibilityStatus eligibilityStatus;

    @JsonProperty("numberOfChildrenUnderOne")
    private final Integer numberOfChildrenUnderOne;

    @JsonProperty("numberOfChildrenUnderFour")
    private final Integer numberOfChildrenUnderFour;

    @JsonProperty("householdIdentifier")
    private final String householdIdentifier;
}

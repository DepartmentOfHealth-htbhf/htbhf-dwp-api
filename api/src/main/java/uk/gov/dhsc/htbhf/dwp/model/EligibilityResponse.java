package uk.gov.dhsc.htbhf.dwp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
@ApiModel(description = "The eligibility response from DWP")
public class EligibilityResponse {

    @JsonProperty("eligibilityStatus")
    @ApiModelProperty(notes = "The eligibility status", example = "ELIGIBLE")
    private EligibilityStatus eligibilityStatus;

    @JsonProperty("earningsThresholdExceeded")
    @ApiModelProperty(notes = "Whether or not the household has exceeded the earnings threshold for help to buy healthy foods.", example = "true")
    private Boolean earningsThresholdExceeded;

    @JsonProperty("numberOfChildrenUnderOne")
    @ApiModelProperty(notes = "The number of children under 1 that the person has", example = "1")
    private final Integer numberOfChildrenUnderOne;

    @JsonProperty("numberOfChildrenUnderFour")
    @ApiModelProperty(notes = "The number of children under 4 that the person has (which will include the number of children under 1)", example = "1")
    private final Integer numberOfChildrenUnderFour;

    @JsonProperty("householdIdentifier")
    @ApiModelProperty(notes = "The identifier for their household", example = "9d6049ee-d6e6-4d9b-ae3d-3b1bdf98682f")
    private final String householdIdentifier;
}

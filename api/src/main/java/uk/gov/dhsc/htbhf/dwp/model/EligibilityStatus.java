package uk.gov.dhsc.htbhf.dwp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;

/**
 * The possible states that a claim can be in according to the DWP.
 */
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
@ApiModel(description = "The eligibility status")
public enum EligibilityStatus {

    ELIGIBLE,
    INELIGIBLE,
    PENDING,
    NOMATCH
}

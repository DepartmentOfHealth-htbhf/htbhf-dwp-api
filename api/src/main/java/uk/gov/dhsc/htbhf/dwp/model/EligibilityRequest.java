package uk.gov.dhsc.htbhf.dwp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
@ApiModel(description = "The eligibility request for Universal Credit")
public class EligibilityRequest {

    @NotNull
    @Valid
    @JsonProperty("person")
    @ApiModelProperty(notes = "Details of the person")
    private PersonDTO person;

    @NotNull
    @JsonProperty("ucMonthlyIncomeThreshold")
    @ApiModelProperty(notes = "The monthly income threshold for Universal Credit", example = "408")
    private final BigDecimal ucMonthlyIncomeThreshold;

    @NotNull
    @JsonProperty("eligibleStartDate")
    @ApiModelProperty(notes = "The start date for eligibility", example = "2019-01-01")
    private final LocalDate eligibleStartDate;

    @NotNull
    @JsonProperty("eligibleEndDate")
    @ApiModelProperty(notes = "The end date for eligibility", example = "2019-02-01")
    private final LocalDate eligibleEndDate;
}

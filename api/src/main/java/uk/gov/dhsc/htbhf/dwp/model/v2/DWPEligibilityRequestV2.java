package uk.gov.dhsc.htbhf.dwp.model.v2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
public class DWPEligibilityRequestV2 {

    @NotNull
    @Valid
    @JsonProperty("person")
    private PersonDTOV2 person;

    @JsonProperty("ucMonthlyIncomeThreshold")
    private final Integer ucMonthlyIncomeThresholdInPence;

    @NotNull
    @JsonProperty("eligibilityEndDate")
    private final LocalDate eligibilityEndDate;
}

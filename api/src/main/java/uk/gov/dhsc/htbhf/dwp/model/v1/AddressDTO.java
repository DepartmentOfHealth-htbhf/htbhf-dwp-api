package uk.gov.dhsc.htbhf.dwp.model.v1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
@ApiModel(description = "The address object")
public class AddressDTO {
    @NotNull
    @JsonProperty("addressLine1")
    @ApiModelProperty(notes = "First line of the address", example = "Flat B")
    private String addressLine1;

    @JsonProperty("addressLine2")
    @ApiModelProperty(notes = "Second line of the address", example = "221 Baker Street")
    private String addressLine2;

    @NotNull
    @JsonProperty("townOrCity")
    @ApiModelProperty(notes = "Town or city of the address", example = "London")
    private String townOrCity;

    @NotNull
    @JsonProperty("postcode")
    @ApiModelProperty(notes = "The postcode of the address.", example = "AA1 1AA")
    private String postcode;
}

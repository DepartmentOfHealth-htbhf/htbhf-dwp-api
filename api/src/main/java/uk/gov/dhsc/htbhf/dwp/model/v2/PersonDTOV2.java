package uk.gov.dhsc.htbhf.dwp.model.v2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
public class PersonDTOV2 {

    @NotNull
    @JsonProperty("surname")
    private final String surname;

    @NotNull
    @Pattern(regexp = "^(?!BG|GB|NK|KN|TN|NT|ZZ)[A-CEGHJ-PR-TW-Z][A-CEGHJ-NPR-TW-Z](\\d{6})[A-D]$")
    @JsonProperty("nino")
    private final String nino;

    @NotNull
    @Past
    @JsonProperty("dateOfBirth")
    private final LocalDate dateOfBirth;

    @NotNull
    @JsonProperty("addressLine1")
    private final String addressLine1;

    @NotNull
    @JsonProperty("postcode")
    @Pattern(regexp = "^(GIR ?0AA|[A-PR-UWYZ]([0-9]{1,2}|([A-HK-Y][0-9]([0-9ABEHMNPRV-Y])?)|[0-9][A-HJKPS-UW]) ?[0-9][ABD-HJLNP-UW-Z]{2})$")
    private final String postcode;

    @JsonProperty("emailAddress")
    @Pattern(regexp = "(^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$)")
    private final String emailAddress;

    @JsonProperty("mobilePhoneNumber")
    @Pattern(regexp = "^\\+44\\d{9,10}$")
    private final String mobilePhoneNumber;

    @Past
    @JsonProperty("pregnantDependentDob")
    private LocalDate pregnantDependentDob;
}

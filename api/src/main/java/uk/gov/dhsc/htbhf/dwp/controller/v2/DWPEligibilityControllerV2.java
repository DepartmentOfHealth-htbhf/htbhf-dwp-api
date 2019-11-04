package uk.gov.dhsc.htbhf.dwp.controller.v2;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dhsc.htbhf.dwp.model.v2.IdentityAndEligibilityResponse;

import java.util.Map;

@RestController
@RequestMapping("/v2/dwp/eligibility")
@AllArgsConstructor
@Slf4j
@Api(description = "Endpoints for dealing with DWP Identity and Eligibility requests.")
public class DWPEligibilityControllerV2 {

    @GetMapping
    @ApiOperation("Verify a person's identity and eligibility for benefits with DWP")
    @ApiResponses({@ApiResponse(code = 200,
            message = "The person's identity status and eligibility for DWP Benefits",
            response = IdentityAndEligibilityResponse.class)})
    public IdentityAndEligibilityResponse getBenefits(@RequestHeader Map<String, String> headers) {
        log.debug("Received eligibility request");

        return IdentityAndEligibilityResponse.builder().build();
    }
}

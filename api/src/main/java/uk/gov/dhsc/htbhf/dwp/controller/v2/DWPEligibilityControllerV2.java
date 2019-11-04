package uk.gov.dhsc.htbhf.dwp.controller.v2;

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dhsc.htbhf.dwp.model.v2.DWPEligibilityRequestV2;
import uk.gov.dhsc.htbhf.dwp.model.v2.IdentityAndEligibilityResponse;

@RestController
@RequestMapping("/v2/dwp/eligibility")
@AllArgsConstructor
@Slf4j
@Api(description = "Endpoints for dealing with DWP Identity and Eligibility requests.")
public class DWPEligibilityControllerV2 {

    /**
     * Determines the eligibility of the claimant and checks their identity based on the given request
     * details. The request object is built from header values in the request using
     * {@link DwpEligibilityRequestResolver}. The request object is validated, but due to having to
     * use the argument resolver, this is done manually in {@link DwpEligibilityRequestResolver}
     * rather than using Spring's @Valid annotation because this doesn't work on request parameters
     * which are built using an argument resolver.
     *
     * @param request The valid request object built up by {@link DwpEligibilityRequestResolver}
     * @return The identity and eligibility response.
     */
    @GetMapping
    @ApiOperation("Verify a person's identity and eligibility for benefits with DWP")
    @ApiResponses({@ApiResponse(code = 200,
            message = "The person's identity status and eligibility for DWP Benefits",
            response = IdentityAndEligibilityResponse.class)})
    public IdentityAndEligibilityResponse getBenefits(@ApiParam("The identity and eligibility request for DWP benefits")
                                                              DWPEligibilityRequestV2 request) {
        log.debug("Received eligibility request");

        return IdentityAndEligibilityResponse.builder().build();
    }
}

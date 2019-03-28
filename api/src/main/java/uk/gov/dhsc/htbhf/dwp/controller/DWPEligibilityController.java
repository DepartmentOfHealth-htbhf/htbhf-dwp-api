package uk.gov.dhsc.htbhf.dwp.controller;

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dhsc.htbhf.dwp.converter.EligibilityRequestToDWPEligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.DWPEligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.service.EligibilityService;

import javax.validation.Valid;

@RestController
@RequestMapping("v1/dwp/eligibility")
@AllArgsConstructor
@Slf4j
@Api(description = "Endpoints for dealing with DWP Eligibility requests.")
public class DWPEligibilityController {

    private final EligibilityService eligibilityService;
    private final EligibilityRequestToDWPEligibilityRequest converter;

    @PostMapping
    @ApiOperation("Retrieve the eligibility of a person for Universal Credit")
    @ApiResponses({@ApiResponse(code = 200, message = "The person's eligibility for Universal Credit", response = EligibilityResponse.class)})
    public EligibilityResponse getBenefits(@RequestBody
                                           @Valid
                                           @ApiParam("The eligibility request for Universal Credit")
                                           EligibilityRequest eligibilityRequest) {
        log.debug("Received eligibility request");

        DWPEligibilityRequest request = converter.convert(eligibilityRequest);

        return eligibilityService.checkEligibility(request);
    }
}

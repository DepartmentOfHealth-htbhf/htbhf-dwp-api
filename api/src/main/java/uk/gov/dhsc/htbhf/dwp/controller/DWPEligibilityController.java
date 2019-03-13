package uk.gov.dhsc.htbhf.dwp.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityResponse;
import uk.gov.dhsc.htbhf.dwp.service.EligibilityService;

import javax.validation.Valid;

@RestController
@RequestMapping("v1/dwp/eligibility")
@AllArgsConstructor
public class DWPEligibilityController {

    private final EligibilityService eligibilityService;

    @PostMapping
    public EligibilityResponse getBenefits(@RequestBody @Valid EligibilityRequest eligibilityRequest) {
        return eligibilityService.checkEligibility(eligibilityRequest);
    }
}

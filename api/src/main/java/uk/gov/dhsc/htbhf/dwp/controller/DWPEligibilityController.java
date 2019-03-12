package uk.gov.dhsc.htbhf.dwp.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;

import javax.validation.Valid;

@RestController
@RequestMapping("v1/dwp/eligibility")
public class DWPEligibilityController {

    @PostMapping
    public String getBenefits(@RequestBody @Valid PersonDTO person) {
        return null;
    }
}

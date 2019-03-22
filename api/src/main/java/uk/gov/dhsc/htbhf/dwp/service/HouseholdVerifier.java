package uk.gov.dhsc.htbhf.dwp.service;

import org.springframework.stereotype.Service;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;

@Service
public class HouseholdVerifier {
    public Boolean detailsMatch(UCHousehold household, PersonDTO person) {
        return true;
    }

    public Boolean detailsMatch(LegacyHousehold household, PersonDTO person) {
        return true;
    }
}

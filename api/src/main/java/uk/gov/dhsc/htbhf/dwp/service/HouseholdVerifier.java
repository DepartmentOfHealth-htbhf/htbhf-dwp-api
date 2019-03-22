package uk.gov.dhsc.htbhf.dwp.service;

import org.springframework.stereotype.Service;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;

@Service
public class HouseholdVerifier {
    public Boolean detailsMatch(UCHousehold household, PersonDTO person) {
        return household.getAdults().stream()
                .anyMatch(adult ->
                        adult.getForename().equals(person.getForename())
                        && adult.getSurname().equals(person.getSurname())
                        && adult.getAddressLine1().equals(person.getAddress().getAddressLine1())
                        && adult.getPostcode().equals(person.getAddress().getPostcode()));
    }

    public Boolean detailsMatch(LegacyHousehold household, PersonDTO person) {
        return true;
    }
}

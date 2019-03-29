package uk.gov.dhsc.htbhf.dwp.converter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import uk.gov.dhsc.htbhf.dwp.model.DWPEligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.DWPPersonDTO;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;

@Component
@AllArgsConstructor
public class EligibilityRequestToDWPEligibilityRequestConverter {

    public DWPEligibilityRequest convert(EligibilityRequest eligibilityRequest) {
        return DWPEligibilityRequest.builder()
                .person(convertPerson(eligibilityRequest.getPerson()))
                .eligibleStartDate(eligibilityRequest.getEligibleStartDate())
                .eligibleEndDate(eligibilityRequest.getEligibleEndDate())
                .ucMonthlyIncomeThreshold(eligibilityRequest.getUcMonthlyIncomeThreshold())
                .build();
    }

    public DWPPersonDTO convertPerson(PersonDTO person) {
        return DWPPersonDTO.builder()
                .address(person.getAddress())
                .dateOfBirth(person.getDateOfBirth())
                .forename(person.getFirstName())
                .surname(person.getLastName())
                .nino(person.getNino())
                .build();
    }
}

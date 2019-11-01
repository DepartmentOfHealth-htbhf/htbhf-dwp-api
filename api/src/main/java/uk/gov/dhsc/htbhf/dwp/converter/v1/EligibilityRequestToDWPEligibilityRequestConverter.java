package uk.gov.dhsc.htbhf.dwp.converter.v1;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import uk.gov.dhsc.htbhf.dwp.model.v1.DWPEligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.v1.DWPPersonDTO;
import uk.gov.dhsc.htbhf.dwp.model.v1.EligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.v1.PersonDTO;

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

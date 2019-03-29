package uk.gov.dhsc.htbhf.dwp.converter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import uk.gov.dhsc.htbhf.dwp.model.DWPEligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.DWPPersonDTO;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityRequest;

@Component
@AllArgsConstructor
public class EligibilityRequestToDWPEligibilityRequestConverter {

    private final PersonDTOToDWPPersonConverter converter;

    public DWPEligibilityRequest convert(EligibilityRequest eligibilityRequest) {
        DWPPersonDTO person = converter.convert(eligibilityRequest.getPerson());
        return DWPEligibilityRequest.builder()
                .person(person)
                .eligibleStartDate(eligibilityRequest.getEligibleStartDate())
                .eligibleEndDate(eligibilityRequest.getEligibleEndDate())
                .ucMonthlyIncomeThreshold(eligibilityRequest.getUcMonthlyIncomeThreshold())
                .build();
    }
}

package uk.gov.dhsc.htbhf.dwp.testhelper.v2;

import uk.gov.dhsc.htbhf.dwp.model.v2.DWPEligibilityRequestV2;
import uk.gov.dhsc.htbhf.dwp.model.v2.PersonDTOV2;

import java.time.LocalDate;

import static uk.gov.dhsc.htbhf.dwp.testhelper.TestConstants.UC_MONTHLY_INCOME_THRESHOLD_IN_PENCE;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v2.PersonDTOV2TestDataFactory.aValidPersonDTOV2;

public class DWPEligibilityRequestV2TestDataFactory {

    public static DWPEligibilityRequestV2 aValidDWPEligibilityRequestV2() {
        return validDWPEligibilityRequestBuilder()
                .build();
    }

    public static DWPEligibilityRequestV2 aValidDWPEligibilityRequestV2WithPerson(PersonDTOV2 person) {
        return validDWPEligibilityRequestBuilder()
                .person(person)
                .build();
    }

    public static DWPEligibilityRequestV2 aValidDWPEligibilityRequestV2WithEligibilityEndDate(LocalDate eligibilityEndDate) {
        return validDWPEligibilityRequestBuilder()
                .eligibilityEndDate(eligibilityEndDate)
                .build();
    }

    private static DWPEligibilityRequestV2.DWPEligibilityRequestV2Builder validDWPEligibilityRequestBuilder() {
        return DWPEligibilityRequestV2.builder()
                .person(aValidPersonDTOV2())
                .eligibilityEndDate(LocalDate.now().plusDays(28))
                .ucMonthlyIncomeThresholdInPence(UC_MONTHLY_INCOME_THRESHOLD_IN_PENCE);
    }

}

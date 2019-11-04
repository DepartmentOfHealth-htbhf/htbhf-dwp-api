package uk.gov.dhsc.htbhf.dwp.converter.v1;

import org.junit.jupiter.api.Test;
import uk.gov.dhsc.htbhf.dwp.model.v1.DWPEligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.v1.EligibilityRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v1.EligibilityRequestTestDataFactory.anEligibilityRequest;

public class EligibilityRequestToDWPEligibilityRequestTest {

    private EligibilityRequestToDWPEligibilityRequestConverter converter = new EligibilityRequestToDWPEligibilityRequestConverter();

    @Test
    void shouldConvertRequest() {
        EligibilityRequest request = anEligibilityRequest();

        DWPEligibilityRequest result = converter.convert(request);

        assertThat(result.getEligibleStartDate()).isEqualTo(request.getEligibleStartDate());
        assertThat(result.getEligibleEndDate()).isEqualTo(request.getEligibleEndDate());
        assertThat(result.getUcMonthlyIncomeThreshold()).isEqualTo(request.getUcMonthlyIncomeThreshold());
        assertThat(result.getPerson().getAddress()).isEqualTo(request.getPerson().getAddress());
        assertThat(result.getPerson().getDateOfBirth()).isEqualTo(request.getPerson().getDateOfBirth());
        assertThat(result.getPerson().getForename()).isEqualTo(request.getPerson().getFirstName());
        assertThat(result.getPerson().getSurname()).isEqualTo(request.getPerson().getLastName());
        assertThat(result.getPerson().getNino()).isEqualTo(request.getPerson().getNino());
    }
}

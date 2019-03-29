package uk.gov.dhsc.htbhf.dwp.converter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import uk.gov.dhsc.htbhf.dwp.model.DWPEligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.DWPPersonDTO;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static uk.gov.dhsc.htbhf.dwp.factory.DWPPersonDTOTestDataFactory.aValidDWPPerson;
import static uk.gov.dhsc.htbhf.dwp.helper.EligibilityRequestTestFactory.anEligibilityRequest;

@SpringBootTest
public class EligibilityRequestToDWPEligibilityRequestTest {

    @MockBean
    private PersonDTOToDWPPersonConverter personConverter;

    @Autowired
    private EligibilityRequestToDWPEligibilityRequestConverter converter;

    @Test
    void shouldConvertRequest() {
        EligibilityRequest request = anEligibilityRequest();
        DWPPersonDTO person = aValidDWPPerson();
        given(personConverter.convert(any())).willReturn(person);

        DWPEligibilityRequest result = converter.convert(request);

        assertThat(result.getEligibleStartDate()).isEqualTo(request.getEligibleStartDate());
        assertThat(result.getEligibleEndDate()).isEqualTo(request.getEligibleEndDate());
        assertThat(result.getUcMonthlyIncomeThreshold()).isEqualTo(request.getUcMonthlyIncomeThreshold());
        assertThat(result.getPerson()).isEqualTo(person);
        verify(personConverter).convert(request.getPerson());
    }
}

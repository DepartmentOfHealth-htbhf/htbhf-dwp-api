package uk.gov.dhsc.htbhf.dwp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.dhsc.htbhf.dwp.model.EligibilityRequest;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static uk.gov.dhsc.htbhf.dwp.helper.EligibilityResponseTestFactory.anEligibilityResponse;
import static uk.gov.dhsc.htbhf.dwp.helper.PersonTestFactory.aPerson;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class EligibilityServiceTest {

    @MockBean
    private DWPClient dwpClient;

    @Autowired
    private EligibilityService eligibilityService;

    @Test
    void shouldCreateEligibilityRequest() {
        var person = aPerson();
        given(dwpClient.checkEligibility(any(EligibilityRequest.class))).willReturn(anEligibilityResponse());

        var response = eligibilityService.checkEligibility(person);

        assertThat(response).isEqualTo(anEligibilityResponse());
        ArgumentCaptor<EligibilityRequest> argumentCaptor = ArgumentCaptor.forClass(EligibilityRequest.class);
        verify(dwpClient).checkEligibility(argumentCaptor.capture());
        assertEligibilityRequest(person, argumentCaptor.getValue());
    }

    private void assertEligibilityRequest(PersonDTO person, EligibilityRequest eligibilityRequest) {
        assertThat(eligibilityRequest.getPerson()).isEqualTo(person);
        assertThat(eligibilityRequest.getEligibleEndDate()).isNotNull();
        assertThat(eligibilityRequest.getEligibleStartDate()).isBefore(eligibilityRequest.getEligibleEndDate());
        assertThat(eligibilityRequest.getUcMonthlyIncomeThreshold()).isNotNull();
    }
}

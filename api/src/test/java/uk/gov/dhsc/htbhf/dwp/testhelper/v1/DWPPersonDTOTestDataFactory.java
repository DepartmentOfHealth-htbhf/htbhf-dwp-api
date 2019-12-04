package uk.gov.dhsc.htbhf.dwp.testhelper.v1;

import uk.gov.dhsc.htbhf.dwp.model.v1.DWPPersonDTO;

import static uk.gov.dhsc.htbhf.TestConstants.HOMER_DATE_OF_BIRTH;
import static uk.gov.dhsc.htbhf.TestConstants.HOMER_FORENAME;
import static uk.gov.dhsc.htbhf.TestConstants.HOMER_NINO_V1;
import static uk.gov.dhsc.htbhf.TestConstants.SIMPSON_SURNAME;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v1.AddressDTOTestDataFactory.aValidAddress;

public class DWPPersonDTOTestDataFactory {

    public static DWPPersonDTO aValidDWPPerson() {
        return buildDefaultDWPPerson().build();
    }

    private static DWPPersonDTO.DWPPersonDTOBuilder buildDefaultDWPPerson() {
        return DWPPersonDTO.builder()
                .dateOfBirth(HOMER_DATE_OF_BIRTH)
                .nino(HOMER_NINO_V1)
                .address(aValidAddress())
                .forename(HOMER_FORENAME)
                .surname(SIMPSON_SURNAME);
    }
}

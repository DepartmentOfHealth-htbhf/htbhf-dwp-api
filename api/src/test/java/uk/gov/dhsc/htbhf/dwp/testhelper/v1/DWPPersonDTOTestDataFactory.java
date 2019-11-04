package uk.gov.dhsc.htbhf.dwp.testhelper.v1;

import uk.gov.dhsc.htbhf.dwp.model.v1.DWPPersonDTO;

import static uk.gov.dhsc.htbhf.dwp.testhelper.v1.AddressDTOTestDataFactory.aValidAddress;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v1.TestConstants.HOMER_DOB;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v1.TestConstants.HOMER_FORENAME;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v1.TestConstants.HOMER_NINO;
import static uk.gov.dhsc.htbhf.dwp.testhelper.v1.TestConstants.SIMPSON_SURNAME;

public class DWPPersonDTOTestDataFactory {

    public static DWPPersonDTO aValidDWPPerson() {
        return buildDefaultDWPPerson().build();
    }

    private static DWPPersonDTO.DWPPersonDTOBuilder buildDefaultDWPPerson() {
        return DWPPersonDTO.builder()
                .dateOfBirth(HOMER_DOB)
                .nino(HOMER_NINO)
                .address(aValidAddress())
                .forename(HOMER_FORENAME)
                .surname(SIMPSON_SURNAME);
    }
}

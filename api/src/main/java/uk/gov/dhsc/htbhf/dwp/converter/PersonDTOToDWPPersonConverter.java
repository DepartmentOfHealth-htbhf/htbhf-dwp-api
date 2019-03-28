package uk.gov.dhsc.htbhf.dwp.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import uk.gov.dhsc.htbhf.dwp.model.DWPPersonDTO;
import uk.gov.dhsc.htbhf.dwp.model.PersonDTO;

@Component
public class PersonDTOToDWPPersonConverter implements Converter<PersonDTO, DWPPersonDTO> {

    @Override
    public DWPPersonDTO convert(PersonDTO person) {
        return DWPPersonDTO.builder()
                .address(person.getAddress())
                .dateOfBirth(person.getDateOfBirth())
                .forename(person.getFirstName())
                .surname(person.getLastName())
                .nino(person.getNino())
                .build();
    }
}

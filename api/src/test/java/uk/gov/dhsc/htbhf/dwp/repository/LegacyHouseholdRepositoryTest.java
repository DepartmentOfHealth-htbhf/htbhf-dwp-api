package uk.gov.dhsc.htbhf.dwp.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dhsc.htbhf.dwp.entity.LegacyHouseholdFactory.aHousehold;
import static uk.gov.dhsc.htbhf.dwp.entity.LegacyHouseholdFactory.aHouseholdWithNoAdults;
import static uk.gov.dhsc.htbhf.dwp.entity.LegacyHouseholdFactory.anAdultWithNino;

@SpringBootTest
class LegacyHouseholdRepositoryTest {

    @Autowired
    LegacyHouseholdRepository repository;

    @PersistenceContext
    EntityManager em;

    @AfterEach
    void afterEach() {
        repository.deleteAll();
    }

    @Test
    void saveAndRetrieveHousehold() {
        //Given
        LegacyHousehold household = aHousehold();

        //When
        LegacyHousehold savedHousehold = repository.save(household);

        //Then
        assertThat(savedHousehold.getId()).isEqualTo(household.getId());
        household.getChildren().forEach(child -> assertThat(em.contains(child)));
        household.getAdults().forEach(adult -> assertThat(em.contains(adult)));
        assertThat(savedHousehold).isEqualTo(household);
        assertThat(savedHousehold).isEqualToComparingFieldByFieldRecursively(household);
    }

    @Test
    void shouldFindLegacyHouseholdByNino() {
        String nino = "QQ111111A";
        LegacyHousehold household1Version1 = aHouseholdWithNoAdults().fileImportNumber(1).build().addAdult(anAdultWithNino(nino));
        LegacyHousehold household1Version2 = aHouseholdWithNoAdults().fileImportNumber(2).build().addAdult(anAdultWithNino(nino));
        LegacyHousehold household2Version1 = aHouseholdWithNoAdults().fileImportNumber(1).build().addAdult(anAdultWithNino("QQ222222C"));
        LegacyHousehold household2Version2 = aHouseholdWithNoAdults().fileImportNumber(2).build().addAdult(anAdultWithNino("QQ222222C"));
        repository.save(household1Version1);
        repository.save(household1Version2);
        repository.save(household2Version1);
        repository.save(household2Version2);

        Optional<LegacyHousehold> result = repository.findHouseholdByAdultWithNino(nino);

        assertThat(result).contains(household1Version2);
    }

    @Test
    void shouldFailToFindLegacyHouseholdByNino() {
        LegacyHousehold household = aHousehold();
        repository.save(household);

        Optional<LegacyHousehold> result = repository.findHouseholdByAdultWithNino("AB999999C");

        assertThat(result).isEmpty();
    }

}

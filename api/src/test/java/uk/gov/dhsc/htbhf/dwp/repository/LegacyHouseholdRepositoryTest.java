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
        String nino = "QQ123456A";
        LegacyHousehold household = aHousehold();
        repository.save(household);

        Optional<LegacyHousehold> result = repository.findHouseholdByAdultWithNino(nino);

        assertThat(result.get()).isEqualTo(household);
    }

    @Test
    void shouldFailToFindLegacyHouseholdByNino() {
        Optional<LegacyHousehold> result = repository.findHouseholdByAdultWithNino("QQ123456C");

        assertThat(result.isEmpty()).isTrue();
    }

}

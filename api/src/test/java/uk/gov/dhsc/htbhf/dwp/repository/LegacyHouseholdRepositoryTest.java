package uk.gov.dhsc.htbhf.dwp.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.dhsc.htbhf.dwp.entity.LegacyHouseholdFactory;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

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
        LegacyHousehold household = LegacyHouseholdFactory.aHousehold();

        //When
        LegacyHousehold savedHousehold = repository.save(household);

        //Then
        assertThat(savedHousehold.getId()).isEqualTo(household.getId());
        household.getChildren().forEach(child -> assertThat(em.contains(child)));
        household.getAdults().forEach(adult -> assertThat(em.contains(adult)));
        assertThat(savedHousehold).isEqualTo(household);
        assertThat(savedHousehold).isEqualToComparingFieldByFieldRecursively(household);
    }

}
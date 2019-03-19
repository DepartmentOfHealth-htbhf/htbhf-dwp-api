package uk.gov.dhsc.htbhf.dwp.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.dhsc.htbhf.dwp.entity.LegacyHouseholdFactory;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LegacyHouseholdRepositoryTest {

    @Autowired
    LegacyHouseholdRepository repository;

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
        assertThat(savedHousehold.getId()).isNotNull();
        savedHousehold.getChildren().forEach(child -> assertThat(child.getId()).isNotNull());
        savedHousehold.getAdults().forEach(adult -> assertThat(adult.getId()).isNotNull());
        assertThat(savedHousehold).isEqualTo(household);
        assertThat(savedHousehold).isEqualToComparingFieldByFieldRecursively(household);
    }

}
package uk.gov.dhsc.htbhf.dwp.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.dhsc.htbhf.dwp.entity.UCHouseholdFactory;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UCHouseholdRepositoryTest {

    @Autowired
    UCHouseholdRepository repository;

    @AfterEach
    void afterEach() {
        repository.deleteAll();
    }

    @Test
    void saveAndRetrieveHousehold() {
        //Given
        UCHousehold household = UCHouseholdFactory.aHousehold();

        //When
        UCHousehold savedHousehold = repository.save(household);

        //Then
        assertThat(savedHousehold.getId()).isNotNull();
        savedHousehold.getChildren().forEach(child -> assertThat(child.getId()).isNotNull());
        savedHousehold.getAdults().forEach(adult -> assertThat(adult.getId()).isNotNull());
        assertThat(savedHousehold).isEqualTo(household);
        assertThat(savedHousehold).isEqualToComparingFieldByFieldRecursively(household);
    }

}
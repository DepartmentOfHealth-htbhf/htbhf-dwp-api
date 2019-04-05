package uk.gov.dhsc.htbhf.dwp.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dhsc.htbhf.dwp.entity.UCHouseholdFactory.aUCAdultWithNino;
import static uk.gov.dhsc.htbhf.dwp.entity.UCHouseholdFactory.aUCHousehold;
import static uk.gov.dhsc.htbhf.dwp.entity.UCHouseholdFactory.aUCHouseholdWithNoAdultsOrChildren;

@SpringBootTest
class UCHouseholdRepositoryTest {

    @Autowired
    UCHouseholdRepository repository;

    @PersistenceContext
    EntityManager em;

    @AfterEach
    void afterEach() {
        repository.deleteAll();
    }

    @Test
    void saveAndRetrieveHousehold() {
        //Given
        UCHousehold household = aUCHousehold();

        //When
        UCHousehold savedHousehold = repository.save(household);

        //Then
        assertThat(savedHousehold.getId()).isEqualTo(household.getId());
        household.getChildren().forEach(child -> assertThat(em.contains(child)));
        household.getAdults().forEach(adult -> assertThat(em.contains(adult)));
        assertThat(savedHousehold).isEqualTo(household);
        assertThat(savedHousehold).isEqualToComparingFieldByFieldRecursively(household);
    }

    @Test
    void shouldFindMostRecentVersionOfUCHouseholdByNino() {
        String nino = "QQ111111A";
        UCHousehold household1Version1 = aUCHouseholdWithNoAdultsOrChildren().fileImportNumber(1).build().addAdult(aUCAdultWithNino(nino));
        UCHousehold household1Version2 = aUCHouseholdWithNoAdultsOrChildren().fileImportNumber(2).build().addAdult(aUCAdultWithNino(nino));
        UCHousehold household2Version1 = aUCHouseholdWithNoAdultsOrChildren().fileImportNumber(1).build().addAdult(aUCAdultWithNino("QQ222222C"));
        UCHousehold household2Version2 = aUCHouseholdWithNoAdultsOrChildren().fileImportNumber(2).build().addAdult(aUCAdultWithNino("QQ222222C"));
        repository.save(household1Version1);
        repository.save(household1Version2);
        repository.save(household2Version1);
        repository.save(household2Version2);

        Optional<UCHousehold> result = repository.findHouseholdByAdultWithNino(nino);

        assertThat(result).contains(household1Version2);
    }

    @Test
    void shouldFailToFindUCHouseholdByNino() {
        UCHousehold household = aUCHousehold();
        repository.save(household);

        Optional<UCHousehold> result = repository.findHouseholdByAdultWithNino("AB999999C");

        assertThat(result).isEmpty();
    }

}

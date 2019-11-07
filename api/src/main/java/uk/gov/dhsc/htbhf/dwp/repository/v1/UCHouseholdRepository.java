package uk.gov.dhsc.htbhf.dwp.repository.v1;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * JPA repository for Universal Credit households (members of the household are persisted via the household).
 */
public interface UCHouseholdRepository extends CrudRepository<UCHousehold, UUID> {

    /**
     * Return all households containing an adult with the given nino.
     *
     * @param nino The nino to check against
     * @return A stream containing all households found
     */
    @Query("SELECT household FROM UCHousehold household INNER JOIN FETCH household.adults adult "
            + "WHERE adult.nino = :nino")
    Stream<UCHousehold> findAllHouseholdsByAdultWithNino(@Param("nino") String nino);

    /**
     * Finds a household containing an adult with a matching nino. The household with the highest fileImportNumber
     * (most recent version) is the one returned.
     *
     * @param nino The nino to check against
     * @return An Optional containing a household if found
     */
    @Transactional(readOnly = true)
    default Optional<UCHousehold> findHouseholdByAdultWithNino(String nino) {
        Stream<UCHousehold> households = findAllHouseholdsByAdultWithNino(nino);
        return households.findFirst();
    }
}

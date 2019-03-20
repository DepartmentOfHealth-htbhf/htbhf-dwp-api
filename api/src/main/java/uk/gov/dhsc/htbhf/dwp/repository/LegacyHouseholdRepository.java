package uk.gov.dhsc.htbhf.dwp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA repository for legacy benefit households (members of the household are persisted via the household).
 */
public interface LegacyHouseholdRepository extends CrudRepository<LegacyHousehold, UUID> {

    @Query("SELECT household FROM LegacyHousehold household INNER JOIN household.adults adult WHERE adult.nino = :nino")
    Optional<LegacyHousehold> findHouseholdByAdultWithNino(@Param("nino") String nino);
}

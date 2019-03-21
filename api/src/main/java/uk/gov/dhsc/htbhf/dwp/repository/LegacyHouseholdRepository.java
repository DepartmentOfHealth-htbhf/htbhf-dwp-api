package uk.gov.dhsc.htbhf.dwp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * JPA repository for legacy benefit households (members of the household are persisted via the household).
 */
public interface LegacyHouseholdRepository extends CrudRepository<LegacyHousehold, UUID> {

    @Query("SELECT household FROM LegacyHousehold household INNER JOIN household.adults adult "
            + "WHERE adult.nino = :nino ORDER BY household.fileImportNumber DESC")
    Stream<LegacyHousehold> findAllHouseholdsByAdultWithNino(@Param("nino") String nino);

    @Transactional(readOnly = true)
    default Optional<LegacyHousehold> findHouseholdByAdultWithNino(String nino) {
        Stream<LegacyHousehold> households = findAllHouseholdsByAdultWithNino(nino);
        return households.findFirst();
    }
}

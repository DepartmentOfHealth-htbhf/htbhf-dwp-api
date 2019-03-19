package uk.gov.dhsc.htbhf.dwp.repository;

import org.springframework.data.repository.CrudRepository;
import uk.gov.dhsc.htbhf.dwp.entity.legacy.LegacyHousehold;

import java.util.UUID;

/**
 * JPA repository for legacy benefit households (members of the household are persisted via the household).
 */
public interface LegacyHouseholdRepository extends CrudRepository<LegacyHousehold, UUID> {
}

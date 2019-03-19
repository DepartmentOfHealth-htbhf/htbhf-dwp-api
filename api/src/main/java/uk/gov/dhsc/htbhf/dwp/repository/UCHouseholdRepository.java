package uk.gov.dhsc.htbhf.dwp.repository;

import org.springframework.data.repository.CrudRepository;
import uk.gov.dhsc.htbhf.dwp.entity.uc.UCHousehold;

import java.util.UUID;

/**
 * JPA repository for Universal Credit households (members of the household are persisted via the household).
 */
public interface UCHouseholdRepository extends CrudRepository<UCHousehold, UUID> {
}

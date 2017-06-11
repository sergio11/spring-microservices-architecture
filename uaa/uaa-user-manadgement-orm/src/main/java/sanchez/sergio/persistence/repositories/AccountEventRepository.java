package sanchez.sergio.persistence.repositories;

import sanchez.sergio.event.EventRepository;
import sanchez.sergio.persistence.entities.AccountEvent;

/**
 * @author sergio
 */
public interface AccountEventRepository extends EventRepository<AccountEvent, Long> {}

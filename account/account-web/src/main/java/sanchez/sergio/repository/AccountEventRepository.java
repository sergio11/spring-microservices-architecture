package sanchez.sergio.repository;

import sanchez.sergio.event.EventRepository;
import sanchez.sergio.events.AccountEvent;

/**
 *
 * @author sergio
 */
public interface AccountEventRepository extends EventRepository<AccountEvent, Long> {}
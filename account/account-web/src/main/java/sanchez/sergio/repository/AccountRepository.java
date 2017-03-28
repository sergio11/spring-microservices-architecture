package sanchez.sergio.repository;

import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import sanchez.sergio.domain.Account;
import sanchez.sergio.domain.AccountStatus;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountByEmail(@Param("email") String email);
    List<Account> findAllByStatusAndCreatedAtBefore(AccountStatus status, ZonedDateTime dateTime);
}

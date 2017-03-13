package sanchez.sergio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import sanchez.sergio.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountByEmail(@Param("email") String email);
}

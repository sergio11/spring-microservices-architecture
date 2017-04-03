package sanchez.sergio.domain.account;

/**
 * The {@link AccountStatus} describes the state of an {@link Account}.
 * The aggregate state of a {@link Account} is sourced from attached domain
 * events in the form of {@link sanchez.sergio.event.AccountEvent}.
 *
 * @author sergio
 */
public enum AccountStatus {
    ACCOUNT_CREATED,
    ACCOUNT_PENDING,
    ACCOUNT_CONFIRMED,
    ACCOUNT_ACTIVE,
    ACCOUNT_SUSPENDED,
    ACCOUNT_ARCHIVED
}

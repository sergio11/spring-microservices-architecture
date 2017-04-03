package sanchez.sergio.domain.event;

import sanchez.sergio.domain.BaseEntity;

/**
 * The domain event {@link AccountEvent} tracks the type and state of events as
 * applied to the {@link Account} domain object. This event resource can be used
 * to event source the aggregate state of {@link Account}.
 * This event resource also provides a transaction log that can be used to append
 * actions to the event.
 * @author sergio
 */
public class AccountEvent extends BaseEntity {

    private AccountEventType type;

    public AccountEvent() {
    }

    public AccountEvent(AccountEventType type) {
        this.type = type;
    }

    public AccountEventType getType() {
        return type;
    }

    public void setType(AccountEventType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AccountEvent{" +
                "type=" + type +
                "} " + super.toString();
    }
}

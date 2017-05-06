package sanchez.sergio.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.Link;

import javax.persistence.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import sanchez.sergio.controller.UserController;
import sanchez.sergio.persistence.entities.User;
import sanchez.sergio.event.Event;

/**
 * The domain event {@link AccountEvent} tracks the type and state of events as applied to the {@link User} domain
 * object. This event resource can be used to event source the aggregate state of {@link User}.
 * <p>
 * This event resource also provides a transaction log that can be used to append actions to the event.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "USERS_EVENTS")
public class AccountEvent extends Event<User, AccountEventType, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="events_id")
    private Long eventId;
   
    @Enumerated(EnumType.STRING)
    private AccountEventType type;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="user_identity")
    @JsonIgnore
    private User entity;

    @CreatedDate
    @Column(name="created_at")
    private Long createdAt;

    @LastModifiedDate
    @Column(name="last_modified")
    private Long lastModified;

    public AccountEvent() {}

    public AccountEvent(AccountEventType type) {
        this.type = type;
    }

    public AccountEvent(AccountEventType type, User entity) {
        this.type = type;
        this.entity = entity;
    }
    
    

    @Override
    public Long getEventId() {
        return eventId;
    }

    @Override
    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
    
    @Override
    public AccountEventType getType() {
        return type;
    }

    @Override
    public void setType(AccountEventType type) {
        this.type = type;
    }

    @Override
    public User getEntity() {
        return entity;
    }

    @Override
    public void setEntity(User entity) {
        this.entity = entity;
    }

    @Override
    public Long getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Long getLastModified() {
        return lastModified;
    }

    @Override
    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public Link getId() {
        return linkTo(UserController.class).slash("accounts").slash(getEntity().getIdentity()).slash("events")
                .slash(getId()).withSelfRel();
    }
    
    @Override
    public String toString() {
        return "AccountEvent{" +
                "id=" + eventId +
                ", type=" + type +
                ", entity=" + entity +
                ", createdAt=" + createdAt +
                ", lastModified=" + lastModified +
                "} " + super.toString();
    }
}

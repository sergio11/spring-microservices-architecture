package sanchez.sergio.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sanchez.sergio.event.Event;

/**
 * JPA also defines a mapped superclass concept defined though the @MappedSuperclass
 * annotation or the <mapped-superclass> element. A mapped superclass is not a persistent class, 
 * but allow common mappings to be defined for its subclasses.
 * @author sergio
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity<E extends Event, T extends Serializable> extends Aggregate<E, T> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected T identity;

    @CreatedDate
    @Column(name="createdAt")
    private Long createdAt;

    @LastModifiedDate
    @Column(name="lastModified")
    private Long lastModified;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<E> events = new ArrayList<>();

    public AbstractEntity() {
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    @JsonIgnore
    @Override
    public List<E> getEvents() {
        return events;
    }

    public void setEvents(List<E> events) {
        this.events = events;
    }

    public T getIdentity() {
        return identity;
    }

    public void setIdentity(T identity) {
        this.identity = identity;
    }
    
    @Override
    public String toString() {
        return "BaseEntity{"
                + "createdAt=" + createdAt
                + ", lastModified=" + lastModified
                + '}';
    }
}

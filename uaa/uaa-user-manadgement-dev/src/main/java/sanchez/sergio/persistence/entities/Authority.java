package sanchez.sergio.persistence.entities;

import sanchez.sergio.persistence.entities.User;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author sergio
 */
@Entity
@Table(name = "AUTHORITIES")
public class Authority implements GrantedAuthority, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, unique = true)
    private AuthorityEnum type;
    private String description;
    @ManyToMany(mappedBy = "authorities", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<User> users = new HashSet();

    public Authority(AuthorityEnum type, String description) {
        this.type = type;
        this.description = description;
    }

    public Authority() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuthorityEnum getType() {
        return type;
    }

    public void setType(AuthorityEnum type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        if (!this.users.contains(user)) {
            this.users.add(user);
            user.addAuthority(this);
        }
    }
    
    @Override
    public String getAuthority() {
        return type.name();
    }

    @Override
    public String toString() {
        return "Authority{" + "id=" + id + ", type=" + type + ", description=" + description + '}';
    }
}

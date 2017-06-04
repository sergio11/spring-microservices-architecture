package sanchez.sergio.config.properties;

import java.io.Serializable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 *
 * @author sergio
 */
@Component
@ConfigurationProperties(prefix="ldap")
@RefreshScope
public class LdapCustomProperties implements Serializable {
    
    private String uri;
    private String adminDN;
    private String adminPassword;
    private String searchBase; //ou=organization;
    private String searchFilter; //uid={0}
    private String groupSearchBase; //ou=groups
    private String groupSearchFilter; //memberUid={1}
    private String groupRoleAttribute; //cn
    
   
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getAdminDN() {
        return adminDN;
    }

    public void setAdminDN(String adminDN) {
        this.adminDN = adminDN;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getSearchBase() {
        return searchBase;
    }

    public void setSearchBase(String searchBase) {
        this.searchBase = searchBase;
    }

    public String getSearchFilter() {
        return searchFilter;
    }

    public void setSearchFilter(String searchFilter) {
        this.searchFilter = searchFilter;
    }

    public String getGroupSearchBase() {
        return groupSearchBase;
    }

    public void setGroupSearchBase(String groupSearchBase) {
        this.groupSearchBase = groupSearchBase;
    }

    public String getGroupSearchFilter() {
        return groupSearchFilter;
    }

    public void setGroupSearchFilter(String groupSearchFilter) {
        this.groupSearchFilter = groupSearchFilter;
    }

    public String getGroupRoleAttribute() {
        return groupRoleAttribute;
    }

    public void setGroupRoleAttribute(String groupRoleAttribute) {
        this.groupRoleAttribute = groupRoleAttribute;
    }
    
}

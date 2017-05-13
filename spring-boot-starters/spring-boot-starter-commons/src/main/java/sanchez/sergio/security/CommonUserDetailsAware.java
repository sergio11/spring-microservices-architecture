/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanchez.sergio.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author sergio
 */
public interface CommonUserDetailsAware<T> extends UserDetails {
    T getUserId();
    String getUsername();
    String getFirstName();
    String getLastName();
    String getEmail();
}

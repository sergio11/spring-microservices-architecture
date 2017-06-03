/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanchez.sergio.security.userdetails;

import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author sergio
 */
public interface CommonUserDetailsAware<T> extends UserDetails {
    
    final String ID = "id";
    final String USERNAME = "USERNAME";
    final String PASSWORD = "PASSWORD";
    final String FULL_NAME = "FULL_NAME";
    final String EMAIL = "EMAIL";
    
    
    T getUserId();
    String getUsername();
    String getPassword();
    String getFullName();
    String getEmail();
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanchez.sergio.service;

import sanchez.sergio.security.CommonUserDetailsAware;

/**
 *
 * @author sergio
 */
public interface IAuthenticationService {
    CommonUserDetailsAware<Long> getPrincipal();
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanchez.sergio.domain.account;

import sanchez.sergio.domain.BaseEntity;

/**
 * The {@link Account} domain object contains information related to
 * a user's account. The status of an account is event sourced using
 * events logged to the {@link AccountEvent} collection attached to
 * this resource.
 *
 * @author sergio
 */
public class Account extends BaseEntity {
    
    private String firstName;
    private String lastName;
    private String email;
    private AccountStatus status;

    public Account() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}

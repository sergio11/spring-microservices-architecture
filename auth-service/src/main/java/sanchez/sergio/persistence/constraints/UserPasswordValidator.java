/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanchez.sergio.persistence.constraints;

import sanchez.sergio.domain.User;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author sergio
 */
public class UserPasswordValidator implements ConstraintValidator<UserCurrentPassword, String> {
    

    @Override
    public void initialize(UserCurrentPassword constraintAnnotation) {}

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return false;
    }
}

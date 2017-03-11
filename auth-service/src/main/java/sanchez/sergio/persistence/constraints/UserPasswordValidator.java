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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author sergio
 */
public class UserPasswordValidator implements ConstraintValidator<UserCurrentPassword, String> {
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void initialize(UserCurrentPassword constraintAnnotation) {}

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if(password == null) return false;
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user != null){
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }
}

package sanchez.sergio.persistence.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import sanchez.sergio.persistence.repositories.UserRepository;

/**
 *
 * @author sergio
 */
public class UsernameUniqueValidator implements ConstraintValidator<UsernameUnique, String> {
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UsernameUnique constraintAnnotation) {}

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return userRepository.existsUserWithUsername(username) > 0 ? false : true;
    }
}

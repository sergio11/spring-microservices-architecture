package sanchez.sergio.persistence.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.BeanUtils;

/**
 * @author sergio
 */
public class FieldNotMatchValidator implements ConstraintValidator<FieldNotMatch, Object>
{
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(final FieldNotMatch constraintAnnotation)
    {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context)
    {
        try
        {
            final Object firstObj = BeanUtils.getProperty(value, firstFieldName);
            final Object secondObj = BeanUtils.getProperty(value, secondFieldName);

            return firstObj == null && secondObj == null || firstObj != null && !firstObj.equals(secondObj);
        }
        catch (final Exception ignore)
        {
            // ignore
        }
        return true;
    }
}

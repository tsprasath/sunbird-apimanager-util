package in.ekstep.am.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.springframework.util.StringUtils.containsWhitespace;

public class NoSpaceValidator implements ConstraintValidator<NoSpace, String> {
  @Override
  public void initialize(NoSpace constraintAnnotation) {

  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return !containsWhitespace(value);
  }
}

package in.ekstep.am.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class NotUuidValidator implements ConstraintValidator<NotUuid, String> {

  private static final String AM_UUID_REGEX_PATTERN = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
  private Pattern pattern;

  public NotUuidValidator() {
    pattern = Pattern.compile(AM_UUID_REGEX_PATTERN);
  }

  @Override
  public void initialize(NotUuid constraintAnnotation) {

  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.trim().isEmpty()) {
      return true;
    }

    return !pattern.matcher(value).matches();
  }
}

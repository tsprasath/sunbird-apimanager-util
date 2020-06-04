package in.ekstep.am.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = NoSpaceValidator.class)
@Documented
public @interface NoSpace {

  String message() default "{org.hibernate.validator.constraints.NoSpace.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  @Target({FIELD, PARAMETER, ANNOTATION_TYPE})
  @Retention(RUNTIME)
  @Documented
  public @interface List {
    NoSpace[] value();
  }
}

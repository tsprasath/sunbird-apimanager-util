package in.ekstep.am.dto.group;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class GrantConsumerRequestDetailsTest {
  private static javax.validation.Validator validator;

  @BeforeClass
  public static void setUpValidator() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void shouldNotAllowRequestWithEmptyGroups() {
    GrantConsumerRequestDetails grantConsumerRequestDetails = new GrantConsumerRequestDetails(new String[]{});

    Set<ConstraintViolation<GrantConsumerRequestDetails>> constraintViolations = validator.validate(grantConsumerRequestDetails);

    assertEquals(1, constraintViolations.size());
    assertEquals("GROUPS ARE MANDATORY", constraintViolations.iterator().next().getMessage());

  }

  @Test
  public void shouldNotAllowRequestWithNullGroups() {
    GrantConsumerRequestDetails grantConsumerRequestDetails = new GrantConsumerRequestDetails(null);

    Set<ConstraintViolation<GrantConsumerRequestDetails>> constraintViolations = validator.validate(grantConsumerRequestDetails);

    assertEquals(1, constraintViolations.size());
    assertEquals("GROUPS ARE MANDATORY", constraintViolations.iterator().next().getMessage());

  }
}
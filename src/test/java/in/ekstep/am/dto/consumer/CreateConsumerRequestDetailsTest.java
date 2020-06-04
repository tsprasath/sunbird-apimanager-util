package in.ekstep.am.dto.consumer;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class CreateConsumerRequestDetailsTest {

  private static javax.validation.Validator validator;

  @BeforeClass
  public static void setUpValidator() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void detailsWithNullUsernameShouldBeInvalidated() {
    CreateConsumerRequestDetails withNullUsername = new CreateConsumerRequestDetails(null);

    Set<ConstraintViolation<CreateConsumerRequestDetails>> constraintViolations = validator.validate(withNullUsername);

    assertEquals(1, constraintViolations.size());
    assertEquals("USERNAME IS MANDATORY", constraintViolations.iterator().next().getMessage());
  }

  @Test
  public void detailsWithUsernameHavingSpaceShouldBeInvalidated() {
    CreateConsumerRequestDetails withNullUsername = new CreateConsumerRequestDetails("user name with space");

    Set<ConstraintViolation<CreateConsumerRequestDetails>> constraintViolations = validator.validate(withNullUsername);

    assertEquals(1, constraintViolations.size());
    assertEquals("USERNAME MUST NOT HAVE SPACE", constraintViolations.iterator().next().getMessage());
  }

  @Test
  public void detailsOfTypeUUIDShouldBeInvalidated() {
    CreateConsumerRequestDetails withNullUsername = new CreateConsumerRequestDetails(UUID.randomUUID().toString());

    Set<ConstraintViolation<CreateConsumerRequestDetails>> constraintViolations = validator.validate(withNullUsername);

    assertEquals(1, constraintViolations.size());
    assertEquals("USERNAME MUST NOT BE A UUID", constraintViolations.iterator().next().getMessage());
  }

}
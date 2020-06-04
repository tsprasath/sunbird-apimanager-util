package in.ekstep.am.dto.consumer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import in.ekstep.am.constraint.NoSpace;
import in.ekstep.am.constraint.NotUuid;
import org.hibernate.validator.constraints.NotEmpty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateConsumerRequestDetails {
  @JsonProperty
  @NotEmpty(message = "USERNAME IS MANDATORY")
  @NoSpace(message = "USERNAME MUST NOT HAVE SPACE")
  @NotUuid(message = "USERNAME MUST NOT BE A UUID")
  private String username;

  private CreateConsumerRequestDetails() {
  }

  public CreateConsumerRequestDetails(String username) {
    this.username = username;
  }

  public String username() {
    return username;
  }

  @Override
  public String toString() {
    return "CreateConsumerRequestDetails{" +
        "username='" + username + '\'' +
        '}';
  }
}

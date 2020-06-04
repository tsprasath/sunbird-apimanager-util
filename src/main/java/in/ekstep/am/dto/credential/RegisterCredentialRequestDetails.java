package in.ekstep.am.dto.credential;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import in.ekstep.am.constraint.NoSpace;
import in.ekstep.am.constraint.NotUuid;
import org.hibernate.validator.constraints.NotEmpty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterCredentialRequestDetails {

  @JsonProperty
  @NotEmpty(message = "KEY IS MANDATORY")
  @NoSpace(message = "KEY MUST NOT HAVE SPACE")
  @NotUuid(message = "KEY MUST NOT BE A UUID")
  private String key;

  public String key() {
    return key;
  }

  private RegisterCredentialRequestDetails() {
  }

  public RegisterCredentialRequestDetails(String key) {
    this.key = key;
  }
}

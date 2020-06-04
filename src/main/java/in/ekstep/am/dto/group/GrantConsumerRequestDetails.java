package in.ekstep.am.dto.group;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GrantConsumerRequestDetails {

  @JsonProperty
  @NotEmpty(message = "GROUPS ARE MANDATORY")
  private String[] groups;

  public String[] groups() {
    return groups;
  }

  private GrantConsumerRequestDetails() {
  }

  public GrantConsumerRequestDetails(String[] groups) {
    this.groups = groups;
  }
}

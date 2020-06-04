package in.ekstep.am.dto.consumer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsumerDetails {
  @JsonProperty
  private String username;
  @JsonProperty
  private String[] groups;

  protected ConsumerDetails() {

  }

  public ConsumerDetails(String username, String[] groups) {
    this.username = username;
    this.groups = groups;
  }

  public String getString(final String name) {
    return name + "{" +
        "groups='" + groups + '\'' +
        ", username='" + username + '\'' +
        '}';
  }

}

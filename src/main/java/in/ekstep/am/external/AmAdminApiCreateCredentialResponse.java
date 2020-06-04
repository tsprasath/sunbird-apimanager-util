package in.ekstep.am.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmAdminApiCreateCredentialResponse {
  @JsonProperty
  private String key;
  @JsonProperty
  private String secret;

  private AmAdminApiCreateCredentialResponse() {
  }

  public String key() {
    return key;
  }

  public String secret() {
    return secret;
  }
}

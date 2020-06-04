package in.ekstep.am.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmAdminApiGetCredentialResponse {
  @JsonProperty
  private String key;
  @JsonProperty
  private String secret;

  private AmAdminApiGetCredentialResponse() {
  }

  public String key() {
    return key;
  }

  public String secret() {
    return secret;
  }

}

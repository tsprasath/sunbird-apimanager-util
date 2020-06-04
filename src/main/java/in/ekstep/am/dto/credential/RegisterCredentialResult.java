package in.ekstep.am.dto.credential;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterCredentialResult {
  @JsonProperty
  private String key;
  @JsonProperty
  private String secret;
  @JsonProperty
  private String token;

  private RegisterCredentialResult() {
  }

  public RegisterCredentialResult(String key, String secret) {
    this.key = key;
    this.secret = secret;
  }

  public RegisterCredentialResult(String key, String secret, String token) {
    this.key = key;
    this.secret = secret;
    this.token = token;
  }

  @Override
  public String toString() {
    return "CreateConsumerResult{" +
        "key='" + key + '\'' +
        ", secret='" + secret + '\'' +
        '}';
  }
}

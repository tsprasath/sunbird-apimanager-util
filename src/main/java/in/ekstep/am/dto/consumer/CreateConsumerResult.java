package in.ekstep.am.dto.consumer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateConsumerResult {
  @JsonProperty
  private String key;
  @JsonProperty
  private String secret;
  @JsonProperty
  private String username;

  private CreateConsumerResult() {
  }

  public CreateConsumerResult(String key, String secret, String username) {
    this.key = key;
    this.secret = secret;
    this.username = username;
  }

  @Override
  public String toString() {
    return "CreateConsumerResult{" +
        "key='" + key + '\'' +
        ", secret='" + secret + '\'' +
        ", username='" + username + '\'' +
        '}';
  }
}

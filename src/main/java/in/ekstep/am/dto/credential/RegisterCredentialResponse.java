package in.ekstep.am.dto.credential;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import in.ekstep.am.dto.ResponseParams;
import in.ekstep.am.dto.ResponseStatus;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterCredentialResponse {
  @JsonProperty
  private String id;
  @JsonProperty
  private String ver;
  @JsonProperty
  private long ets;
  @JsonProperty
  private ResponseParams params;
  @JsonProperty
  private RegisterCredentialResult result;

  private RegisterCredentialResponse() {
  }

  public RegisterCredentialResponse(String id, String ver, long ets, ResponseParams params, RegisterCredentialResult result) {
    this.id = "ekstep.api.am.adminutil.consumer.create";
    this.ver = ver;
    this.ets = ets;
    this.params = params;
    this.result = result;
  }

  @Override
  public String toString() {
    return "CreateConsumerResponse{" +
        "id='" + id + '\'' +
        ", ver='" + ver + '\'' +
        ", ets=" + ets +
        ", params=" + params +
        ", result=" + result +
        '}';
  }

  public boolean successful() {
    return params.status() == ResponseStatus.successful;
  }
}

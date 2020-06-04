package in.ekstep.am.dto.consumer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateConsumerRequest {
  @JsonProperty
  private String id;

  @JsonProperty
  private String ver;

  @JsonProperty
  private long ets;

  @JsonProperty
  private Map<String, Object> params;

  @JsonProperty
  @Valid
  @NotNull(message = "REQUEST IS MANDATORY")
  private CreateConsumerRequestDetails request;

  private CreateConsumerRequest() {
  }

  public CreateConsumerRequest(Map<String, Object> params, CreateConsumerRequestDetails request) {
    this.params = params;
    this.request = request;
  }

  public String msgid() {
    return params == null ? "" : params.get("msgid") == null ? "" : (String) params.get("msgid");
  }

  public String username() {
    return request.username();
  }

  public CreateConsumerRequestDetails getRequest() {
    return request;
  }

  @Override
  public String toString() {
    return "CreateConsumerRequest{" +
        "id='" + id + '\'' +
        ", ver='" + ver + '\'' +
        ", ets=" + ets +
        ", params=" + params +
        ", request=" + request +
        '}';
  }
}

package in.ekstep.am.dto.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import in.ekstep.am.dto.ResponseParams;

public class GrantConsumerResponse {
  @JsonProperty
  private String id;
  @JsonProperty
  private String ver;
  @JsonProperty
  private long ets;
  @JsonProperty
  private ResponseParams params;
  @JsonProperty
  private GrantConsumerResult result;

  private GrantConsumerResponse() {

  }

  public GrantConsumerResponse(String id, String ver, long ets, ResponseParams params, GrantConsumerResult result) {
    this.id = id;
    this.ver = ver;
    this.ets = ets;
    this.params = params;
    this.result = result;
  }

  @Override
  public String toString() {
    return "GrantConsumerResponse{" +
        "id='" + id + '\'' +
        ", ver='" + ver + '\'' +
        ", ets=" + ets +
        ", params=" + params +
        ", result=" + result +
        '}';
  }
}

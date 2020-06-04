package in.ekstep.am.dto.consumer;

import com.fasterxml.jackson.annotation.JsonProperty;
import in.ekstep.am.dto.ResponseParams;

public class ReadConsumerResponse {
  @JsonProperty
  private String id;
  @JsonProperty
  private String ver;
  @JsonProperty
  private long ets;
  @JsonProperty
  private ResponseParams params;
  @JsonProperty
  private ReadConsumerResult result;

  private ReadConsumerResponse() {

  }

  public ReadConsumerResponse(String id, String ver, long ets, ResponseParams params, ReadConsumerResult result) {
    this.id = id;
    this.ver = ver;
    this.ets = ets;
    this.params = params;
    this.result = result;
  }

  @Override
  public String toString() {
    return "ReadConsumerResponse{" +
        "id='" + id + '\'' +
        ", ver='" + ver + '\'' +
        ", ets=" + ets +
        ", params=" + params +
        ", result=" + result +
        '}';
  }
}

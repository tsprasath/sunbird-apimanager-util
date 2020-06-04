package in.ekstep.am.dto.health;

import com.fasterxml.jackson.annotation.JsonProperty;
import in.ekstep.am.dto.ResponseParams;

public class HealthCheckResponse {
  @JsonProperty
  private String id = "ekstep.am.adminutil.health";
  @JsonProperty
  private String ver = "1.0";
  @JsonProperty
  private long ets;
  @JsonProperty
  private ResponseParams params;
  @JsonProperty
  private HealthCheckResponseResult result;

  private HealthCheckResponse() {
  }

  public HealthCheckResponse(long ets, ResponseParams params, HealthCheckResponseResult result) {
    this.ets = ets;
    this.params = params;
    this.result = result;
  }

  @Override
  public String toString() {
    return "HealthCheckResponse{" +
        "id='" + id + '\'' +
        ", ver='" + ver + '\'' +
        ", ets=" + ets +
        ", params=" + params +
        ", result=" + result +
        '}';
  }
}

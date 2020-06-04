package in.ekstep.am.dto.health;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class HealthCheckResponseResult {
  @JsonProperty
  private String name = "am.adminutil";
  @JsonProperty
  private boolean healthy;
  @JsonProperty
  private List<HealthCheckResult> checks;

  private HealthCheckResponseResult() {
  }

  public HealthCheckResponseResult(boolean healthy, List<HealthCheckResult> checks) {
    this.healthy = healthy;
    this.checks = checks;
  }

  @Override
  public String toString() {
    return "HealthCheckResponseResult{" +
        "name='" + name + '\'' +
        ", healthy=" + healthy +
        '}';
  }
}

package in.ekstep.am.dto.health;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HealthCheckResult {
  @JsonProperty
  private String name;
  @JsonProperty
  private boolean healthy;
  @JsonProperty
  private String err;
  @JsonProperty
  private String errmsg;

  public HealthCheckResult(String name, boolean healthy) {
    this(name, healthy, null, null);
  }

  public HealthCheckResult(String name, boolean healthy, String err, String errmsg) {
    this.name = name;
    this.healthy = healthy;
    this.err = err;
    this.errmsg = errmsg;
  }

  @Override
  public String toString() {
    return "HealthCheckResult{" +
        "name='" + name + '\'' +
        ", healthy=" + healthy +
        ", err=" + err +
        ", errmsg=" + errmsg +
        '}';
  }
}

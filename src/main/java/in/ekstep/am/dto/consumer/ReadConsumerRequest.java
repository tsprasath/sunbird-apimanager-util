package in.ekstep.am.dto.group;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReadConsumerRequest {
  @JsonProperty
  private String id;

  @JsonProperty
  private String ver;

  @JsonProperty
  private long ets;

  @JsonProperty
  private Map<String, Object> params;

  private ReadConsumerRequest() {

  }

  public ReadConsumerRequest(Map<String, Object> params) {
    this.params = params;
  }

  public String msgid() {
    return params == null ? "" : params.get("msgid") == null ? "" : (String) params.get("msgid");
  }

}

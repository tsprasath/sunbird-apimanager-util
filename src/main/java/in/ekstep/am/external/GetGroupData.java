package in.ekstep.am.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetGroupData {


  @JsonProperty
  private String group;

  @JsonProperty
  private String consumer_id;

  @JsonProperty
  private String id;

  private GetGroupData() {
  }

  public List<String> groups() {
    return Arrays.asList(group.split(","));
  }

  public String groupString() {
    return group;
  }

  public String consumerId() {
    return consumer_id;
  }

  public String id() {
    return id;
  }
}

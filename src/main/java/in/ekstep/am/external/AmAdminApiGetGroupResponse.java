package in.ekstep.am.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmAdminApiGetGroupResponse {
  @JsonProperty
  private List<GetGroupData> data;

  private AmAdminApiGetGroupResponse() {
  }

  public List<GetGroupData> data() {
    return data;
  }

}

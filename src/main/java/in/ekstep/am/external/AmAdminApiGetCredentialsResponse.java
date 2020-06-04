package in.ekstep.am.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmAdminApiGetCredentialsResponse {
  @JsonProperty
  private List<GetCredentialsData> data;

  private AmAdminApiGetCredentialsResponse() {
  }

  public List<GetCredentialsData> data() {
    return data;
  }

}

package in.ekstep.am.dto.group;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import in.ekstep.am.dto.consumer.ConsumerDetails;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GrantConsumerResult extends ConsumerDetails {

  private GrantConsumerResult() {

  }

  public GrantConsumerResult(String username, String[] groups) {
    super(username, groups);
  }

  @Override
  public String toString() {
    return getString("GrantConsumerResult");
  }
}

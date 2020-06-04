package in.ekstep.am.dto.consumer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReadConsumerResult extends ConsumerDetails {
  private ReadConsumerResult() {
  }

  public ReadConsumerResult(String username, String[] groups) {
    super(username, groups);
  }

  @Override
  public String toString() {
    return getString("ReadConsumerResult");
  }
}

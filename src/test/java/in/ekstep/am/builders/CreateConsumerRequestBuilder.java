package in.ekstep.am.builders;

import in.ekstep.am.dto.consumer.CreateConsumerRequest;
import in.ekstep.am.dto.consumer.CreateConsumerRequestDetails;

import java.util.HashMap;
import java.util.Map;

public class CreateConsumerRequestBuilder {

  private String username = "Purandara";
  private Map<String, Object> params = new HashMap<>();
  private boolean nullRequest;

  public CreateConsumerRequest build() {
    return new CreateConsumerRequest(params, nullRequest ? null : new CreateConsumerRequestDetails(this.username));
  }

  public CreateConsumerRequestBuilder withUsername(String username) {
    this.username = username;
    return this;
  }

  public CreateConsumerRequestBuilder withParams(Map<String, Object> params) {
    this.params = params;
    return this;
  }

  public CreateConsumerRequestBuilder withNullRequest() {
    nullRequest = true;
    return this;
  }
}

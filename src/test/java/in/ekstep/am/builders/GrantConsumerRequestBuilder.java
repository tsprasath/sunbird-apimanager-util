package in.ekstep.am.builders;

import in.ekstep.am.dto.group.GrantConsumerRequest;
import in.ekstep.am.dto.group.GrantConsumerRequestDetails;

import java.util.HashMap;
import java.util.Map;

public class GrantConsumerRequestBuilder {

  private String[] groups = {"group1"};
  private Map<String, Object> params = new HashMap<>();
  private boolean nullRequest;

  public GrantConsumerRequest build() {
    return new GrantConsumerRequest(params, nullRequest ? null : new GrantConsumerRequestDetails(groups));
  }

  public GrantConsumerRequestBuilder withGroups(String[] groups) {
    this.groups = groups;
    return this;
  }

  public GrantConsumerRequestBuilder withParams(Map<String, Object> params) {
    this.params = params;
    return this;
  }

  public GrantConsumerRequestBuilder withNullRequest() {
    nullRequest = true;
    return this;
  }
}

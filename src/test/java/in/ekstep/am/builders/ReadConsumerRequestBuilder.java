package in.ekstep.am.builders;

import in.ekstep.am.dto.group.ReadConsumerRequest;

import java.util.HashMap;
import java.util.Map;

public class ReadConsumerRequestBuilder {

  private Map<String, Object> params = new HashMap<>();

  public ReadConsumerRequest build() {
    return new ReadConsumerRequest(params);
  }

  public ReadConsumerRequestBuilder withParams(Map<String, Object> params) {
    this.params = params;
    return this;
  }

}

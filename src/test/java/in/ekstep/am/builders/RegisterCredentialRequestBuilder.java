package in.ekstep.am.builders;

import in.ekstep.am.dto.credential.RegisterCredentialRequest;
import in.ekstep.am.dto.credential.RegisterCredentialRequestDetails;

import java.util.HashMap;
import java.util.Map;

public class RegisterCredentialRequestBuilder {

  private String key = "key1";
  private Map<String, Object> params = new HashMap<>();
  private boolean nullRequest;

  public RegisterCredentialRequest build() {
    return new RegisterCredentialRequest(params, nullRequest ? null : new RegisterCredentialRequestDetails(key));
  }

  public RegisterCredentialRequestBuilder withKey(String key) {
    this.key = key;
    return this;
  }

  public RegisterCredentialRequestBuilder withParams(Map<String, Object> params) {
    this.params = params;
    return this;
  }

  public RegisterCredentialRequestBuilder withNullRequest() {
    nullRequest = true;
    return this;
  }
}

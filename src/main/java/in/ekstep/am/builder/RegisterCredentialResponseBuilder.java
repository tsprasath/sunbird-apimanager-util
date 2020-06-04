package in.ekstep.am.builder;

import in.ekstep.am.dto.ResponseParams;
import in.ekstep.am.dto.credential.RegisterCredentialResponse;
import in.ekstep.am.dto.credential.RegisterCredentialResult;

public class RegisterCredentialResponseBuilder extends ResponseBuilder<RegisterCredentialResponse> implements CredentialDetails {

  private String msgid;
  private String key = "";
  private String secret = "";
  private String username = "";
  private String token = "";

  public static RegisterCredentialResponseBuilder newInstance() {
    return new RegisterCredentialResponseBuilder();
  }

  public RegisterCredentialResponse build() {

    return new RegisterCredentialResponse(
        "ekstep.api.am.adminutil.credential.register",
        "1.0",
        System.currentTimeMillis(),
        success ? ResponseParams.successful(this.msgid) : ResponseParams.failed(this.msgid, err, errMsg),
        new RegisterCredentialResult(
            key,
            secret, token));
  }

  public RegisterCredentialResponseBuilder withMsgid(String msgid) {
    this.msgid = msgid;
    return this;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public RegisterCredentialResponseBuilder withUsername(String username) {
    this.username = username;
    return this;
  }

}

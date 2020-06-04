package in.ekstep.am.builder;

import in.ekstep.am.dto.ResponseParams;
import in.ekstep.am.dto.consumer.CreateConsumerResponse;
import in.ekstep.am.dto.consumer.CreateConsumerResult;

public class CreateConsumerResponseBuilder extends ResponseBuilder<CreateConsumerResponse> implements CredentialDetails {

  private String msgid;
  private String key = "";
  private String secret = "";
  private String username = "";

  public static CreateConsumerResponseBuilder newInstance() {
    return new CreateConsumerResponseBuilder();
  }

  public CreateConsumerResponse build() {

    return new CreateConsumerResponse(
        "ekstep.api.am.adminutil.consumer.create",
        "1.0",
        System.currentTimeMillis(),
        success ? ResponseParams.successful(this.msgid) : ResponseParams.failed(this.msgid, err, errMsg),
        new CreateConsumerResult(
            key,
            secret,
            this.username));
  }

  public CreateConsumerResponseBuilder withMsgid(String msgid) {
    this.msgid = msgid;
    return this;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public void setToken(String token) {

  }

  public CreateConsumerResponseBuilder withUsername(String username) {
    this.username = username;
    return this;
  }

}

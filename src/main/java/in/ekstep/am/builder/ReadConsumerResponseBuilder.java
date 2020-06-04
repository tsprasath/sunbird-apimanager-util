package in.ekstep.am.builder;

import in.ekstep.am.dto.ResponseParams;
import in.ekstep.am.dto.consumer.ReadConsumerResponse;
import in.ekstep.am.dto.consumer.ReadConsumerResult;

public class ReadConsumerResponseBuilder extends ResponseBuilder<ReadConsumerResponse> implements ConsumerDetails {
  private String[] groups = new String[]{};
  private String userName = "";
  private String msgId;

  public ReadConsumerResponse build() {
    return new ReadConsumerResponse("ekstep.api.am.adminutil.consumer.read",
        "1.0",
        System.currentTimeMillis(),
        success ? ResponseParams.successful(this.msgId) : ResponseParams.failed(this.msgId, err, errMsg),
        new ReadConsumerResult(
            userName,
            groups));
  }

  public ReadConsumerResponseBuilder withMsgid(String msgid) {
    this.msgId = msgid;
    return this;
  }

  @Override
  public void setGroups(String[] groups) {
    this.groups = groups;
  }

  public ReadConsumerResponseBuilder withUserName(String consumerName) {
    this.userName = consumerName;
    return this;
  }
}

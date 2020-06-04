package in.ekstep.am.builder;

import in.ekstep.am.dto.ResponseParams;
import in.ekstep.am.dto.group.GrantConsumerResponse;
import in.ekstep.am.dto.group.GrantConsumerResult;

public class GrantConsumerResponseBuilder extends ResponseBuilder<GrantConsumerResponse> implements ConsumerDetails {
  private String[] groups = new String[]{};
  private String userName = "";
  private String msgId;

  public GrantConsumerResponse build() {
    return new GrantConsumerResponse("ekstep.api.am.adminutil.consumer.grant",
        "1.0",
        System.currentTimeMillis(),
        success ? ResponseParams.successful(this.msgId) : ResponseParams.failed(this.msgId, err, errMsg),
        new GrantConsumerResult(
            userName,
            groups));
  }

  public GrantConsumerResponseBuilder withMsgid(String msgid) {
    this.msgId = msgid;
    return this;
  }

  public GrantConsumerResponseBuilder withGroups(String[] groups) {
    setGroups(groups);
    return this;
  }

  public GrantConsumerResponseBuilder withUserName(String consumerName) {
    this.userName = consumerName;
    return this;
  }

  @Override
  public void setGroups(String[] groups) {
    this.groups = groups;
  }


}

package in.ekstep.am.step;

import in.ekstep.am.builder.GrantConsumerResponseBuilder;
import in.ekstep.am.dto.group.GrantConsumerRequest;
import in.ekstep.am.external.AmAdminApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GrantConsumerStepChain {

  @Autowired
  @Qualifier("am.admin.api")
  private AmAdminApi amAdminApi;

  public void execute(String userName, GrantConsumerRequest request, GrantConsumerResponseBuilder responseBuilder) throws Exception {
    for (Step step : stepChain(request, userName, responseBuilder)) {
      if (responseBuilder.successful()) {
        step.execute();
      }
    }
  }


  private List<Step> stepChain(GrantConsumerRequest request, String userName, GrantConsumerResponseBuilder responseBuilder) {
    List<Step> stepChain = new ArrayList<>();
    stepChain.add(new DeleteConsumerToGroupStep(userName, amAdminApi));
    for (String group : request.groups())
      stepChain.add(new AssociateConsumerToGroupStep(userName, responseBuilder, amAdminApi, group));
    stepChain.add(new FetchConsumerGroupStep(userName, responseBuilder, amAdminApi));
    return stepChain;
  }
}

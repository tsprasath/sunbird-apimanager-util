package in.ekstep.am.step;

import in.ekstep.am.builder.ReadConsumerResponseBuilder;
import in.ekstep.am.external.AmAdminApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReadConsumerStepChain {

  @Autowired
  @Qualifier("am.admin.api")
  private AmAdminApi amAdminApi;

  public void execute(String userName, ReadConsumerResponseBuilder responseBuilder) throws Exception {
    for (Step step : stepChain(userName, responseBuilder)) {
      if (responseBuilder.successful()) {
        step.execute();
      }
    }
  }


  private List<Step> stepChain(String userName, ReadConsumerResponseBuilder responseBuilder) {
    List<Step> stepChain = new ArrayList<>();
    stepChain.add(new FetchConsumerGroupStep(userName, responseBuilder, amAdminApi));
    return stepChain;
  }
}

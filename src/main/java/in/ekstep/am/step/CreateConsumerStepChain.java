package in.ekstep.am.step;

import in.ekstep.am.builder.CreateConsumerResponseBuilder;
import in.ekstep.am.dto.consumer.CreateConsumerRequest;
import in.ekstep.am.external.AmAdminApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.asList;

@Component
public class CreateConsumerStepChain {

  @Autowired
  @Qualifier("am.admin.api")
  private AmAdminApi amAdminApi;

  @Autowired
  @Qualifier("default.consumer.group")
  private String defaultConsumerGroup;

  public void execute(CreateConsumerRequest request, CreateConsumerResponseBuilder responseBuilder) throws Exception {
    for (Step step : stepChain(request, responseBuilder)) {
      if (responseBuilder.successful()) {
        step.execute();
      }
    }
  }

  private List<Step> stepChain(CreateConsumerRequest request, CreateConsumerResponseBuilder responseBuilder) {
    String username = request.username();
    RegisterConsumerStep registerConsumerStep = new RegisterConsumerStep(username, responseBuilder, amAdminApi);
    AssociateConsumerToGroupStep associateConsumerToGroupStep = new AssociateConsumerToGroupStep(username, responseBuilder, amAdminApi, defaultConsumerGroup);
    CreateCredentialWithoutKeyStep createCredentialWithoutKeyStep = new CreateCredentialWithoutKeyStep(username, responseBuilder, amAdminApi, "");
    return asList(registerConsumerStep, associateConsumerToGroupStep, createCredentialWithoutKeyStep);
  }
}

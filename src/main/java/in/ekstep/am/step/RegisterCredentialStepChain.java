package in.ekstep.am.step;

import in.ekstep.am.builder.RegisterCredentialResponseBuilder;
import in.ekstep.am.dto.credential.RegisterCredentialRequest;
import in.ekstep.am.external.AmAdminApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.asList;

@Component
public class RegisterCredentialStepChain {

  @Autowired
  @Qualifier("am.admin.api")
  private AmAdminApi amAdminApi;

  public void execute(String userName, RegisterCredentialRequest request, RegisterCredentialResponseBuilder responseBuilder) throws Exception {
    for (Step step : stepChain(userName, request, responseBuilder)) {
      if (responseBuilder.successful()) {
        step.execute();
      }
    }
  }

  private List<Step> stepChain(String userName, RegisterCredentialRequest request, RegisterCredentialResponseBuilder responseBuilder) {
    CreateCredentialWithKeyStep createCredentialWithKeyStep = new CreateCredentialWithKeyStep(userName, responseBuilder, amAdminApi, request.key());
    return asList(createCredentialWithKeyStep);
  }
}

package in.ekstep.am.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.ekstep.am.builder.CredentialDetails;
import in.ekstep.am.external.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static java.text.MessageFormat.format;

public class CreateCredentialWithoutKeyStep implements Step {

  private final Logger log = LoggerFactory.getLogger(this.getClass());
  private AmAdminApi amAdminApi;
  private String userName;
  private CredentialDetails responseBuilder;
  private String key;

  CreateCredentialWithoutKeyStep(String userName, CredentialDetails responseBuilder, AmAdminApi api, String key) {
    this.userName = userName;
    this.responseBuilder = responseBuilder;
    amAdminApi = api;
    this.key = key;
  }

  @Override
  public void execute() throws Exception {
    AmResponse response = amAdminApi.getCredentials(userName);
    if (AmAdminApi.OK.equals(response.code())) {
      AmAdminApiGetCredentialsResponse getCredentialsResponse =
          new ObjectMapper().readValue(response.body(), AmAdminApiGetCredentialsResponse.class);
      GetCredentialsData credentialsData = filter(getCredentialsResponse.data());
      if (credentialsData != null) {
        updateResponseBuilder(credentialsData.key(), credentialsData.secret());
        return;
      }
    }
    if (AmAdminApi.NOT_FOUND.equals(response.code())) {
      log.error(format("CONSUMER DOES NOT EXISTS, USERNAME: {0}", userName));
      responseBuilder.markFailure("CREATE_CREDENTIAL_ERROR", "ERROR WHEN CREATING CREDENTIAL FOR CONSUMER, CONSUMER DOES NOT EXISTS.");
      return;
    }
    log.info(format("NO CREDENTIALS FOUND FOR CONSUMER. USERNAME: {0}", userName));
    createCredential();
  }

  private GetCredentialsData filter(List<GetCredentialsData> credentials) {
    if (credentials.size() != 0) {
      if (!shouldFilter()) {
        log.info(format("RETURNING EXISTING CREDENTIALS. USERName: {0}", userName));
        return credentials.get(0);
      }
      Optional<GetCredentialsData> requiredCredential = credentials.stream().filter(getCredentialsData -> key.equals(getCredentialsData.key())).findFirst();
      if (requiredCredential.isPresent()) {
        log.info(format("RETURNING EXISTING CREDENTIALS. USERName: {0}, KEY: {1}", userName, key));
        return requiredCredential.get();
      }
    }
    return null;
  }

  private void createCredential() throws Exception {
    log.info(format("CREATE NEW CREDENTIALS FOR CONSUMER. USERNAME: {0}", userName));
    AmResponse createCredentialResponse = amAdminApi.createCredential(userName, key);
    if (AmAdminApi.CREATED.equals(createCredentialResponse.code())) {
      AmAdminApiCreateCredentialResponse amCreateCredentialResponse =
          new ObjectMapper().readValue(createCredentialResponse.body(), AmAdminApiCreateCredentialResponse.class);
      updateResponseBuilder(amCreateCredentialResponse.key(), amCreateCredentialResponse.secret());
      log.info(format("CREATED CREDENTIAL. USERNAME: {0}", userName));
      return;
    }

    String errorDetail = AmAdminApi.CONFLICT.equals(createCredentialResponse.code())
        ? format(" KEY : {0}, already exists.", key)
        : "";

    log.error(format("UNABLE TO CREATE CREDENTIAL. USERNAME: {0}, AM RESPONSE CODE: {1}, AM RESPONSE: {2}",
        userName, createCredentialResponse.code(), createCredentialResponse.body()));
    responseBuilder.markFailure("CREATE_CREDENTIAL_ERROR", format("ERROR WHEN CREATING CREDENTIAL FOR CONSUMER.{0}", errorDetail));
  }

  private void updateResponseBuilder(String key, String secret) {
    responseBuilder.setKey(key);
    responseBuilder.setSecret(secret);
  }

  private boolean shouldFilter() {
    return !(key == null || key.isEmpty());
  }
}

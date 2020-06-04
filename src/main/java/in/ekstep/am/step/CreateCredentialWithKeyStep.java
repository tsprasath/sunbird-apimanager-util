package in.ekstep.am.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.ekstep.am.builder.CredentialDetails;
import in.ekstep.am.external.AmAdminApi;
import in.ekstep.am.external.AmAdminApiCreateCredentialResponse;
import in.ekstep.am.external.AmAdminApiGetCredentialResponse;
import in.ekstep.am.external.AmResponse;
import in.ekstep.am.jwt.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.text.MessageFormat.format;

public class CreateCredentialWithKeyStep implements Step {

  private final Logger log = LoggerFactory.getLogger(this.getClass());
  private AmAdminApi amAdminApi;
  private String userName;
  private CredentialDetails responseBuilder;
  private String key;

  CreateCredentialWithKeyStep(String userName, CredentialDetails responseBuilder, AmAdminApi api, String key) {
    this.userName = userName;
    this.responseBuilder = responseBuilder;
    amAdminApi = api;
    this.key = key;
  }

  @Override
  public void execute() throws Exception {
    AmResponse response = amAdminApi.getCredential(userName, key);
    if (AmAdminApi.OK.equals(response.code())) {
      AmAdminApiGetCredentialResponse getCredentialResponse =
          new ObjectMapper().readValue(response.body(), AmAdminApiGetCredentialResponse.class);
      updateResponseBuilder(getCredentialResponse.key(), getCredentialResponse.secret());
      return;
    }

    if (AmAdminApi.NOT_FOUND.equals(response.code())) {
      log.error(format("CREDENTIAL DOES NOT EXISTS, USERNAME: {0}", userName));
      createCredential();
    }
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
    responseBuilder.setToken(JWTUtil.createHS256Token(key, secret, null));
  }

  private boolean shouldFilter() {
    return !(key == null || key.isEmpty());
  }
}

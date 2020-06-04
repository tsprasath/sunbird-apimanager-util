package in.ekstep.am.external;

import in.ekstep.am.controller.HealthCheckController;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Optional;

import static java.text.MessageFormat.format;
import static okhttp3.MediaType.parse;

public class AmAdminApiClient implements AmAdminApi {

  private static final Logger log = LoggerFactory.getLogger(HealthCheckController.class);
  private static final MediaType FORM_MEDIA_TYPE =
      parse(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE);

  @Autowired
  @Qualifier("am.admin.api.client")
  private OkHttpClient amAdminApiClient;

  @Autowired
  @Qualifier("am.admin.api.endpoint")
  private String amAdminApiEndpoint;

  @Override
  public Response determineHealth() throws Exception {
    Request amAdminApiHealthRequest = new Request.Builder()
        .url(amAdminApiEndpoint)
        .get()
        .build();

    return amAdminApiClient.newCall(amAdminApiHealthRequest).execute();
  }

  @Override
  public AmResponse getConsumer(String username) throws Exception {
    Request request = new Request.Builder()
        .url(format("{0}/consumers/{1}/", amAdminApiEndpoint, username))
        .get()
        .build();

    return executeRequest(request);
  }

  @Override
  public AmResponse registerConsumer(String username) throws Exception {
    Request request = new Request.Builder()
        .url(amAdminApiEndpoint + "/consumers/")
        .post(FormBody.create(FORM_MEDIA_TYPE, format("username={0}", username)))
        .build();
    return executeRequest(request);
  }

  @Override
  public AmResponse associateConsumerToApiGroups(String username, String group) throws Exception {
    Request request = new Request.Builder()
        .url(format(amAdminApiEndpoint + "/consumers/{0}/acls/", username))
        .post(FormBody.create(FORM_MEDIA_TYPE, format("group={0}", group)))
        .build();

    return executeRequest(request);
  }

  @Override
  public AmResponse getConsumerGroups(String username) throws Exception {
    Request request = new Request.Builder()
        .url(format(amAdminApiEndpoint + "/consumers/{0}/acls/", username))
        .get()
        .build();

    return executeRequest(request);
  }

  @Override
  public AmResponse deleteConsumerGroup(String username, String aclId) throws Exception {

    Request request = new Request.Builder()
        .url(format(amAdminApiEndpoint + "/consumers/{0}/acls/{1}", username, aclId))
        .delete()
        .build();
    return executeRequest(request);
  }

  @Override
  public AmResponse getCredential(String username, String key) throws Exception {
    String url = format(amAdminApiEndpoint + "/consumers/{0}/jwt/{1}", username, key);
    Request request = new Request.Builder()
        .url(url)
        .get()
        .build();

    return executeRequest(request);
  }

  @Override
  public AmResponse getCredentials(String username) throws Exception {
    String url = format(amAdminApiEndpoint + "/consumers/{0}/jwt/", username);
    Request request = new Request.Builder()
        .url(url)
        .get()
        .build();

    return executeRequest(request);
  }

  @Override
  public AmResponse createCredential(String username, String key) throws Exception {
    boolean keyIsPresent = !(key == null || key.isEmpty());
    String credentialData = keyIsPresent
        ? format("key={0}", key)
        : "";
    Request request = new Request.Builder()
        .url(format(amAdminApiEndpoint + "/consumers/{0}/jwt/", username))
        .post(FormBody.create(FORM_MEDIA_TYPE, credentialData))
        .build();

    return executeRequest(request);
  }

  private AmResponse executeRequest(Request request) throws Exception {
    Response response = null;
    try {
      response = amAdminApiClient.newCall(request).execute();
      return new AmResponse(response.code(), response.body().string());
    } finally {
      //Response body must be closed for all requests
      Optional.ofNullable(response).ifPresent((res) -> res.body().close());
    }
  }
}

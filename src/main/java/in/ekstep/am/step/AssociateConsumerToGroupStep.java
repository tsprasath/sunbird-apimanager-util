package in.ekstep.am.step;

import in.ekstep.am.builder.ResponseBuilder;
import in.ekstep.am.external.AmAdminApi;
import in.ekstep.am.external.AmResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.text.MessageFormat.format;


public class AssociateConsumerToGroupStep implements Step {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private AmAdminApi amAdminApi;
  private String consumerGroup;
  private static final String CONSUMER_ALREADY_ASSOCIATED_TO_GROUP = "{\"group\":\"ACL group already exist for this consumer\"}\n";
  private String userName;
  private ResponseBuilder responseBuilder;

  AssociateConsumerToGroupStep(String userName, ResponseBuilder responseBuilder, AmAdminApi api, String consumerGroup) {
    this.userName = userName;
    this.responseBuilder = responseBuilder;
    this.amAdminApi = api;
    this.consumerGroup = consumerGroup;
  }

  @Override
  public void execute() throws Exception {

    AmResponse response = amAdminApi.associateConsumerToApiGroups(userName, consumerGroup);
    if (AmAdminApi.CREATED.equals(response.code())) {
      log.info(format("CONSUMER ASSIGNED TO GROUP. USERNAME: {0}, GROUP: {1}", userName, consumerGroup));
      return;
    }

    String responseBody = response.body();
    if (AmAdminApi.BAD_REQUEST.equals(response.code()) &&
        CONSUMER_ALREADY_ASSOCIATED_TO_GROUP.startsWith(responseBody)) {
      log.info(format("CONSUMER ALREADY ASSIGNED TO DEFAULT GROUP. USERNAME: {0}", userName));
      return;
    }

    log.error(format("UNABLE TO GRANT CONSUMER ACCESS TO DEFAULT GROUP. USERNAME: {0}, AM RESPONSE CODE: {1}, AM RESPONSE: {2}",
        userName, responseBody, response.code()));
    if (AmAdminApi.NOT_FOUND.equals(response.code())) {
      responseBuilder.markFailure("GROUP_ASSIGN_ERROR", format("ERROR WHEN GRANTING CONSUMER ACCESS TO API GROUP : {0}. CONSUMER DOES NOT EXISTS", consumerGroup));
      return;
    }
    responseBuilder.markFailure("GROUP_ASSIGN_ERROR", format("ERROR WHEN GRANTING CONSUMER ACCESS TO API GROUP:{0}", consumerGroup));
  }
}

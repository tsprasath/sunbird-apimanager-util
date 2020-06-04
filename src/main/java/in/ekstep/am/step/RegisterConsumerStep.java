package in.ekstep.am.step;

import in.ekstep.am.builder.ResponseBuilder;
import in.ekstep.am.external.AmAdminApi;
import in.ekstep.am.external.AmResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static in.ekstep.am.external.AmAdminApi.*;
import static java.text.MessageFormat.format;

public class RegisterConsumerStep implements Step {

  private final Logger log = LoggerFactory.getLogger(this.getClass());
  private AmAdminApi amAdminApi;
  private String userName;
  private ResponseBuilder responseBuilder;

  RegisterConsumerStep(String userName, ResponseBuilder responseBuilder, AmAdminApi amAdminApi) {
    this.userName = userName;
    this.responseBuilder = responseBuilder;
    this.amAdminApi = amAdminApi;
  }

  @Override
  public void execute() throws Exception {

    AmResponse response = amAdminApi.registerConsumer(userName);
    if (CREATED.equals(response.code())) {
      log.info(format("CONSUMER CREATED. USERNAME: {0}", userName));
      responseBuilder.markSuccess();
    } else if (CONFLICT.equals(response.code())) {
      log.info(format("CONSUMER ALREADY EXISTS. USERNAME: {0}", userName));
      AmResponse getConsumerResponse = amAdminApi.getConsumer(userName);
      if (OK.equals(getConsumerResponse.code())) {
        log.info(format("RETURNING EXISTING CONSUMER. USERNAME: {0}", userName));
        responseBuilder.markSuccess();
      } else {
        log.info(format("COULD NOT GET EXISTING CONSUMER. USERNAME: {0}. AM ADMIN API RESPONSE: {1}",
            userName, getConsumerResponse.body()));
        responseBuilder.markFailure("CONSUMER_GET_ERROR", "ERROR WHEN GETTING EXISTING CONSUMER");
      }
    } else {
      log.error(format
          ("UNKNOWN RESPONSE FROM AM ADMIN API WHEN CREATING CONSUMER. USERNAME: {0}. RESPONSE CODE: {1}, RESPONSE: {2}",
              userName, response.code(), response.body()));
      responseBuilder.markFailure("INTERNAL_ERROR", "UNKNOWN RESPONSE CODE FROM AM ADMIN API");
    }
  }
}

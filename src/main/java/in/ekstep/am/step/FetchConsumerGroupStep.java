package in.ekstep.am.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.ekstep.am.builder.ConsumerDetails;
import in.ekstep.am.external.AmAdminApi;
import in.ekstep.am.external.AmAdminApiGetGroupResponse;
import in.ekstep.am.external.AmResponse;
import in.ekstep.am.external.GetGroupData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.text.MessageFormat.format;


public class FetchConsumerGroupStep implements Step {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private AmAdminApi amAdminApi;
  private static final String CONSUMER_ALREADY_ASSOCIATED_TO_GROUP = "{\"group\":\"ACL group already exist for this consumer\"}\n";
  private String userName;
  private ConsumerDetails responseBuilder;

  FetchConsumerGroupStep(String userName, ConsumerDetails responseBuilder, AmAdminApi api) {
    this.userName = userName;
    this.responseBuilder = responseBuilder;
    this.amAdminApi = api;
  }

  @Override
  public void execute() throws Exception {

    AmResponse response = amAdminApi.getConsumerGroups(userName);
    if (AmAdminApi.OK.equals(response.code())) {
      log.info(format("GROUPS FETCHED. USERNAME: {0}", userName));
      AmAdminApiGetGroupResponse groupResponse = new ObjectMapper().readValue(response.body(), AmAdminApiGetGroupResponse.class);
      List<String> groups = new ArrayList<>();
      if (groupResponse.data().size() > 0) {
        groups = groupResponse
            .data()
            .stream()
            .map(GetGroupData::groups)
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList());
        log.info(format("FOR USERNAME:{0}, GROUPS:{1}", userName, String.join(",", groups)));
      }
      log.info("NO GROUPS FOUND FOR USERNAME:{0}", userName);
      responseBuilder.setGroups(groups.toArray(new String[groups.size()]));
      return;
    }
    responseBuilder.setGroups(new String[]{});
    if (AmAdminApi.NOT_FOUND.equals(response.code())) {
      responseBuilder.markFailure("GROUP_FETCH_ERROR", format("ERROR WHEN FETCHING GROUPS OF CONSUMER, USERNAME: {0}. CONSUMER DOES NOT EXISTS", userName));
      return;
    }
    responseBuilder.markFailure("GROUP_FETCH_ERROR", format("ERROR WHEN FETCHING GROUPS OF CONSUMER, USERNAME: {0}", userName));
  }
}

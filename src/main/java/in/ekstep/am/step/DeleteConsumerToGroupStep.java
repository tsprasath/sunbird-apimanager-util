package in.ekstep.am.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.ekstep.am.external.AmAdminApi;
import in.ekstep.am.external.AmAdminApiGetGroupResponse;
import in.ekstep.am.external.AmResponse;
import in.ekstep.am.external.GetGroupData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.text.MessageFormat.format;


public class DeleteConsumerToGroupStep implements Step {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private AmAdminApi amAdminApi;
  private String userName;

  DeleteConsumerToGroupStep(String userName, AmAdminApi api) {
    this.userName = userName;
    this.amAdminApi = api;
  }

  @Override
  public void execute() throws Exception {

    AmResponse getGroupResponse = amAdminApi.getConsumerGroups(userName);
    if (AmAdminApi.OK.equals(getGroupResponse.code())) {
      log.info(format("GROUPS FETCHED. USERNAME: {0}", userName));
      AmAdminApiGetGroupResponse groupResponse = new ObjectMapper().readValue(getGroupResponse.body(), AmAdminApiGetGroupResponse.class);
      deleteGroups(groupResponse);
      return;
    }
    log.info("NOT OK RESPONSE WHEN FETCHING GROUPS FOR USERNAME:{0}", userName);
  }

  private void deleteGroups(AmAdminApiGetGroupResponse groupResponse) throws Exception {
    List<GetGroupData> groupsData = groupResponse.data();
    if (groupsData.size() > 0) {
      for (GetGroupData groupData : groupsData) {
        AmResponse deleteResponse = amAdminApi.deleteConsumerGroup(userName, groupData.id());
        log(deleteResponse, groupData);
      }
      return;
    }
    log.info("NO GROUPS FOUND FOR USERNAME:{0}", userName);
  }

  private void log(AmResponse deleteResponse, GetGroupData groupData) {
    if (AmAdminApi.DELETED.equals(deleteResponse.code())) {
      log.info(format("GROUP(s):{0} SUCCESSFULLY DELETED fOR CONSUMER:{1}", groupData.groupString(), userName));
    }

    if (AmAdminApi.BAD_REQUEST.equals(deleteResponse.code())) {
      log.info(format("UNABLE TO DELETE GROUP(s):{0} FOR USERNAME: {1}", groupData.groupString(), userName));
    }

    if (AmAdminApi.NOT_FOUND.equals(deleteResponse.code())) {
      log.info(format("GROUP(s):{0} NOT FOUND, WHEN DELETING FOR USERNAME: {1}", groupData.groupString(), userName));
    }

    log.info(format("UNKNOWN RESPONSE TYPE: {0}. WHEN DELETING GROUP(s): {1} FOR USERNAME: {2}", String.valueOf(deleteResponse.code()), groupData.groupString(), userName));
  }

}

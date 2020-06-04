package in.ekstep.am.controller;

import com.codahale.metrics.annotation.Timed;
import in.ekstep.am.dto.ResponseParams;
import in.ekstep.am.dto.health.HealthCheckResponse;
import in.ekstep.am.dto.health.HealthCheckResponseResult;
import in.ekstep.am.dto.health.HealthCheckResult;
import in.ekstep.am.external.AmAdminApi;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;

@RestController
public class HealthCheckController {

  private static final Logger log = LoggerFactory.getLogger(HealthCheckController.class);
  public static final Integer SUCCESS_RESPONSE_CODE = 200;

  @Autowired
  @Qualifier("am.admin.api")
  private AmAdminApi amAdminApi;

  @Timed(name = "health-api")
  @RequestMapping("/health")
  public HealthCheckResponse determineHealth() {
    Response response = null;
    try {
      response = amAdminApi.determineHealth();
      if (SUCCESS_RESPONSE_CODE.equals(response.code())) {
        log.info("API HEALTHY");
        return buildHealthCheckResponse(true, true, null, null);
      }
      log.info("AM ADMIN API UNHEALTHY");
      return buildHealthCheckResponse(
              false, false, "AM_ADMIN_API_ERROR",
              format("RESPONSE CODE: {0}, BODY: {1}", response.code(), response.body().string()));
    } catch (Exception e) {
      log.error("ERROR WHEN DETERMINING HEALTH", e);
      return buildHealthCheckResponse(
              false, false, "INTERNAL_ERROR",
              format("ERROR WHEN DETERMINING HEALTH. Msg: {0}", e.getMessage()));
    } finally {
      Optional.ofNullable(response).ifPresent((res) -> res.body().close());
    }
  }

  private HealthCheckResponse buildHealthCheckResponse(boolean apiHealthy, boolean adminApiHealthy, String err, String errmsg) {
    return new HealthCheckResponse(
            System.currentTimeMillis(),
            ResponseParams.successful(UUID.randomUUID().toString()),
            new HealthCheckResponseResult(
                    apiHealthy,
                    asList(new HealthCheckResult("AM Admin API", adminApiHealthy, err, errmsg))));
  }

  @Timed(name = "health-api")
  @RequestMapping("/service/health")
  public HealthCheckResponse serviceHealth() {
    return new HealthCheckResponse(
            System.currentTimeMillis(),
            ResponseParams.successful(UUID.randomUUID().toString()),
            new HealthCheckResponseResult(
                    true,
                    asList(new HealthCheckResult("Admin Util", true, null, null))));
  }

}

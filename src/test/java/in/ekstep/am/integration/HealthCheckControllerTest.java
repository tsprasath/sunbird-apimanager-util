package in.ekstep.am.integration;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public class HealthCheckControllerTest {

  @LocalServerPort
  private int serverPort;

  @Rule
  public WireMockRule amAdminApi = new WireMockRule(3010);

  @Before
  public void setUp() throws Exception {
    amAdminApi.resetAll();
  }

  @Test
  public void healthCheckShouldPassWhenAmAdminApiIsReachable() throws Exception {

    amAdminApi.stubFor(get(urlEqualTo("/"))
        .willReturn(aResponse()
            .withStatus(200)));

    given()
        .port(serverPort)
        .get("/health")

        .then()
        .body("id", is("ekstep.am.adminutil.health"))
        .body("ver", is("1.0"))
        .body("ets", is(greaterThan(0L)))
        .body("params.status", is("successful"))
        .body("result.name", is("am.adminutil"))
        .body("result.healthy", is(true))
        .body("result.checks.any { check -> check.name == 'AM Admin API' }", is(true))
        .body("result.checks.find { check -> check.name == 'AM Admin API' }.healthy", equalTo(true))
        .body("result.checks.find { check -> check.name == 'AM Admin API' }.err", is(nullValue()))
        .body("result.checks.find { check -> check.name == 'AM Admin API' }.errmsg", is(nullValue()));
  }

  @Test
  public void healthCheckShouldFailWhenAmAdminApiReturnsError() throws Exception {

    amAdminApi.stubFor(get(urlEqualTo("/"))
        .willReturn(aResponse()
            .withStatus(500)
            .withBody("{'message':'Error'}")));

    given()

        .port(serverPort)
        .get("/health")

        .then()
        .body("params.status", is("successful"))
        .body("result.healthy", is(false))
        .body("result.checks.any { check -> check.name == 'AM Admin API' }", is(true))
        .body("result.checks.find { check -> check.name == 'AM Admin API' }.healthy", equalTo(false))
        .body("result.checks.find { check -> check.name == 'AM Admin API' }.err", is("AM_ADMIN_API_ERROR"))
        .body("result.checks.find { check -> check.name == 'AM Admin API' }.errmsg", is("RESPONSE CODE: 500, BODY: {'message':'Error'}"));
  }

  @Test
  public void healthCheckShouldFailWhenAMAdminApiIsNotReachable() throws Exception {

    amAdminApi.stop();

    given()

        .port(serverPort)
        .get("/health")

        .then()
        .body("params.status", is("successful"))
        .body("result.healthy", is(false))
        .body("result.checks.any { check -> check.name == 'AM Admin API' }", is(true))
        .body("result.checks.find { check -> check.name == 'AM Admin API' }.healthy", equalTo(false))
        .body("result.checks.find { check -> check.name == 'AM Admin API' }.err", is("INTERNAL_ERROR"))
        .body("result.checks.find { check -> check.name == 'AM Admin API' }.errmsg", startsWith("ERROR WHEN DETERMINING HEALTH. Msg: Failed to connect to localhost"));
  }
}

package in.ekstep.am.integration;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import in.ekstep.am.builders.CreateConsumerRequestBuilder;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public class CreateConsumerBadRequestTest {

  @LocalServerPort
  private int serverPort;

  @Rule
  public WireMockRule amAdminApi = new WireMockRule(3010);

  @Before
  public void setUp() throws Exception {
    amAdminApi.resetAll();
  }

  @Test
  public void shouldReturnBadRequestResponseWhenRequestBodyIsEmpty() throws Exception {

    given()
        .port(serverPort)
        .body("")
        .contentType(ContentType.JSON)
        .post("/v1/consumer/create")
        .then()
        .statusCode(400);
  }

  @Test
  public void shouldReturnBadRequestResponseWhenRequestIsEmpty() throws Exception {

    given()
        .port(serverPort)
        .body(new CreateConsumerRequestBuilder().withParams(null).withNullRequest().build())
        .contentType(ContentType.JSON)
        .post("/v1/consumer/create")
        .then()
        .body("params.status", is("failed"))
        .body("params.err", is("BAD_REQUEST"))
        .body("params.errmsg", is("REQUEST IS MANDATORY"))
        .body("result.username", is(not(nullValue())))
        .body("result.key", is(not(nullValue()))) //Currently mocked
        .body("result.secret", is(not(nullValue()))); //Currently mocked
  }

  @Test
  public void shouldReturnBadRequestResponseWhenUsernameIsEmpty() throws Exception {

    given()
        .port(serverPort)
        .body(new CreateConsumerRequestBuilder().withParams(null).withUsername("").build())
        .contentType(ContentType.JSON)
        .post("/v1/consumer/create")
        .then()
        .body("params.status", is("failed"))
        .body("params.err", is("BAD_REQUEST"))
        .body("params.errmsg", is("USERNAME IS MANDATORY"))
        .body("result.username", is(not(nullValue())))
        .body("result.key", is(not(nullValue()))) //Currently mocked
        .body("result.secret", is(not(nullValue()))); //Currently mocked
  }

}

package in.ekstep.am.integration;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import in.ekstep.am.builders.RegisterCredentialRequestBuilder;
import io.restassured.http.ContentType;
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
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public class RegisterCredentialTest {

  @LocalServerPort
  private int serverPort;

  @Rule
  public WireMockRule amAdminApi = new WireMockRule(3010);

  @Before
  public void setUp() throws Exception {
    amAdminApi.resetAll();
  }

  @Test
  public void shouldRegisterNewCredentialWhenNoCredentialExistsForConsumer() throws Exception {

    amAdminApi.stubFor(get(urlEqualTo("/consumers/Purandara/jwt/a36c3049b36249a3c9f8891cb127243c"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(404)
            .withBody("{\"message\":\"Not found\"}")));
    amAdminApi.stubFor(post(urlEqualTo("/consumers/Purandara/jwt/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(201)
            .withBody("{\"consumer_id\":\"7bce93e1-0a90-489c-c887-d385545f8f4b\",\"created_at\":1442426001000,\"id\":\"bcbfb45d-e391-42bf-c2ed-94e32946753a\",\"key\":\"a36c3049b36249a3c9f8891cb127243c\",\"secret\":\"e71829c351aa4242c2719cbfbe671c09\"}")));
    given()
        .port(serverPort)
        .body(new RegisterCredentialRequestBuilder().withKey("a36c3049b36249a3c9f8891cb127243c").build())
        .contentType(ContentType.JSON)
        .post("/v1/consumer/Purandara/credential/register")
        .then()
        .statusCode(200)
        .body("id", is("ekstep.api.am.adminutil.consumer.create"))
        .body("ver", is("1.0"))
        .body("ets", is(greaterThan(0L)))
        .body("params.status", is("successful"))
        .body("params.err", is(nullValue()))
        .body("params.errmsg", is(nullValue()))
        .body("params.resmsgid", is(not(nullValue())))
        .body("result.key", is("a36c3049b36249a3c9f8891cb127243c"))
        .body("result.secret", is("e71829c351aa4242c2719cbfbe671c09"));

    amAdminApi.verify(0, getRequestedFor(urlEqualTo("/consumers/Purandara/")));
    amAdminApi.verify(postRequestedFor(urlEqualTo("/consumers/Purandara/jwt/")));
  }

  @Test
  public void shouldReturnExistingCredentialIfRequestIsMadeWithSameKey() throws Exception {
    amAdminApi.stubFor(get(urlEqualTo("/consumers/Purandara/jwt/a36c3049b36249a3c9f8891cb127243c"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(200)
            .withBody("{\"secret\":\"842fd381e344494db2ed70d235881d3f\",\"id\":\"7588c6f1-e9c9-43f5-aee8-6cde8d297b3d\",\"created_at\":1500958174000,\"key\":\"a36c3049b36249a3c9f8891cb127243c\",\"algorithm\":\"HS256\",\"consumer_id\":\"c46a07b3-fb77-41db-a6ff-0680f40bb563\"}")));

    given()
        .port(serverPort)
        .body(new RegisterCredentialRequestBuilder().withKey("a36c3049b36249a3c9f8891cb127243c").build())
        .contentType(ContentType.JSON)
        .post("/v1/consumer/Purandara/credential/register")
        .then()
        .statusCode(200)
        .body("params.status", is("successful"))
        .body("result.key", is("a36c3049b36249a3c9f8891cb127243c"))
        .body("result.secret", is("842fd381e344494db2ed70d235881d3f"));

    amAdminApi.verify(getRequestedFor(urlEqualTo("/consumers/Purandara/jwt/a36c3049b36249a3c9f8891cb127243c")));
  }

  @Test
  public void shouldRegisterNewCredentialWhenNoneExistsWithGivenKey() throws Exception {

    amAdminApi.stubFor(get(urlEqualTo("/consumers/Purandara/jwt/a36c3049b36249a3c9f8891cb127243c"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(404)
            .withBody("{\"message\":\"Not found\"}")));
    amAdminApi.stubFor(post(urlEqualTo("/consumers/Purandara/jwt/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(201)
            .withBody("{\"consumer_id\":\"7bce93e1-0a90-489c-c887-d385545f8f4b\",\"created_at\":1442426001000,\"id\":\"bcbfb45d-e391-42bf-c2ed-94e32946753a\",\"key\":\"a36c3049b36249a3c9f8891cb127243c\",\"secret\":\"e71829c351aa4242c2719cbfbe671c09\"}")));

    given()
        .port(serverPort)
        .body(new RegisterCredentialRequestBuilder().withKey("a36c3049b36249a3c9f8891cb127243c").build())
        .contentType(ContentType.JSON)
        .post("/v1/consumer/Purandara/credential/register")
        .then()
        .statusCode(200)
        .body("id", is("ekstep.api.am.adminutil.consumer.create"))
        .body("ver", is("1.0"))
        .body("ets", is(greaterThan(0L)))
        .body("params.status", is("successful"))
        .body("params.err", is(nullValue()))
        .body("params.errmsg", is(nullValue()))
        .body("params.resmsgid", is(not(nullValue())))
        .body("result.key", is("a36c3049b36249a3c9f8891cb127243c"))
        .body("result.secret", is("e71829c351aa4242c2719cbfbe671c09"));

    amAdminApi.verify(0, getRequestedFor(urlEqualTo("/consumers/Purandara/")));
    amAdminApi.verify(postRequestedFor(urlEqualTo("/consumers/Purandara/jwt/")));
  }

  @Test
  public void shouldReturnErrorWhenUnableToCreateCredentials() throws Exception {

    amAdminApi.stubFor(post(urlEqualTo("/consumers/Purandara/jwt/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(400)
            .withBody("{\"message\":\"Not found\"}")));
    amAdminApi.stubFor(get(urlEqualTo("/consumers/Purandara/jwt/a36c3049b36249a3c9f8891cb127243c"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(404)
            .withBody("{\"message\":\"Not found\"}")));

    given()
        .port(serverPort)
        .body(new RegisterCredentialRequestBuilder().withKey("a36c3049b36249a3c9f8891cb127243c").build())
        .contentType(ContentType.JSON)
        .post("/v1/consumer/Purandara/credential/register")
        .then()
        .statusCode(400)
        .body("params.status", is("failed"))
        .body("params.err", is("CREATE_CREDENTIAL_ERROR"))
        .body("params.errmsg", is("ERROR WHEN CREATING CREDENTIAL FOR CONSUMER."));
  }

  @Test
  public void shouldReturnErrorWhenConsumerDoesNotExists() throws Exception {

    amAdminApi.stubFor(get(urlEqualTo("/consumers/Purandara/jwt/a36c3049b36249a3c9f8891cb127243c"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(404)
            .withBody("{\"message\":\"Not found\"}")));
    amAdminApi.stubFor(post(urlEqualTo("/consumers/Purandara/jwt/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(400)
            .withBody("{\"message\":\"Not found\"}")));

    given()
        .port(serverPort)
        .body(new RegisterCredentialRequestBuilder().withKey("a36c3049b36249a3c9f8891cb127243c").build())
        .contentType(ContentType.JSON)
        .post("/v1/consumer/Purandara/credential/register")
        .then()
        .statusCode(400)
        .body("params.status", is("failed"))
        .body("params.err", is("CREATE_CREDENTIAL_ERROR"))
        .body("params.errmsg", is("ERROR WHEN CREATING CREDENTIAL FOR CONSUMER."));
  }

  @Test
  public void shouldReturnErrorWhenKeyAlreadyExists() throws Exception {

    amAdminApi.stubFor(get(urlEqualTo("/consumers/Purandara/jwt/a36c3049b36249a3c9f8891cb127243c"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(404)
            .withBody("{\"message\":\"Not found\"}")));
    amAdminApi.stubFor(post(urlEqualTo("/consumers/Purandara/jwt/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(409)
            .withBody("{\"key\":\"already exists with value 'a36c3049b36249a3c9f8891cb127243c'\"}")));

    given()
        .port(serverPort)
        .body(new RegisterCredentialRequestBuilder().withKey("a36c3049b36249a3c9f8891cb127243c").build())
        .contentType(ContentType.JSON)
        .post("/v1/consumer/Purandara/credential/register")
        .then()
        .statusCode(400)
        .body("params.status", is("failed"))
        .body("params.err", is("CREATE_CREDENTIAL_ERROR"))
        .body("params.errmsg", is("ERROR WHEN CREATING CREDENTIAL FOR CONSUMER. KEY : a36c3049b36249a3c9f8891cb127243c, already exists."));
  }
}

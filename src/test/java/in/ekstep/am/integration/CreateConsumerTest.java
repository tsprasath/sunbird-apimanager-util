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

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public class CreateConsumerTest {

  @LocalServerPort
  private int serverPort;

  @Rule
  public WireMockRule amAdminApi = new WireMockRule(3010);

  @Before
  public void setUp() throws Exception {
    amAdminApi.resetAll();
  }

  @Test
  public void shouldRegisterNewConsumerWhenNoneExistsWithGivenID() throws Exception {

    amAdminApi.stubFor(post(urlEqualTo("/consumers/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(201)
            .withBody("{\"username\":\"Purandara\",\"created_at\":1428555626000,\"id\":\"bbdf1c48-19dc-4ab7-cae0-ff4f59d87dc9\"}")));
    amAdminApi.stubFor(post(urlEqualTo("/consumers/Purandara/acls/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(201)
            .withBody("{\"group\":\"defaultGroup\",\"consumer_id\":\"bbdf1c48-19dc-4ab7-cae0-ff4f59d87dc9\",\"created_at\":1428555626000,\"id\":\"784bc8be-9f59-44b3-8ccb-545d16651f46\"}")));
    amAdminApi.stubFor(post(urlEqualTo("/consumers/Purandara/jwt/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(201)
            .withBody("{\"consumer_id\":\"7bce93e1-0a90-489c-c887-d385545f8f4b\",\"created_at\":1442426001000,\"id\":\"bcbfb45d-e391-42bf-c2ed-94e32946753a\",\"key\":\"a36c3049b36249a3c9f8891cb127243c\",\"secret\":\"e71829c351aa4242c2719cbfbe671c09\"}")));
    amAdminApi.stubFor(get(urlEqualTo("/consumers/Purandara/jwt/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(200)
            .withBody("{\"data\":[],\"total\":0}")));

    given()
        .port(serverPort)
        .body(new CreateConsumerRequestBuilder().build())
        .contentType(ContentType.JSON)
        .post("/v1/consumer/create")
        .then()
        .body("id", is("ekstep.api.am.adminutil.consumer.create"))
        .body("ver", is("1.0"))
        .body("ets", is(greaterThan(0L)))
        .body("params.status", is("successful"))
        .body("params.err", is(nullValue()))
        .body("params.errmsg", is(nullValue()))
        .body("params.resmsgid", is(not(nullValue())))
        .body("result.username", is("Purandara"))
        .body("result.key", is("a36c3049b36249a3c9f8891cb127243c"))
        .body("result.secret", is("e71829c351aa4242c2719cbfbe671c09"));

    amAdminApi.verify(0, getRequestedFor(urlEqualTo("/consumers/Purandara/")));
    amAdminApi.verify(postRequestedFor(urlEqualTo("/consumers/"))
        .withRequestBody(equalTo("username=Purandara")));
    amAdminApi.verify(postRequestedFor(urlEqualTo("/consumers/Purandara/jwt/")));
  }

  @Test
  public void shouldReturnExistingConsumerIfRequestIsMadeWithSameId() throws Exception {

    amAdminApi.stubFor(post(urlEqualTo("/consumers/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(409)
            .withBody("{\"username\":\"already exists with value 'Purandara'\"}")));
    amAdminApi.stubFor(get(urlEqualTo("/consumers/Purandara/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(200)
            .withBody("{\"username\":\"Purandara\",\"created_at\":1490163913000,\"id\":\"09c89737-a9a0-4aeb-add3-c9fc16d93713\"}")));
    amAdminApi.stubFor(post(urlEqualTo("/consumers/Purandara/acls/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(201)
            .withBody("{\"group\":\"defaultGroup\",\"consumer_id\":\"bbdf1c48-19dc-4ab7-cae0-ff4f59d87dc9\",\"created_at\":1428555626000,\"id\":\"784bc8be-9f59-44b3-8ccb-545d16651f46\"}")));
    amAdminApi.stubFor(get(urlEqualTo("/consumers/Purandara/jwt/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(200)
            .withBody("{\"data\":[{\"secret\":\"842fd381e344494db2ed70d235881d3f\",\"id\":\"d6ae0661-d253-49fa-bac7-3db7e9c2562e\",\"created_at\":1490596318000,\"key\":\"de3da55b284241f398aec95c3f14030c\",\"algorithm\":\"HS256\",\"consumer_id\":\"aeb0b82b-f957-4c8e-bb10-5f5a336e6f59\"},{\"secret\":\"ce2e1b07a6e14d38afd59384534e0bbc\",\"id\":\"2b2b3364-d947-41d5-a586-af8d9a218c50\",\"created_at\":1490596323000,\"key\":\"d6938f065d3b435085296e7fed1ff92e\",\"algorithm\":\"HS256\",\"consumer_id\":\"aeb0b82b-f957-4c8e-bb10-5f5a336e6f59\"}],\"total\":2}")));

    given()
        .port(serverPort)
        .body(new CreateConsumerRequestBuilder().build())
        .contentType(ContentType.JSON)
        .post("/v1/consumer/create")
        .then()
        .body("params.status", is("successful"))
        .body("result.username", is("Purandara"))
        .body("result.key", is("de3da55b284241f398aec95c3f14030c"))
        .body("result.secret", is("842fd381e344494db2ed70d235881d3f"));

    amAdminApi.verify(postRequestedFor(urlEqualTo("/consumers/"))
        .withRequestBody(equalTo("username=Purandara")));
    amAdminApi.verify(getRequestedFor(urlEqualTo("/consumers/Purandara/")));
  }

  @Test
  public void shouldReturnErrorResponseWhenAmReturnsErrorWhenRegisteringConsumer() throws Exception {

    amAdminApi.stubFor(post(urlEqualTo("/consumers/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(500)
            .withBody("{\"message\":\"AM internal error\"}")));

    given()
        .port(serverPort)
        .body(new CreateConsumerRequestBuilder().build())
        .contentType(ContentType.JSON)
        .post("/v1/consumer/create")
        .then()
        .body("params.status", is("failed"))
        .body("params.err", is("INTERNAL_ERROR"))
        .body("params.errmsg", is("UNKNOWN RESPONSE CODE FROM AM ADMIN API"))
        .body("result.username", is("Purandara"))
        .body("result.key", is(not(nullValue())))
        .body("result.secret", is(not(nullValue())));
  }

  @Test
  public void shouldReturnErrorResponseWhenAmReturnsErrorWhenGettingExistingConsumer() throws Exception {

    amAdminApi.stubFor(post(urlEqualTo("/consumers/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(409)
            .withBody("{\"username\":\"already exists with value 'Purandara'\"}")));
    amAdminApi.stubFor(get(urlEqualTo("/consumers/Purandara/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(500)
            .withBody("{\"message\":\"AM internal error\"}")));

    given()
        .port(serverPort)
        .body(new CreateConsumerRequestBuilder().build())
        .contentType(ContentType.JSON)
        .post("/v1/consumer/create")
        .then()
        .body("params.status", is("failed"))
        .body("params.err", is("CONSUMER_GET_ERROR"))
        .body("params.errmsg", is("ERROR WHEN GETTING EXISTING CONSUMER"))
        .body("result.username", is("Purandara"));
  }

  @Test
  public void shouldReturnErrorWhenUnableToCreateCredentials() throws Exception {

    amAdminApi.stubFor(post(urlEqualTo("/consumers/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(201)
            .withBody("{\"username\":\"Purandara\",\"created_at\":1428555626000,\"id\":\"bbdf1c48-19dc-4ab7-cae0-ff4f59d87dc9\"}")));
    amAdminApi.stubFor(post(urlEqualTo("/consumers/Purandara/acls/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(201)
            .withBody("{\"group\":\"defaultGroup\",\"consumer_id\":\"bbdf1c48-19dc-4ab7-cae0-ff4f59d87dc9\",\"created_at\":1428555626000,\"id\":\"784bc8be-9f59-44b3-8ccb-545d16651f46\"}")));
    amAdminApi.stubFor(post(urlEqualTo("/consumers/Purandara/jwt/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(400)
            .withBody("{\"message\":\"Not found\"}")));
    amAdminApi.stubFor(get(urlEqualTo("/consumers/Purandara/jwt/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(200)
            .withBody("{\"data\":[],\"total\":0}")));

    given()
        .port(serverPort)
        .body(new CreateConsumerRequestBuilder().build())
        .contentType(ContentType.JSON)
        .post("/v1/consumer/create")
        .then()
        .body("params.status", is("failed"))
        .body("params.err", is("CREATE_CREDENTIAL_ERROR"))
        .body("params.errmsg", is("ERROR WHEN CREATING CREDENTIAL FOR CONSUMER."));
  }

  @Test
  public void shouldRegisterNewConsumerWithAccessToDefaultApiGroup() throws Exception {

    amAdminApi.stubFor(post(urlEqualTo("/consumers/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(201)
            .withBody("{\"username\":\"Purandara\",\"created_at\":1428555626000,\"id\":\"bbdf1c48-19dc-4ab7-cae0-ff4f59d87dc9\"}")));
    amAdminApi.stubFor(post(urlEqualTo("/consumers/Purandara/acls/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(201)
            .withBody("{\"group\":\"defaultGroup\",\"consumer_id\":\"bbdf1c48-19dc-4ab7-cae0-ff4f59d87dc9\",\"created_at\":1428555626000,\"id\":\"784bc8be-9f59-44b3-8ccb-545d16651f46\"}")));
    amAdminApi.stubFor(get(urlEqualTo("/consumers/Purandara/jwt/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(200)
            .withBody("{\"data\":[{\"secret\":\"842fd381e344494db2ed70d235881d3f\",\"id\":\"d6ae0661-d253-49fa-bac7-3db7e9c2562e\",\"created_at\":1490596318000,\"key\":\"de3da55b284241f398aec95c3f14030c\",\"algorithm\":\"HS256\",\"consumer_id\":\"aeb0b82b-f957-4c8e-bb10-5f5a336e6f59\"},{\"secret\":\"ce2e1b07a6e14d38afd59384534e0bbc\",\"id\":\"2b2b3364-d947-41d5-a586-af8d9a218c50\",\"created_at\":1490596323000,\"key\":\"d6938f065d3b435085296e7fed1ff92e\",\"algorithm\":\"HS256\",\"consumer_id\":\"aeb0b82b-f957-4c8e-bb10-5f5a336e6f59\"}],\"total\":2}")));

    given()
        .port(serverPort)
        .body(new CreateConsumerRequestBuilder().build())
        .contentType(ContentType.JSON)
        .post("/v1/consumer/create")
        .then()
        .body("params.status", is("successful"))
        .body("result.username", is("Purandara"));

    amAdminApi.verify(postRequestedFor(urlEqualTo("/consumers/Purandara/acls/"))
        .withRequestBody(equalTo("group=contentUser")));
  }

  @Test
  public void shouldNotFailIfConsumerAlreadyHasAccessToDefaultApiGroup() throws Exception {

    amAdminApi.stubFor(post(urlEqualTo("/consumers/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(201)
            .withBody("{\"username\":\"Purandara\",\"created_at\":1428555626000,\"id\":\"bbdf1c48-19dc-4ab7-cae0-ff4f59d87dc9\"}")));
    amAdminApi.stubFor(post(urlEqualTo("/consumers/Purandara/acls/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(400)
            .withBody("{\"group\":\"ACL group already exist for this consumer\"}\n")));
    amAdminApi.stubFor(get(urlEqualTo("/consumers/Purandara/jwt/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(200)
            .withBody("{\"data\":[{\"secret\":\"842fd381e344494db2ed70d235881d3f\",\"id\":\"d6ae0661-d253-49fa-bac7-3db7e9c2562e\",\"created_at\":1490596318000,\"key\":\"de3da55b284241f398aec95c3f14030c\",\"algorithm\":\"HS256\",\"consumer_id\":\"aeb0b82b-f957-4c8e-bb10-5f5a336e6f59\"},{\"secret\":\"ce2e1b07a6e14d38afd59384534e0bbc\",\"id\":\"2b2b3364-d947-41d5-a586-af8d9a218c50\",\"created_at\":1490596323000,\"key\":\"d6938f065d3b435085296e7fed1ff92e\",\"algorithm\":\"HS256\",\"consumer_id\":\"aeb0b82b-f957-4c8e-bb10-5f5a336e6f59\"}],\"total\":2}")));

    given()
        .port(serverPort)
        .body(new CreateConsumerRequestBuilder().build())
        .contentType(ContentType.JSON)
        .post("/v1/consumer/create")
        .then()
        .body("params.status", is("successful"))
        .body("result.username", is("Purandara"));
  }

  @Test
  public void shouldReturnFailedResponseWhenConsumerCannotBeGrantedAccessToDefaultApiGroup() throws Exception {

    amAdminApi.stubFor(post(urlEqualTo("/consumers/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(201)
            .withBody("{\"username\":\"Purandara\",\"created_at\":1428555626000,\"id\":\"bbdf1c48-19dc-4ab7-cae0-ff4f59d87dc9\"}")));
    amAdminApi.stubFor(post(urlEqualTo("/consumers/Purandara/acls/"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withStatus(400)
            .withBody("{\"group\":\"group is required\"}")));

    given()
        .port(serverPort)
        .body(new CreateConsumerRequestBuilder().build())
        .contentType(ContentType.JSON)
        .post("/v1/consumer/create")
        .then()
        .body("params.status", is("failed"))
        .body("params.err", is("GROUP_ASSIGN_ERROR"))
        .body("params.errmsg", is("ERROR WHEN GRANTING CONSUMER ACCESS TO API GROUP:contentUser"))
        .body("result.username", is("Purandara"));
  }
}

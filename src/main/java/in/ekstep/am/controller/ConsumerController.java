package in.ekstep.am.controller;

import com.codahale.metrics.annotation.Timed;
import in.ekstep.am.builder.CreateConsumerResponseBuilder;
import in.ekstep.am.builder.GrantConsumerResponseBuilder;
import in.ekstep.am.builder.ReadConsumerResponseBuilder;
import in.ekstep.am.builder.RegisterCredentialResponseBuilder;
import in.ekstep.am.dto.consumer.CreateConsumerRequest;
import in.ekstep.am.dto.consumer.CreateConsumerResponse;
import in.ekstep.am.dto.consumer.ReadConsumerResponse;
import in.ekstep.am.dto.credential.RegisterCredentialRequest;
import in.ekstep.am.dto.credential.RegisterCredentialResponse;
import in.ekstep.am.dto.group.GrantConsumerRequest;
import in.ekstep.am.dto.group.GrantConsumerResponse;
import in.ekstep.am.dto.group.ReadConsumerRequest;
import in.ekstep.am.step.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static java.text.MessageFormat.format;

@RestController
public class ConsumerController {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private CreateConsumerStepChain createConsumerStepChain;

  @Autowired
  private GrantConsumerStepChain grantConsumerStepChain;

  @Autowired
  private ReadConsumerStepChain readConsumerStepChain;

  @Autowired
  private RegisterCredentialStepChain registerCredentialStepChain;

  @Autowired
  private RegisterCredentialStepChainV2 registerCredentialStepChainV2;

  @Timed(name = "register-credential-api")
  @RequestMapping(method = RequestMethod.POST, value = "/v1/consumer/{consumer_name}/credential/register",
          consumes = "application/json", produces = "application/json")
  public ResponseEntity<RegisterCredentialResponse> registerCredential(@Valid @PathVariable(value = "consumer_name") String userName, @Valid @RequestBody RegisterCredentialRequest request, BindingResult bindingResult) {

    RegisterCredentialResponseBuilder responseBuilder = new RegisterCredentialResponseBuilder();
    try {
      log.debug(format("GOT REQUEST TO REGISTER CREDENTIAL. REQUEST: {0}, USERNAME:{1}", request, userName));

      if (bindingResult.hasErrors()) {
        return responseBuilder.badRequest(bindingResult);
      }

      responseBuilder
              .withMsgid(request.msgid())
              .withUsername(userName)
              .markSuccess();

      registerCredentialStepChain.execute(userName, request, responseBuilder);
      return responseBuilder.response();
    } catch (Exception e) {
      log.error(format("ERROR WHEN REGISTERING CREDENTIAL. REQUEST: {0}, USERNAME:{1}", request, userName), e);
      return responseBuilder
              .errorResponse("INTERNAL_ERROR", "UNABLE TO REGISTER CREDENTIAL DUE TO INTERNAL ERROR");
    }

  }

  @Timed(name = "register-credential-apiv2")
  @RequestMapping(method = RequestMethod.POST, value = "/v2/consumer/mobile_device/credential/register",
          consumes = "application/json", produces = "application/json")
  public ResponseEntity<RegisterCredentialResponse> registerCredential(@Valid @RequestBody RegisterCredentialRequest request, BindingResult bindingResult) {
    String userName = "mobile_device";
    RegisterCredentialResponseBuilder responseBuilder = new RegisterCredentialResponseBuilder();
    try {
      log.debug(format("GOT REQUEST TO REGISTER CREDENTIAL v2. REQUEST: {0}, USERNAME:{1}", request, userName));

      if (bindingResult.hasErrors()) {
        return responseBuilder.badRequest(bindingResult);
      }

      responseBuilder
              .withMsgid(request.msgid())
              .withUsername(userName)
              .markSuccess();

      registerCredentialStepChainV2.execute(userName, request, responseBuilder);
      return responseBuilder.response();
    } catch (Exception e) {
      log.error(format("ERROR WHEN REGISTERING CREDENTIAL. REQUEST: {0}, USERNAME:{1}", request, userName), e);
      return responseBuilder
              .errorResponse("INTERNAL_ERROR", "UNABLE TO REGISTER CREDENTIAL DUE TO INTERNAL ERROR");
    }

  }


  @Timed(name = "create-consumer-api")
  @RequestMapping(method = RequestMethod.POST, value = "/v1/consumer/create",
      consumes = "application/json", produces = "application/json")
  public ResponseEntity<CreateConsumerResponse> createConsumer(@Valid @RequestBody CreateConsumerRequest request, BindingResult bindingResult) {

    CreateConsumerResponseBuilder responseBuilder = new CreateConsumerResponseBuilder();
    try {
      log.debug(format("GOT REQUEST TO CREATE CONSUMER. REQUEST: {0}", request));

      if (bindingResult.hasErrors()) {
        return responseBuilder.badRequest(bindingResult);
      }

      responseBuilder
          .withMsgid(request.msgid())
          .withUsername(request.username())
          .markSuccess();

      createConsumerStepChain.execute(request, responseBuilder);
      return responseBuilder.response();
    } catch (Exception e) {
      log.error(format("ERROR WHEN CREATING CONSUMER. REQUEST: {0}", request), e);
      return responseBuilder
          .errorResponse("INTERNAL_ERROR", "UNABLE TO CREATE CONSUMER DUE TO INTERNAL ERROR");
    }
  }

  @Timed(name = "grant-consumer-api")
  @RequestMapping(method = RequestMethod.POST, value = "/v1/consumer/{user_name}/grant",
      consumes = "application/json", produces = "application/json")
  public ResponseEntity<GrantConsumerResponse> grantConsumer(@Valid @PathVariable(value = "user_name") String userName, @Valid @RequestBody GrantConsumerRequest request, BindingResult bindingResult) {
    GrantConsumerResponseBuilder responseBuilder = new GrantConsumerResponseBuilder();
    try {
      log.error(format("Binding Result:", String.valueOf(bindingResult.hasErrors())));
      if (bindingResult.hasErrors()) {
        ResponseEntity<GrantConsumerResponse> grantConsumerResponseResponseEntity = responseBuilder.badRequest(bindingResult);
        return grantConsumerResponseResponseEntity;
      }

      responseBuilder
          .withMsgid(request.msgid())
          .withUserName(userName)
          .withGroups(request.groups())
          .markSuccess();

      grantConsumerStepChain.execute(userName, request, responseBuilder);
      return responseBuilder
          .response();
    } catch (Exception e) {
      return responseBuilder
          .errorResponse("INTERNAL_ERROR", "UNABLE TO GRANT ACCESS TO CONSUMER DUE TO INTERNAL ERROR");
    }
  }

  @Timed(name = "read-consumer-api")
  @RequestMapping(method = RequestMethod.POST, value = "/v1/consumer/{user_name}/read",
      consumes = "application/json", produces = "application/json")
  public ResponseEntity<ReadConsumerResponse> readConsumer(@Valid @PathVariable(value = "user_name") String userName, @Valid @RequestBody ReadConsumerRequest request, BindingResult bindingResult) {
    ReadConsumerResponseBuilder responseBuilder = new ReadConsumerResponseBuilder();
    try {
      log.error(format("Binding Result:", String.valueOf(bindingResult.hasErrors())));
      if (bindingResult.hasErrors()) {
        ResponseEntity<ReadConsumerResponse> readConsumerResponseResponseEntity = responseBuilder.badRequest(bindingResult);
        return readConsumerResponseResponseEntity;
      }

      responseBuilder
          .withMsgid(request.msgid())
          .withUserName(userName)
          .markSuccess();

      readConsumerStepChain.execute(userName, responseBuilder);
      return responseBuilder
          .response();
    } catch (Exception e) {
      return responseBuilder
          .errorResponse("INTERNAL_ERROR", "UNABLE TO READ CONSUMER DETAILS DUE TO INTERNAL ERROR");
    }
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.initDirectFieldAccess();
  }
}

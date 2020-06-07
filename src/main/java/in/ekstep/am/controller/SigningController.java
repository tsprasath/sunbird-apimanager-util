package in.ekstep.am.controller;

import com.codahale.metrics.annotation.Timed;
import in.ekstep.am.builder.RegisterCredentialResponseBuilder;
import in.ekstep.am.dto.credential.RegisterCredentialRequest;
import in.ekstep.am.dto.credential.RegisterCredentialResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

public class SigningController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Timed(name = "v1-manageduser-generatetoken")
    @RequestMapping(method = RequestMethod.POST, value = "/v1/generate/token",
            consumes = "application/json", produces = "application/json")
    public ResponseEntity<RegisterCredentialResponse> registerCredential(@Valid @RequestBody RegisterCredentialRequest request, BindingResult bindingResult) {
        RegisterCredentialResponseBuilder responseBuilder = new RegisterCredentialResponseBuilder();
//        try {
//            log.debug(format("GOT REQUEST TO REGISTER CREDENTIAL v2. REQUEST: {0}, USERNAME:{1}", request, userName));
//
//            if (bindingResult.hasErrors()) {
//                return responseBuilder.badRequest(bindingResult);
//            }
//
//            responseBuilder
//                    .withMsgid(request.msgid())
//                    .withUsername(userName)
//                    .markSuccess();
//
//            registerCredentialStepChainV2.execute(userName, request, responseBuilder);
//            return responseBuilder.response();
//        } catch (Exception e) {
//            log.error(format("ERROR WHEN REGISTERING CREDENTIAL. REQUEST: {0}, USERNAME:{1}", request, userName), e);
            return responseBuilder
                    .errorResponse("INTERNAL_ERROR", "UNABLE TO REGISTER CREDENTIAL DUE TO INTERNAL ERROR");
//        }

    }

}

package in.ekstep.am.controller;

import com.codahale.metrics.annotation.Timed;
import in.ekstep.am.builder.RegisterCredentialResponseBuilder;
import in.ekstep.am.builder.SignPayloadResponseBuilder;
import in.ekstep.am.dto.ResponseParams;
import in.ekstep.am.dto.credential.RegisterCredentialRequest;
import in.ekstep.am.dto.credential.RegisterCredentialResponse;
import in.ekstep.am.dto.credential.RegisterCredentialResult;
import in.ekstep.am.dto.signing.Payload;
import in.ekstep.am.dto.signing.SignPayloadRequest;
import in.ekstep.am.dto.signing.SignPayloadResponse;
import in.ekstep.am.dto.signing.SignPayloadResult;
import in.ekstep.am.jwt.JWTUtil;
import in.ekstep.am.jwt.KeyData;
import in.ekstep.am.jwt.KeyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static java.text.MessageFormat.format;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@RestController
public class SigningController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KeyManager keyManager;

    @Timed(name = "v1-sign-payload")
    @RequestMapping(method = RequestMethod.POST, value = "/v1/sign/payload",
            consumes = "application/json", produces = "application/json")
    public ResponseEntity<SignPayloadResponse> signPayload(@Valid @RequestBody SignPayloadRequest request, BindingResult bindingResult) {
        SignPayloadResponseBuilder responseBuilder = new SignPayloadResponseBuilder();

        log.debug(format("GOT REQUEST TO SIGN PAYLOAD v1. REQUEST: {0}", request));

        if (bindingResult.hasErrors()) {
            return responseBuilder.badRequest(bindingResult);
        }

        responseBuilder
                .withMsgid(request.msgid())
                .markSuccess();

        try {
            KeyData keyData = keyManager.getRandomKey();
            Map<String, String> headerOptions = createHeaderOptions(keyData.getKeyId());
            Payload[] data = request.getRequest().data();
            for (int i = 0; i < data.length; i++) {
                data[i].setToken(JWTUtil.createRS256Token(headerOptions, data[i], keyData.getPrivateKey()));
            }
            responseBuilder.setData(data);
            return responseBuilder.response();

        } catch (Exception e) {
            log.error(format("ERROR WHEN SIGNING PAYLOAD. REQUEST: {0}", request), e);
            return responseBuilder
                    .errorResponse("INTERNAL_ERROR", "UNABLE TO SIGN PAYLOAD DUE TO INTERNAL ERROR");
        }

    }

    private Map<String, String> createHeaderOptions(String keyId) {
        Map<String, String> headers = new HashMap<>();
        headers.put("kid", keyId);
        return headers;
    }

}

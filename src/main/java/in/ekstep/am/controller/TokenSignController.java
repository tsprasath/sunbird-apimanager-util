package in.ekstep.am.controller;

import com.codahale.metrics.annotation.Timed;
import in.ekstep.am.builder.TokenSignResponseBuilder;
import in.ekstep.am.dto.token.TokenSignResponse;
import in.ekstep.am.step.TokenSignStepChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import in.ekstep.am.dto.token.TokenSignRequest;

@RestController
public class TokenSignController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TokenSignStepChain tokenSignStepChain;

    @Timed(name = "keycloak-refresh-sign-api")
    @PostMapping(path = "/v1/auth/refresh/token", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    public ResponseEntity<TokenSignResponse> verifyAndSendNewToken(@RequestParam("refresh_token") TokenSignRequest tokenSignRequest) {
        TokenSignResponseBuilder tokenSignResponseBuilder = new TokenSignResponseBuilder();
        try {

            tokenSignResponseBuilder.markSuccess();
            tokenSignStepChain.execute(tokenSignRequest, tokenSignResponseBuilder);
            return tokenSignResponseBuilder.response();
        }
        catch (Exception e) {
            log.error("ERROR REFRESHING TOKEN: " + tokenSignRequest.getRefresh_token() + " due to " + e);
            return tokenSignResponseBuilder.errorResponse("INTERNAL_SERVER_ERROR", "Oops! Something went wrong!");
        }
    }
}
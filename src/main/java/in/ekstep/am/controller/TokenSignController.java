package in.ekstep.am.controller;

import com.codahale.metrics.annotation.Timed;
import in.ekstep.am.builder.TokenSignResponseBuilder;
import in.ekstep.am.dto.token.TokenSignResponse;
import in.ekstep.am.step.TokenSignStepChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import in.ekstep.am.dto.token.TokenSignRequest;

@RestController
public class TokenSignController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TokenSignStepChain tokenSignStepChain;

    @Timed(name = "keycloak-refresh-sign-api")
    @PostMapping(path = "/v1/auth/refresh/token", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    public ResponseEntity<TokenSignResponse> verifyAndSendNewToken(TokenSignRequest tokenSignRequest, BindingResult bindingResult) {
        TokenSignResponseBuilder tokenSignResponseBuilder = new TokenSignResponseBuilder();
        try {
            if (bindingResult.hasErrors()) {
                return tokenSignResponseBuilder.badRequest(bindingResult);
            }

            tokenSignResponseBuilder.markSuccess();
            tokenSignStepChain.execute(tokenSignRequest, tokenSignResponseBuilder);
            return tokenSignResponseBuilder.response();
        }
        catch (Exception e) {
            log.error("ERROR REFRESHING TOKEN" + e);
            return tokenSignResponseBuilder.errorResponse("Something went wrong", "INTERNAL_SERVER_ERROR");
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }
}
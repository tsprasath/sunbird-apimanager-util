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

import static java.text.MessageFormat.format;

@RestController
public class TokenSignController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TokenSignStepChain tokenSignStepChain;

    @Timed(name = "keycloak-refresh-sign-api")
    @PostMapping(path = "/v1/auth/refresh/token", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    //public boolean verifyAndSignRefreshToken(@RequestParam("refresh_token") String refresh_token) throws Exception {
    public ResponseEntity<TokenSignResponse> verifyAndSendRefreshToken(TokenSignRequest tokenSignRequest, BindingResult bindingResult) throws Exception {
        TokenSignResponseBuilder keycloakSignResponseBuilder = new TokenSignResponseBuilder();
        try {
            if (bindingResult.hasErrors()) {
                return keycloakSignResponseBuilder.badRequest(bindingResult);
            }

            keycloakSignResponseBuilder.markSuccess();
            log.info("GOT REQUEST TO REFRESH TOKEN");
            tokenSignStepChain.execute(tokenSignRequest.getRefresh_token(), keycloakSignResponseBuilder);
            return keycloakSignResponseBuilder.response();
        }
        catch (Exception e) {
            log.error(format("ERROR REFRESHING TOKEN: {0} ", (Object) e.getStackTrace()));
            return keycloakSignResponseBuilder.errorResponse("SOMETHING WENT WRONG", "INTERNAL_ERROR");
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }
}
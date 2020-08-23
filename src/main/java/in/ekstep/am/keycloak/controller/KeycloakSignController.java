package in.ekstep.am.keycloak.controller;

import com.codahale.metrics.annotation.Timed;
import in.ekstep.am.keycloak.builder.KeycloakResponseBuilder;
import in.ekstep.am.keycloak.builder.KeycloakSignResponseBuilder;
import in.ekstep.am.keycloak.dto.KeycloakSignResponse;
import in.ekstep.am.keycloak.step.KeycloakSignStepChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import in.ekstep.am.keycloak.dto.KeycloakSignRequest;

import static java.text.MessageFormat.format;

@RestController
public class KeycloakSignController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KeycloakSignStepChain keycloakSignStepChain;

    @Timed(name = "keycloak-refresh-sign-api")
    @PostMapping(path = "/v1/auth/refresh/token", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    //public boolean verifyAndSignRefreshToken(@RequestParam("refresh_token") String refresh_token) throws Exception {
    public ResponseEntity<KeycloakSignResponse> verifyAndSendRefreshToken(KeycloakSignRequest keycloakSignRequest , BindingResult bindingResult) throws Exception {
        KeycloakSignResponseBuilder keycloakSignResponseBuilder = new KeycloakSignResponseBuilder();
//        try {
            if (bindingResult.hasErrors()) {
                return keycloakSignResponseBuilder.badRequest(bindingResult);
            }

            keycloakSignResponseBuilder.markSuccess();
            keycloakSignStepChain.execute(keycloakSignRequest.refresh_token(), keycloakSignResponseBuilder);
            return keycloakSignResponseBuilder.response();
//        }

/*
        catch (Exception e) {
            log.error(format("ERROR REFRESHING TOKEN: {0} ", e.getStackTrace()));
            return keycloakSignResponseBuilder.errorResponse("INTERNAL_ERROR", "UNABLE TO REFRESH TOKEN DUE TO INTERNAL ERROR");
        }
*/
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }
}
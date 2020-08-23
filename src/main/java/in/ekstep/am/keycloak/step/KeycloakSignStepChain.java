package in.ekstep.am.keycloak.step;

import in.ekstep.am.builder.RegisterCredentialResponseBuilder;
import in.ekstep.am.dto.credential.RegisterCredentialRequest;
import in.ekstep.am.jwt.KeyManager;
import in.ekstep.am.keycloak.builder.KeycloakSignResponseBuilder;
import in.ekstep.am.keycloak.dto.KeycloakSignRequest;
import in.ekstep.am.step.SignCredentialWithKeyStep;
import in.ekstep.am.step.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.asList;

@Component
public class KeycloakSignStepChain {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private KeyManager keyManager;

    public void execute(String token, KeycloakSignResponseBuilder keycloakSignResponseBuilder) throws Exception {
        for (KeycloakStep step : stepChain(token, keycloakSignResponseBuilder)) {
            if (keycloakSignResponseBuilder.successful()) {
                step.execute();
            }
        }
    }

    private List<KeycloakStep> stepChain(String token, KeycloakSignResponseBuilder keycloakSignResponseBuilder) {
        KeycloakSignRefreshToken keycloakSignRefreshToken = new KeycloakSignRefreshToken(token, keycloakSignResponseBuilder, keyManager);
        return asList(keycloakSignRefreshToken);
    }
}
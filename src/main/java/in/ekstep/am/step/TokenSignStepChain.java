package in.ekstep.am.step;

import in.ekstep.am.jwt.KeyManager;
import in.ekstep.am.builder.TokenSignResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.asList;

@Component
public class TokenSignStepChain {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private KeyManager keyManager;

    public void execute(String token, TokenSignResponseBuilder keycloakSignResponseBuilder) throws Exception {
        for (TokenStep step : stepChain(token, keycloakSignResponseBuilder)) {
            if (keycloakSignResponseBuilder.successful()) {
                step.execute();
            }
        }
    }

    private List<TokenStep> stepChain(String token, TokenSignResponseBuilder keycloakSignResponseBuilder) {
        TokenSignStep tokenSignStep = new TokenSignStep(token, keycloakSignResponseBuilder, keyManager);
        return asList(tokenSignStep);
    }
}
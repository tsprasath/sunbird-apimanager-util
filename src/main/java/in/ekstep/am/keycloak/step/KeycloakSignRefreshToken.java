package in.ekstep.am.keycloak.step;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import in.ekstep.am.jwt.*;
import in.ekstep.am.keycloak.builder.KeycloakSignResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KeycloakSignRefreshToken implements KeycloakStep {

    private static String SEPARATOR = ".";
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    KeycloakSignResponseBuilder keycloakSignResponseBuilder;
    private KeyManager keyManager;

    String token;

    public KeycloakSignRefreshToken(String token, KeycloakSignResponseBuilder keycloakSignResponseBuilder, KeyManager keyManager) {
        this.token = token;
        this.keycloakSignResponseBuilder = keycloakSignResponseBuilder;
        this.keyManager = keyManager;
    }

    @Override
    public void execute() throws Exception {
        if (!JWTUtil.verifyRS256Token(token)) {
            log.info("Error in refreshing token: Invalid Signature");
            keycloakSignResponseBuilder.markFailure("Invalid Signature", "invalid_grant");
        } else {
            String[] tokenElements = token.split("\\.");
            String header = tokenElements[0];
            String body = tokenElements[1];
            Map headerData = GsonUtil.fromJson(new String(Base64Util.decode(header, 11)), Map.class);
            Map bodyData = GsonUtil.fromJson(new String(Base64Util.decode(body, 11)), Map.class);
            boolean isValid = generateNewAccessToken(headerData, bodyData);
            if(isValid)
                keycloakSignResponseBuilder.markSuccess();
        }
    }

    private boolean generateNewAccessToken(Map headerData, Map bodyData) {
        Map<String, String> headers = new HashMap<>();
        Map<String, Object> body = new HashMap<>();
        KeyData keyData = keyManager.getRandomKey("access");

        if (!headerData.get("alg").equals("RS256")) {
            log.info("Error in refreshing token. Invalid algorithm:  " + headerData.get("alg"));
            keycloakSignResponseBuilder.markFailure("Invalid Algorithm", "invalid_grant");
            return false;
        } else
            headers.put("alg", (String) headerData.get("alg"));

        if (!headerData.get("typ").equals("JWT")) {
            log.info("Error in refreshing token. Invalid type: " + headerData.get("typ"));
            keycloakSignResponseBuilder.markFailure("Invalid Typ", "invalid_grant");
            return false;
        } else
            headers.put("typ", (String) headerData.get("typ"));

        if (!headerData.get("kid").equals( keyManager.getValueUsingKey("token.kid").getValue()) && !headerData.get("kid").equals(keyData.getKeyId())) {
            log.info("Error in refreshing token. Invalid kid: " + keyManager.getValueUsingKey("token.kid").getValue());
            keycloakSignResponseBuilder.markFailure("Invalid kid", "invalid_grant");
            return false;
        } else
            headers.put("kid", keyData.getKeyId());

        long iat = System.currentTimeMillis() / 1000;
        long exp = iat + Long.parseLong(keyManager.getValueUsingKey("token.validity").getValue());
        long currentExp = Double.valueOf((Double) bodyData.get("exp")).longValue();
        if ( currentExp <  exp ) {
            log.info("Error in refreshing token. Token expired: " + bodyData.get("exp"));
            keycloakSignResponseBuilder.markFailure("Refresh token expired", "invalid_grant");
            return false;
        } else
            body.put("exp", exp);

        if (!bodyData.get("iss").equals(KeyManager.getValueUsingKey("token.domain").getValue())) {
            log.info("Error in refreshing token: Invalid ISS");
            keycloakSignResponseBuilder.markFailure("Invalid ISS", "invalid_grant");
            return false;
        } else
            body.put("iss", KeyManager.getValueUsingKey("token.domain").getValue());

        body.put("jti", UUID.randomUUID().toString());
        body.put("nbf", 0);
        body.put("iat", iat);
        body.put("aud", bodyData.get("aud"));
        body.put("name", bodyData.get("name"));
        body.put("preferred_username", bodyData.get("preferred_username"));
        body.put("given_name", bodyData.get("given_name"));
        body.put("email", bodyData.get("email"));
        String token = JWTUtil.createRS256Token(headers, body, keyData.getPrivateKey());
        keycloakSignResponseBuilder.setAccess_token(token);
        return true;
    }
}

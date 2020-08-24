package in.ekstep.am.step;

import in.ekstep.am.jwt.*;
import in.ekstep.am.builder.TokenSignResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenSignStep implements TokenStep {

    private static String SEPARATOR = ".";
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    TokenSignResponseBuilder tokenSignResponseBuilder;
    private KeyManager keyManager;

    String token;

    public TokenSignStep(String token, TokenSignResponseBuilder keycloakSignResponseBuilder, KeyManager keyManager) {
        this.token = token;
        this.tokenSignResponseBuilder = keycloakSignResponseBuilder;
        this.keyManager = keyManager;
    }

    @Override
    public void execute() throws Exception {
        if (!JWTUtil.verifyRS256Token(token)) {
            log.info("Error in refreshing token: Invalid Signature");
            tokenSignResponseBuilder.markFailure("Invalid Signature", "invalid_grant");
        } else {
            String[] tokenElements = token.split("\\.");
            String header = tokenElements[0];
            String body = tokenElements[1];
            Map headerData = GsonUtil.fromJson(new String(Base64Util.decode(header, 11)), Map.class);
            Map bodyData = GsonUtil.fromJson(new String(Base64Util.decode(body, 11)), Map.class);
            boolean isValid = generateNewToken(headerData, bodyData);
            if(isValid)
                tokenSignResponseBuilder.markSuccess();
        }
    }

    private boolean generateNewToken(Map headerData, Map bodyData) {
        Map<String, String> headers = new HashMap<>();
        Map<String, Object> body = new HashMap<>();
        KeyData keyData = keyManager.getRandomKey("access");

        if (!headerData.get("alg").equals("RS256")) {
            log.info("Error in refreshing token. Invalid algorithm:  " + headerData.get("alg"));
            tokenSignResponseBuilder.markFailure("Invalid Algorithm", "invalid_grant");
            return false;
        } else
            headers.put("alg", (String) headerData.get("alg"));

        if (!headerData.get("typ").equals("JWT")) {
            log.info("Error in refreshing token. Invalid type: " + headerData.get("typ"));
            tokenSignResponseBuilder.markFailure("Invalid Typ", "invalid_grant");
            return false;
        } else
            headers.put("typ", (String) headerData.get("typ"));

        if (!headerData.get("kid").equals( keyManager.getValueUsingKey("token.kid").getValue()) && !headerData.get("kid").equals(keyData.getKeyId())) {
            log.info("Error in refreshing token. Invalid kid: " + keyManager.getValueUsingKey("token.kid").getValue());
            tokenSignResponseBuilder.markFailure("Invalid kid", "invalid_grant");
            return false;
        } else
            headers.put("kid", keyData.getKeyId());

        if (!bodyData.get("iss").equals(KeyManager.getValueUsingKey("token.domain").getValue())) {
            log.info("Error in refreshing token. Invalid ISS: " + bodyData.get("iss"));
            tokenSignResponseBuilder.markFailure("Invalid ISS", "invalid_grant");
            return false;
        } else
            body.put("iss", KeyManager.getValueUsingKey("token.domain").getValue());

        long iat = System.currentTimeMillis() / 1000;
        long refreshIat = ((Double) bodyData.get("iat")).longValue();
        long validRefreshIat = refreshIat +  Long.parseLong(keyManager.getValueUsingKey("token.offline.vadity").getValue());
        long exp = iat +  Long.parseLong(keyManager.getValueUsingKey("token.validity").getValue());

        if (!bodyData.get("typ").equals("Offline")) {
            log.info("Error in refreshing token. Not an offline token: " + bodyData.get("typ"));
            tokenSignResponseBuilder.markFailure("Not an offline token", "invalid_grant");
            return false;
        } else {
            if(validRefreshIat < iat){
                log.info("Error in refreshing token. Offline token expired at: " + new Date(validRefreshIat * 1000L));
                return false;
            }
            else
                body.put("exp", exp);
        }

        body.put("typ", "Bearer");
        body.put("iat", iat);
        body.put("aud", bodyData.get("aud"));
        body.put("sub", bodyData.get("sub"));
        body.put("session_state", bodyData.get("session_state"));
        tokenSignResponseBuilder.setRefresh_token(token);
        tokenSignResponseBuilder.setExpires_in(Long.parseLong(keyManager.getValueUsingKey("token.validity").getValue()));
        tokenSignResponseBuilder.setToken_type("Bearer");
        tokenSignResponseBuilder.setNot_before_policy(0);
        tokenSignResponseBuilder.setSession_state((String) bodyData.get("session_state"));
        tokenSignResponseBuilder.setRefresh_expires_in(0);
        String token = JWTUtil.createRS256Token(headers, body, keyData.getPrivateKey());
        tokenSignResponseBuilder.setAccess_token(token);
/*
        // Code to create refresh token and then set it using the setter method
        body.put("typ", "Offline");
        body.put("exp", "0");
        token = JWTUtil.createRS256Token(headers, body, keyData.getPrivateKey());
*/
        return true;
    }
}
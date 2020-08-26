package in.ekstep.am.step;

import in.ekstep.am.dto.token.TokenSignRequest;
import in.ekstep.am.jwt.*;
import in.ekstep.am.builder.TokenSignResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.text.MessageFormat.format;

public class TokenSignStep implements TokenStep {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    TokenSignResponseBuilder tokenSignResponseBuilder;
    @Autowired
    private KeyManager keyManager;
    @Autowired
    TokenSignRequest token;

    private String currentToken, kid, iss;
    private Map headerData, bodyData;
    private KeyData keyData;
    private long tokenValidity, offlineTokenValidity, iat, refreshIat;

    public TokenSignStep(TokenSignRequest token, TokenSignResponseBuilder tokenSignResponseBuilder, KeyManager keyManager) {
        this.token = token;
        this.tokenSignResponseBuilder = tokenSignResponseBuilder;
        this.keyManager = keyManager;
    }

    private boolean isJwtTokenValid() {
        currentToken = token.getRefresh_token();
        headerData = GsonUtil.fromJson(new String(Base64Util.decode(currentToken.split("\\.")[0], 11)), Map.class);
        bodyData = GsonUtil.fromJson(new String(Base64Util.decode(currentToken.split("\\.")[1], 11)), Map.class);


        if(currentToken.split("\\.").length != 3){
            log.error("Invalid JWT token: " + currentToken);
            return false;
        }

        kid = keyManager.getValueFromKeyMetaData("token.kid");
        if (!headerData.get("kid").equals(kid)) {
            log.error("Invalid kid: " + kid);
            return false;
        }

        if (!JWTUtil.verifyRS256Token(currentToken, keyManager, kid)) {
            log.error("Invalid Signature: " + currentToken);
            return false;
        }

        keyData = keyManager.getRandomKey("access");

        if (!headerData.get("alg").equals("RS256")) {
            log.error("Invalid algorithm:  " + headerData.get("alg"));
            return false;
        }

        if (!headerData.get("typ").equals("JWT")) {
            log.error("Invalid typ: " + headerData.get("typ"));
            return false;
        }

        iss = keyManager.getValueFromKeyMetaData("token.domain");
        if (!bodyData.get("iss").equals(iss)) {
            log.error("Invalid ISS: " + iss);
            return false;
        }

        tokenValidity = Long.parseLong(keyManager.getValueFromKeyMetaData("token.validity"));
        offlineTokenValidity =  Long.parseLong(keyManager.getValueFromKeyMetaData("token.offline.validity"));

        iat = System.currentTimeMillis() / 1000;
        refreshIat = ((Double) bodyData.get("iat")).longValue();
        long validRefreshIat = refreshIat + offlineTokenValidity;

        if (!bodyData.get("typ").equals("Offline")) {
            log.error("Not an offline token: " + bodyData.get("typ"));
            return false;
        }

        if(validRefreshIat < iat){
            log.error("Offline token expired at: " + new Date(validRefreshIat * 1000L));
            return false;
        }
        return true;
    }

    private void generateNewJwtToken() {
        Map<String, String> header = new HashMap<>();
        Map<String, Object> body = new HashMap<>();
        long exp = iat +tokenValidity;

        header.put("alg", (String) headerData.get("alg"));
        header.put("typ", (String) headerData.get("typ"));
        header.put("kid", kid);
        body.put("exp", exp);
        body.put("iat", iat);
        body.put("iss", iss);
        body.put("aud", bodyData.get("aud"));
        body.put("sub", bodyData.get("sub"));
        body.put("typ", "Bearer");

        tokenSignResponseBuilder.setRefreshToken(currentToken);
        tokenSignResponseBuilder.setExpiresIn(tokenValidity);
        tokenSignResponseBuilder.setRefreshExpiresIn(0);

        String token = JWTUtil.createRS256Token(header, body, keyData.getPrivateKey());
        tokenSignResponseBuilder.setAccessToken(token);

        long tokenOlderThanLog = Long.parseLong(keyManager.getValueFromKeyMetaData("token.older.write.log"));
        long tokenOlderThan =  ((new Date(iat * 1000).getTime() - new Date(refreshIat * 1000).getTime())  / (1000 * 60 * 60 * 24));
        if(tokenOlderThan >= tokenOlderThanLog)
            log.info(format("Token issued before days: {0}", tokenOlderThan));
    }

    @Override
    public void execute() {
        if(isJwtTokenValid())
            generateNewJwtToken();
        else
            tokenSignResponseBuilder.markFailure("invalid_grant", "invalid_grant");
    }
}
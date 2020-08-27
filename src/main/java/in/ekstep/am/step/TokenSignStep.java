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
    @Autowired
    private KeyData keyData;

    private String currentToken, kid, iss;
    private Map headerData, bodyData;
    private long tokenValidity, offlineTokenValidity, currentTime, tokenWasIssuedAt;

    public TokenSignStep(TokenSignRequest token, TokenSignResponseBuilder tokenSignResponseBuilder, KeyManager keyManager) {
        this.token = token;
        this.tokenSignResponseBuilder = tokenSignResponseBuilder;
        this.keyManager = keyManager;
    }

    private boolean isJwtTokenValid() {
        currentToken = token.getRefresh_token();

        if(currentToken.split("\\.").length != 3 || currentToken.equals(null)){
            log.error("Invalid JWT token: " + currentToken);
            return false;
        }

        headerData = GsonUtil.fromJson(new String(Base64Util.decode(currentToken.split("\\.")[0], 11)), Map.class);
        bodyData = GsonUtil.fromJson(new String(Base64Util.decode(currentToken.split("\\.")[1], 11)), Map.class);

        kid = keyManager.getValueFromKeyMetaData("refresh.token.kid");
        if (!headerData.get("kid").equals(kid)) {
            log.error("Invalid kid: " + headerData.get("kid"));
            return false;
        }

        if (!headerData.get("alg").equals("RS256")) {
            log.error("Invalid algorithm:  " + headerData.get("alg"));
            return false;
        }

        if (!headerData.get("typ").equals("JWT")) {
            log.error("Invalid typ: " + headerData.get("typ"));
            return false;
        }

        iss = keyManager.getValueFromKeyMetaData("refresh.token.domain");
        if (!bodyData.get("iss").equals(iss)) {
            log.error("Invalid ISS: " + bodyData.get("iss"));
            return false;
        }

        if (!bodyData.get("typ").equals("Offline")) {
            log.error("Not an offline token: " + bodyData.get("typ"));
            return false;
        }

        if (!JWTUtil.verifyRS256Token(currentToken, keyManager, kid)) {
            log.error("Invalid Signature: " + currentToken);
            return false;
        }

        tokenValidity = Long.parseLong(keyManager.getValueFromKeyMetaData("access.token.validity"));
        offlineTokenValidity =  Long.parseLong(keyManager.getValueFromKeyMetaData("refresh.token.offline.validity"));
        currentTime = System.currentTimeMillis() / 1000;
        tokenWasIssuedAt = ((Double) bodyData.get("iat")).longValue();
        long tokenValidTill = tokenWasIssuedAt + offlineTokenValidity;

        if(tokenValidTill < currentTime){
            log.error("Offline token expired on: " + new Date(tokenValidTill * 1000L));
            return false;
        }
        return true;
    }

    private void generateNewJwtToken() {
        Map<String, String> header = new HashMap<>();
        Map<String, Object> body = new HashMap<>();
        long exp = currentTime + tokenValidity;
        keyData = keyManager.getRandomKey("access");

        header.put("alg", (String) headerData.get("alg"));
        header.put("typ", (String) headerData.get("typ"));
        header.put("kid", keyData.getKeyId());
        body.put("exp", exp);
        body.put("iat", currentTime);
        body.put("iss", iss);
        body.put("aud", bodyData.get("aud"));
        body.put("sub", bodyData.get("sub"));
        body.put("typ", "Bearer");

        tokenSignResponseBuilder.setAccessToken(JWTUtil.createRS256Token(header, body, keyData.getPrivateKey()));
        tokenSignResponseBuilder.setRefreshToken(currentToken);
        tokenSignResponseBuilder.setExpiresIn(tokenValidity);
        tokenSignResponseBuilder.setRefreshExpiresIn(0);

        long tokenOlderThanLog = Long.parseLong(keyManager.getValueFromKeyMetaData("refresh.token.log.older.than"));
        long tokenOlderThan = ((new Date(currentTime * 1000).getTime() - new Date(tokenWasIssuedAt * 1000).getTime())  / (1000 * 60 * 60 * 24));
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
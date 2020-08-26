package in.ekstep.am.dto.token;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenSignResult {
    @JsonProperty(value = "access_token")
    private String accessToken;
    @JsonProperty(value = "expires_in")
    private long expiresIn;
    @JsonProperty(value = "refresh_expires_in")
    private long refreshExpiresIn;
    @JsonProperty(value = "refresh_token")
    private String refreshToken;
    @JsonProperty(value = "token_type")
    private String tokenType;
    @JsonProperty(value = "not-before-policy")
    private long notBeforePolicy;
    @JsonProperty(value = "session_state")
    private String sessionState;

    private TokenSignResult() {
    }

    public TokenSignResult(String accessToken, long expiresIn, long refreshExpiresIn, String refreshToken, String tokenType, long notBeforePolicy, String sessionState) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshExpiresIn = refreshExpiresIn;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.notBeforePolicy = notBeforePolicy;
        this.sessionState = sessionState;
    }

    public TokenSignResult(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    @Override
    public String toString() {
        return "CreateKeycloakRefreshResult{" +
                "accessToken='" + accessToken + '\'' +
                ", expiresIn='" + expiresIn + '\'' +
                ", refresh_expiresIn=" + refreshExpiresIn +
                ", refreshToken=" + refreshToken +
                ", tokenType=" + tokenType +
                ", notBeforePolicy=" + notBeforePolicy +
                ", sessionState=" + sessionState +
                '}';
    }
}

package in.ekstep.am.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KeycloakSignResult {
    @JsonProperty
    private String access_token;
    @JsonProperty
    private String expires_in;
    @JsonProperty
    private String refresh_expires_in;
    @JsonProperty
    private String refresh_token;
    @JsonProperty
    private String token_type;
    @JsonProperty(value = "not-before-policy")
    private String not_before_policy;
    @JsonProperty
    private String session_state;

    private KeycloakSignResult() {
    }

    public KeycloakSignResult(String access_token, String expires_in, String refresh_expires_in, String refresh_token, String token_type, String not_before_policy, String session_state) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.refresh_expires_in = refresh_expires_in;
        this.refresh_token = refresh_token;
        this.token_type = token_type;
        this.not_before_policy = not_before_policy;
        this.session_state = session_state;
    }

    public KeycloakSignResult(String refresh_token) {
        this.refresh_token = refresh_token;
    }


    @Override
    public String toString() {
        return "CreateKeycloakRefreshResult{" +
                "access_token='" + access_token + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", refresh_expires_in=" + refresh_expires_in +
                ", refresh_token=" + refresh_token +
                ", token_type=" + token_type +
                ", not_before_policy=" + not_before_policy +
                ", session_state=" + session_state +
                '}';
    }
}

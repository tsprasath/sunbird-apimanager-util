package in.ekstep.am.keycloak.builder;

import com.fasterxml.jackson.annotation.JsonProperty;
import in.ekstep.am.builder.SignPayloadResponseBuilder;
import in.ekstep.am.dto.ResponseParams;
import in.ekstep.am.dto.signing.Payload;
import in.ekstep.am.keycloak.dto.*;

public class KeycloakSignResponseBuilder extends KeycloakResponseBuilder<KeycloakSignResponse> implements KeycloakToken {

    private String access_token;
    private String expires_in;
    private String refresh_expires_in;
    private String refresh_token;
    private String token_type;
    private String not_before_policy;
    private String session_state;

    public static KeycloakSignResponseBuilder newInstance() {
        return new KeycloakSignResponseBuilder();
    }

/*
    public KeycloakSignResponse build() {
        return new KeycloakSignResponse("api.refresh.token",
                "1.0", System.currentTimeMillis(), success ? KeycloakResponseParams.successful() : KeycloakResponseParams.failed(err, errMsg),
                success ? KeycloakResponseCode.OK : KeycloakResponseCode.invalid_grant,
                new KeycloakSignResult(refresh_token));
    }
*/

    public KeycloakSignResponse build() {
        return new KeycloakSignResponse("api.refresh.token",
                "1.0", System.currentTimeMillis(), success ? KeycloakResponseParams.successful() : KeycloakResponseParams.failed(err, errMsg),
                success ? KeycloakResponseCode.OK : KeycloakResponseCode.invalid_grant,
                new KeycloakSignResult(access_token, expires_in, refresh_expires_in, refresh_token, token_type, not_before_policy, session_state));
    }

    @Override
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    @Override
    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    @Override
    public void setRefresh_expires_in(String refresh_expires_in) {
        this.refresh_expires_in = refresh_expires_in;
    }

    @Override
    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    @Override
    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    @Override
    public void setNot_before_policy(String not_before_policy) {
        this.not_before_policy = not_before_policy;
    }

    @Override
    public void setSession_state(String session_state) {
        this.session_state = session_state;
    }
}

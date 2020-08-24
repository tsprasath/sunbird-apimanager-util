package in.ekstep.am.builder;

import in.ekstep.am.dto.token.TokenResponseCode;
import in.ekstep.am.dto.token.TokenResponseParams;
import in.ekstep.am.dto.token.TokenSignResponse;
import in.ekstep.am.dto.token.TokenSignResult;

public class TokenSignResponseBuilder extends ResponseBuilder<TokenSignResponse> implements TokenDetails {

    private String access_token;
    private long expires_in;
    private long refresh_expires_in;
    private String refresh_token;
    private String token_type;
    private long not_before_policy;
    private String session_state;

    public static TokenSignResponseBuilder newInstance() {
        return new TokenSignResponseBuilder();
    }

    public TokenSignResponse build() {
        return new TokenSignResponse("api.refresh.token",
                "1.0", System.currentTimeMillis(), success ? TokenResponseParams.successful() : TokenResponseParams.failed(err, errMsg),
                success ? TokenResponseCode.OK : TokenResponseCode.invalid_grant,
                new TokenSignResult(access_token, expires_in, refresh_expires_in, refresh_token, token_type, not_before_policy, session_state));
    }

    @Override
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    @Override
    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    @Override
    public void setRefresh_expires_in(long refresh_expires_in) {
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
    public void setNot_before_policy(long not_before_policy) {
        this.not_before_policy = not_before_policy;
    }

    @Override
    public void setSession_state(String session_state) {
        this.session_state = session_state;
    }
}

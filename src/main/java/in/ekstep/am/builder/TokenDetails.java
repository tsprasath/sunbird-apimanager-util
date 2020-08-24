package in.ekstep.am.builder;

public interface TokenDetails {
    void setAccess_token(String access_token);
    void setExpires_in(long expires_in);
    void setRefresh_expires_in(long refresh_expires_in);
    void setRefresh_token(String refresh_token);
    void setToken_type(String token_type);
    void setNot_before_policy(long not_before_policy);
    void setSession_state(String session_state);
    void markFailure(String error, String errmsg);
    void markSuccess();
}

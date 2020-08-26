package in.ekstep.am.builder;

public interface TokenDetails {
    void setAccessToken(String accessToken);
    void setExpiresIn(long expiresIn);
    void setRefreshExpiresIn(long refreshExpiresIn);
    void setRefreshToken(String refreshToken);
    void setTokenType(String tokenType);
    void setNotBeforePolicy(long notBeforePolicy);
    void setSessionState(String sessionState);
    void markFailure(String error, String errmsg);
    void markSuccess();
}

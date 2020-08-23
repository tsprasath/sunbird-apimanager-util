package in.ekstep.am.keycloak.dto;

import in.ekstep.am.constraint.NoSpace;

import javax.validation.constraints.NotNull;

public class KeycloakSignRequest {
    @NotNull(message = "REFRESH TOKEN IS MANDATORY")
    @NoSpace(message = "REFRESH TOKEN MUST NOT HAVE SPACE")
    private String refresh_token;

    public KeycloakSignRequest(){}

    public KeycloakSignRequest(String refresh_token){
        this.refresh_token = refresh_token;
    }

    public String refresh_token(){
        return refresh_token;
    }
}
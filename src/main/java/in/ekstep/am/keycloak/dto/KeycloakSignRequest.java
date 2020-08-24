package in.ekstep.am.keycloak.dto;

import in.ekstep.am.constraint.NoSpace;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class KeycloakSignRequest {
    @Valid
    @NotNull(message = "REFRESH TOKEN IS MANDATORY")
    @NotEmpty(message = "REFRESH TOKEN IS MANDATORY")
    @NotBlank(message = "REFRESH TOKEN IS MANDATORY")
    @NoSpace(message = "REFRESH TOKEN IS MANDATORY")
    private String refresh_token;

    public KeycloakSignRequest(){}

    public KeycloakSignRequest(String refresh_token){
        this.refresh_token = refresh_token;
    }

    public String getRefresh_token(){
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token){
        this.refresh_token = refresh_token;
    }
}
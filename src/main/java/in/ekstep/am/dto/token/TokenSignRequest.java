package in.ekstep.am.dto.token;

import in.ekstep.am.constraint.NoSpace;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class TokenSignRequest {
    private String refresh_token;

    public TokenSignRequest(){}

    public TokenSignRequest(String refresh_token){
        this.refresh_token = refresh_token;
    }

    public String getRefresh_token(){
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token){
        this.refresh_token = refresh_token;
    }
}
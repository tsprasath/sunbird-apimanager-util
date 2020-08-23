package in.ekstep.am.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KeycloakResponseParams {
    @JsonProperty
    private String resmsgid;
    @JsonProperty
    private KeycloakResponseStatus status;
    @JsonProperty
    private String err;
    @JsonProperty
    private String errmsg;

    private KeycloakResponseParams() {
    }

    public KeycloakResponseParams(KeycloakResponseStatus status, String err, String errmsg) {
        this.resmsgid = UUID.randomUUID().toString();
        this.status = status;
        this.err = err;
        this.errmsg = errmsg;
    }

    public static KeycloakResponseParams successful() {
        return new KeycloakResponseParams(KeycloakResponseStatus.successful, null, null);
    }

    public static KeycloakResponseParams failed(String err, String errmsg) {
        return new KeycloakResponseParams(KeycloakResponseStatus.failed, err, errmsg);
    }

    public KeycloakResponseStatus status() {
        return status;
    }

    @Override
    public String toString() {
        return "ResponseParams{" +
                "resmsgid='" + resmsgid + '\'' +
                ", status=" + status +
                ", err='" + err + '\'' +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }
}
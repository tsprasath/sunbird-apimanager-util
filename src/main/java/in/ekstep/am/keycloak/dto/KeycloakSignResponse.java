package in.ekstep.am.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KeycloakSignResponse {
    @JsonProperty
    private String id;
    @JsonProperty
    private String ver;
    @JsonProperty
    private long ts;
    @JsonProperty
    private KeycloakResponseParams params;
    @JsonProperty
    private KeycloakResponseCode responseCode;
    @JsonProperty
    private KeycloakSignResult result;

    private KeycloakSignResponse(){}

    public KeycloakSignResponse(String id, String ver, long ts, KeycloakResponseParams params, KeycloakResponseCode responseCode, KeycloakSignResult result) {
        this.id = id;
        this.ver = ver;
        this.ts = ts;
        this.params = params;
        this.responseCode = responseCode;
        this.result = result;
    }

    @Override
    public String toString() {
        return "CreateKeycloakRefreshResponse{" +
                "id='" + id + '\'' +
                ", ver='" + ver + '\'' +
                ", ts=" + ts +
                ", params=" + params +
                ", responseCode=" + responseCode +
                ", result=" + result +
                '}';
    }

    public boolean successful() {
        return params.status() == KeycloakResponseStatus.successful;
    }
}

package in.ekstep.am.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmAdminApiHealthResponse {
  private Integer responseCode;
  private String errorMessage;

  public AmAdminApiHealthResponse(Integer responseCode) {
    this(responseCode, null);
  }

  public AmAdminApiHealthResponse(Integer responseCode, String errorMessage) {
    this.responseCode = responseCode;
    this.errorMessage = errorMessage;
  }

  public Integer code() {
    return responseCode;
  }

  public String errorMessage() {
    return errorMessage;
  }
}

package in.ekstep.am.external;

public class AmResponse {
  private int code;
  private String body;

  public AmResponse(int code, String body) {
    this.code = code;
    this.body = body;
  }

  public int code() {
    return code;
  }

  public String body() {
    return body;
  }
}

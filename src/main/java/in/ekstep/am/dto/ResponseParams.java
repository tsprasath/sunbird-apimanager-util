package in.ekstep.am.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseParams {
  @JsonProperty
  private ResponseStatus status;
  @JsonProperty
  private String err;
  @JsonProperty
  private String errmsg;
  @JsonProperty
  private String msgid;
  @JsonProperty
  private String resmsgid;

  private ResponseParams() {
  }

  public ResponseParams(ResponseStatus status, String err, String errmsg, String msgid) {
    this.status = status;
    this.err = err;
    this.errmsg = errmsg;
    this.msgid = msgid;
    this.resmsgid = UUID.randomUUID().toString();
  }

  public static ResponseParams successful(String msgid) {
    return new ResponseParams(ResponseStatus.successful, null, null, msgid);
  }

  public static ResponseParams failed(String msgid, String err, String errmsg) {
    return new ResponseParams(ResponseStatus.failed, err, errmsg, msgid);
  }

  public ResponseStatus status() {
    return status;
  }

  @Override
  public String toString() {
    return "ResponseParams{" +
        "status=" + status +
        ", err='" + err + '\'' +
        ", errmsg='" + errmsg + '\'' +
        ", msgid='" + msgid + '\'' +
        ", resmsgid='" + resmsgid + '\'' +
        '}';
  }
}

package in.ekstep.am.step;

import in.ekstep.am.builder.CredentialDetails;
import in.ekstep.am.jwt.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

public class SignCredentialWithKeyStep implements Step {

  private final Logger log = LoggerFactory.getLogger(this.getClass());
  private String userName;
  private CredentialDetails responseBuilder;
  private String key;
  private PrivateKey secretKey;
  private String keyId;

  SignCredentialWithKeyStep(String userName, CredentialDetails responseBuilder, String key, PrivateKey secretKey, String keyId) {
    this.userName = userName;
    this.responseBuilder = responseBuilder;
    this.key = key;
    this.secretKey = secretKey;
    this.keyId = keyId;
  }

  @Override
  public void execute() throws Exception {
     responseBuilder.setKey(key);
     String issuerStr = keyId + "-" + key + "-" + System.currentTimeMillis();
     responseBuilder.setToken(JWTUtil.createRS256Token(issuerStr, secretKey, createHeaderOptions()));
  }

  private Map<String, String> createHeaderOptions() {
    Map<String, String> headers = new HashMap<>();
    headers.put("kid", keyId);
    return headers;
  }
}

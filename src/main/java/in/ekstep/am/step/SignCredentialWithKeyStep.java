package in.ekstep.am.step;

import in.ekstep.am.builder.CredentialDetails;
import in.ekstep.am.jwt.JWTUtil;
import in.ekstep.am.jwt.KeyData;
import in.ekstep.am.jwt.KeyManager;
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
  private KeyManager keyManager;

  SignCredentialWithKeyStep(String userName, CredentialDetails responseBuilder, String key, KeyManager keyManager) {
    this.userName = userName;
    this.responseBuilder = responseBuilder;
    this.key = key;
    this.keyManager = keyManager;
  }

  @Override
  public void execute() throws Exception {
     responseBuilder.setKey(key);
     KeyData keyData = keyManager.getRandomKey();
     responseBuilder.setToken(JWTUtil.createRS256Token(key, keyData.getPrivateKey(), createHeaderOptions(keyData.getKeyId())));
  }

  private Map<String, String> createHeaderOptions(String keyId) {
    Map<String, String> headers = new HashMap<>();
    headers.put("kid", keyId);
    return headers;
  }
}

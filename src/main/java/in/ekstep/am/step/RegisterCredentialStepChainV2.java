package in.ekstep.am.step;

import in.ekstep.am.builder.RegisterCredentialResponseBuilder;
import in.ekstep.am.dto.credential.RegisterCredentialRequest;
import in.ekstep.am.external.AmAdminApi;
import in.ekstep.am.jwt.Base64Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

@Component
public class RegisterCredentialStepChainV2 {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private Map<Integer, PrivateKey> keyMap = new HashMap<Integer, PrivateKey>();

  @Autowired
  private Environment environment;

  private String basePath;
  private String keyPrefix;
  private int keyCount = 0;

  @PostConstruct
  public void init() throws Exception {
    basePath = environment.getProperty("am.admin.api.jwt.basepath");
    keyPrefix = environment.getProperty("am.admin.api.jwt.keyprefix");
    keyCount = Integer.parseInt(environment.getProperty("am.admin.api.jwt.keycount"));

    for(int i = 0; i < keyCount; i++) {
      log.info("Private key loaded - " + basePath + keyPrefix + i);
      keyMap.put(i, getPrivateKey(basePath + keyPrefix + i));
    }
  }

  private PrivateKey getPrivateKey(String path) throws Exception {
    FileInputStream in = new FileInputStream(path);
    byte[] keyBytes = new byte[in.available()];
    in.read(keyBytes);
    in.close();

    String privateKey = new String(keyBytes, "UTF-8");
    privateKey = privateKey
            .replaceAll("(-+BEGIN RSA PRIVATE KEY-+\\r?\\n|-+END RSA PRIVATE KEY-+\\r?\\n?)", "")
            .replaceAll("(-+BEGIN PRIVATE KEY-+\\r?\\n|-+END PRIVATE KEY-+\\r?\\n?)", "");

    keyBytes = Base64Util.decode(privateKey.getBytes("UTF-8"), Base64Util.DEFAULT);

    // generate private key
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return keyFactory.generatePrivate(spec);
  }

  public void execute(String userName, RegisterCredentialRequest request, RegisterCredentialResponseBuilder responseBuilder) throws Exception {
    for (Step step : stepChain(userName, request, responseBuilder)) {
      if (responseBuilder.successful()) {
        step.execute();
      }
    }
  }

  private List<Step> stepChain(String userName, RegisterCredentialRequest request, RegisterCredentialResponseBuilder responseBuilder) {
    int keyId = (int) (Math.random() * keyCount);
    SignCredentialWithKeyStep createCredentialWithKeyStep = new SignCredentialWithKeyStep(
            userName, responseBuilder, request.key(), keyMap.get(keyId), keyPrefix + keyId);
    return asList(createCredentialWithKeyStep);
  }
}

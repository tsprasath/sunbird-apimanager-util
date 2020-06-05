package in.ekstep.am.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

@Component
public class KeyManager {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Map<String, KeyData> keyMap = new HashMap<String, KeyData>();

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
            String keyId = keyPrefix + i;
            keyMap.put(keyId, new KeyData(
                    keyId, getPrivateKey(basePath + keyId), null));
        }
    }

    public KeyData getRandomKey() {
        int keyId = (int) (Math.random() * keyCount);
        return keyMap.get(keyPrefix + keyId);
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

}

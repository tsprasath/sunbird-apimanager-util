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
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class KeyManager {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String SEPARATOR = "_";
    private static Map<String, KeyData> keyMap = new HashMap<String, KeyData>();
    private Map<String, String> keyMetadata = new HashMap<>();

    @Autowired
    private Environment environment;

    @PostConstruct
    public void init() throws Exception {
        String keys[] = environment.getProperty("am.admin.api.keys").split(",");

        for (String key :keys) {
            loadKeys(key);
        }

        loadTokenEnvPublicKey();
    }

    private void loadKeys(String keyName) throws Exception {
        String basePath = environment.getProperty("am.admin.api."+ keyName + ".basepath");
        String keyPrefix = environment.getProperty("am.admin.api."+ keyName + ".keyprefix");
        int keyCount = Integer.parseInt(environment.getProperty("am.admin.api."+ keyName + ".keycount"));
        int keyStart = Integer.parseInt(environment.getProperty("am.admin.api."+ keyName + ".keystart"));
        keyMetadata.put(keyName + SEPARATOR + "keyCount", String.valueOf(keyCount));
        keyMetadata.put(keyName + SEPARATOR + "keyStart", String.valueOf(keyStart));
        keyMetadata.put(keyName + SEPARATOR + "keyPrefix", String.valueOf(keyPrefix));
        keyMetadata.put(keyName + SEPARATOR + "basePath", String.valueOf(basePath));

        for(int i = keyStart; i < (keyStart + keyCount); i++) {
            log.info("Private key loaded - " + basePath + keyPrefix + i);
            String keyId = keyPrefix + i;
/*
            // Code block for loading access public keys in case we want to verify tokens signed by us
            if(keyPrefix.equals("access")) {
                keyMap.put(keyId, new KeyData(keyId, loadPrivateKey(basePath + keyId),
                        loadPublicKey(environment.getProperty("am.admin.api.accesspublic.basepath") +
                                environment.getProperty("am.admin.api.accesspublic.keyprefix") + i)));
            }
            else
*/
                keyMap.put(keyId, new KeyData(keyId, loadPrivateKey(basePath + keyId), null));
        }

    }

    private void loadTokenEnvPublicKey() throws Exception {
        String basePath = environment.getProperty("token.public.basepath");
        String keyPrefix = environment.getProperty("token.public.keyprefix");
        String keyId = environment.getProperty("token.kid");
        keyMap.put("token.kid", new KeyData(environment.getProperty("token.kid")));
        keyMap.put("token.validity", new KeyData(environment.getProperty("token.validity")));
        keyMap.put("token.domain", new KeyData(environment.getProperty("token.domain")));
        keyMap.put("token.offline.vadity", new KeyData(environment.getProperty("token.offline.vadity")));
        keyMap.put(keyId, new KeyData(keyId, loadPublicKey(basePath + keyPrefix)));
        log.info("Public key loaded - " + basePath + keyPrefix);
    }

    public KeyData getRandomKey(String keyName) {
        int keyCount = Integer.parseInt(keyMetadata.get(keyName + SEPARATOR + "keyCount"));
        int keyStart = Integer.parseInt(keyMetadata.get(keyName + SEPARATOR + "keyStart"));
        String keyPrefix = keyMetadata.get(keyName + SEPARATOR + "keyPrefix");
        int randomKeyId = (int) (Math.random() * keyCount);
        int keyId = keyStart + randomKeyId;
        return keyMap.get(keyPrefix + keyId);
    }

    private PrivateKey loadPrivateKey(String path) throws Exception {
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

    private PublicKey loadPublicKey(String path) throws Exception {
        FileInputStream in = new FileInputStream(path);
        byte[] keyBytes = new byte[in.available()];
        in.read(keyBytes);
        in.close();

        String publicKey = new String(keyBytes, "UTF-8");
        publicKey = publicKey
                .replaceAll("(-+BEGIN RSA PUBLIC KEY-+\\r?\\n|-+END RSA PUBLIC KEY-+\\r?\\n?)", "")
                .replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "");
        byte[] publicBytes = Base64.getMimeDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        log.info("Public key loaded - " + path);
        return keyFactory.generatePublic(keySpec);
    }

    public static KeyData getValueUsingKey(String keyId) {
        return keyMap.get(keyId);
    }
}

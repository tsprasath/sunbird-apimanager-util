package in.ekstep.am.jwt;

import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyData {
    private String keyId;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public KeyData(String keyId, PrivateKey privateKey, PublicKey publicKey) {
        this.keyId = keyId;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public KeyData(String keyId, PublicKey publicKey) {
        this.keyId = keyId;
        this.publicKey = publicKey;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}

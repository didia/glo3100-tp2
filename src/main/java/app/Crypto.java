package app;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class Crypto {

    public String h1(String message) {
	return Base64.encodeBase64String(DigestUtils.sha1(message));
    }

    public String h2(String message) {
	return Base64.encodeBase64String(DigestUtils.md5(message));
    }

    public String random5Digits() {
	return new Integer(ThreadLocalRandom.current().nextInt(0, 100000)).toString();
    }

    public String encodeBase64(byte[] binaryData) {
	return Base64.encodeBase64String(binaryData);
    }

    public byte[] decodeBase64(String message) {
	return Base64.decodeBase64(message);
    }

    public KeyPair generateKey() throws NoSuchAlgorithmException {
	KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
	kpg.initialize(1024);
	KeyPair kp = kpg.genKeyPair();
	return kp;
    }

    public String signeH1RSA(String message, String privateKey)
	    throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, SignatureException {
	Signature sig = Signature.getInstance("SHA1WithRSA");

	sig.initSign(getPrivateKeyFromString(privateKey));
	sig.update(message.getBytes());
	byte[] signatureBytes = sig.sign();
	return encodeBase64(signatureBytes);
    }

    private PrivateKey getPrivateKeyFromString(String privateKey)
	    throws NoSuchAlgorithmException, InvalidKeySpecException {
	PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodeBase64(privateKey));
	KeyFactory fact = KeyFactory.getInstance("RSA");
	PrivateKey priv = fact.generatePrivate(keySpec);
	return priv;
    }

    public boolean verifyH1RSA(String message, String publicKey, String signature)
	    throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, SignatureException {
	Signature sig = Signature.getInstance("SHA1WithRSA");
	sig.initVerify(getPublicKeyFromString(publicKey));
	sig.update(message.getBytes());
	return sig.verify(decodeBase64(signature));
    }

    private PublicKey getPublicKeyFromString(String publicKey)
	    throws NoSuchAlgorithmException, InvalidKeySpecException {
	X509EncodedKeySpec spec = new X509EncodedKeySpec(decodeBase64(publicKey));
	KeyFactory fact = KeyFactory.getInstance("RSA");
	return fact.generatePublic(spec);
    }
}

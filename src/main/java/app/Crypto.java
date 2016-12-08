package app;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class Crypto {

    public String h1(String message) {
	return Base64.encodeBase64String(DigestUtils.sha1(message));
    }

    public String random5Digits() {
	return new Integer(ThreadLocalRandom.current().nextInt(0, 100000)).toString();
    }

}

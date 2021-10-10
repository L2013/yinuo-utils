package com.yinuo.utils.security;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * DESede Coder
 */
public class ThreeDESCodec {
	private static final String KEY_ALGORITHM = "DESede";
	private static final String DEFAULT_CIPHER_ALGORITHM = "DESede/ECB/PKCS5Padding";

	private static Key toKey(byte[] key) throws Exception {
		DESedeKeySpec dks = new DESedeKeySpec(key);
		SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		SecretKey secretKey = skf.generateSecret(dks);
		return secretKey;
	}

	private static byte[] encrypt(byte[] data, Key key) throws Exception {
		return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
	}

	private static byte[] encrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception {
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(data);
	}

	private static byte[] decrypt(byte[] data, Key key) throws Exception {
		return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
	}

	private static byte[] decrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception {
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(data);
	}

	public static String encryptData(String srcData, String secretKey) throws CodecException {
		try {
			Key k = toKey(secretKey.getBytes());
			return Base64.encodeBase64String(encrypt(srcData.getBytes(), k));
		} catch (Exception e) {
			throw new CodecException(e.toString());
		}
	}

	public static String decryptData(String encryptedData, String secretKey) throws CodecException {
		try {
			Key k = toKey(secretKey.getBytes());
			return new String(decrypt(Base64.decodeBase64(encryptedData), k));
		} catch (Exception e) {
			throw new CodecException(e.toString());
		}
	}
}

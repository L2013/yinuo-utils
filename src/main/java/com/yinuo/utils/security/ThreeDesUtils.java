package com.yinuo.utils.security;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class ThreeDesUtils {

	private static final String Algorithm = "DESede";

	public static final String ALGORITHM_DES = "DESede/CBC/PKCS5Padding";

	// 加解密统一使用的编码方式
	private final static String ENCODING = "utf-8";

	public static String encrypt(String data, String keystr, String iv) throws Exception {
		DESedeKeySpec spec = new DESedeKeySpec(keystr.getBytes(ENCODING));
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(Algorithm);
		Key deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
		byte[] encryptData = cipher.doFinal(data.getBytes());
		return Base64.getEncoder().encodeToString(encryptData);
	}

	public static String decrypt(String data, String keystr, String iv) throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(keystr.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(Algorithm);
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
		byte[] decryptData = cipher.doFinal(Base64.getDecoder().decode(data));
		return new String(decryptData, ENCODING);
	}

	public static void main(String[] args) throws Exception {
		System.out.println(ThreeDesUtils.encrypt("cc", "123456781234567812345678", "01234567"));
		System.out.println(ThreeDesUtils.decrypt("ek41datqdxo=", "123456781234567812345678", "01234567"));
	}
}

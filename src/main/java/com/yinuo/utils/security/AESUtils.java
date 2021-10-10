package com.yinuo.utils.security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtils {
	private static final String AES = "AES";
	private static final String ENCODING = "utf-8";
	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

	public static String encrypt(String data, String skey, String iv) throws Exception {
		byte[] enCodeFormat = genkey(skey);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		SecretKeySpec skeySpec = new SecretKeySpec(enCodeFormat, AES);
		// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
		IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);
		byte[] encrypted = cipher.doFinal(data.getBytes(ENCODING));
		// 此处使用BASE64做转码。
		return Base64.getEncoder().encodeToString(encrypted);
	}

	public static String decrypt(String data, String skey, String iv) throws Exception {
		byte[] enCodeFormat = genkey(skey);
		SecretKeySpec skeySpec = new SecretKeySpec(enCodeFormat, AES);

		Cipher cipher = Cipher.getInstance(ALGORITHM);
		IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);
		// 先用base64解密
		byte[] ciphertxt = Base64.getDecoder().decode(data);
		byte[] original = cipher.doFinal(ciphertxt);
		String originalString = new String(original, ENCODING);
		return originalString;
	}

	private static byte[] genkey(String skey) throws NoSuchAlgorithmException {
		KeyGenerator kgen = KeyGenerator.getInstance(AES);// 创建AES的Key生产者
		// 利用用户密码作为随机数初始化出
		// 128位的key生产者
		// 加密没关系，SecureRandom是生成安全随机数序列，password.getBytes()是种子，
		// 只要种子相同，序列就一样，所以解密只要有password就行
		kgen.init(128, new SecureRandom(skey.getBytes()));
		// 根据用户密码，生成一个密钥
		SecretKey secretKey = kgen.generateKey();
		// 返回基本编码格式的密钥，如果此密钥不支持编码，则返回null
		byte[] enCodeFormat = secretKey.getEncoded();
		return enCodeFormat;
	}

	public static void main(String[] args) throws Exception {
		String key = "1234567890123456";
		String iv = "1234567890123456";
		String cipher = AESUtils.encrypt("1234567890123456", key, iv);
		System.out.println(cipher);
		System.out.println(AESUtils.decrypt(cipher, key, iv));
	}
}
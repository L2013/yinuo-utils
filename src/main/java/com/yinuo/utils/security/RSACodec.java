package com.yinuo.utils.security;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSACodec {
	private static final String UTF_8 = "UTF-8";

	private RSACodec() {
	}

	/**
	 * 签名验证
	 */
	public static boolean verifySignature(String originData, String cipher, PublicKey publicKey) {
		// 获取客户端传递的经私钥加密的签名,并用公钥解密
		String decodeSha256 = decrypt(cipher, publicKey);
		// 获取原始数据签名
		String sha256 = sha256xHexStr(originData);
		return sha256.equals(decodeSha256);
	}

	public static RSAPrivateKey getPrivateKeyInPem(String pemFile) {
		return getPrivateKeyInStr(getPemString(pemFile));
	}

	public static PublicKey getPublicKeyInPem(String pemFile) {
		return getPublicKeyInStr(getPemString(pemFile));
	}

	public static String getPemString(String pemFile) {
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(pemFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.startsWith("-----")) {
					sb.append(line);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 通过证书路径初始化为公钥证书
	 * 
	 * @param cerFile
	 * @return @throws
	 */
	public static PublicKey getPublicKeyInCer(String cerFile) {
		X509Certificate x509 = null;
		try (FileInputStream in = new FileInputStream(cerFile)) {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
			x509 = (X509Certificate) cf.generateCertificate(in);
		} catch (CertificateException e) {
			System.out.println("InitCert Error");
		} catch (FileNotFoundException e) {
			System.out.println("InitCert Error File Not Found");
		} catch (NoSuchProviderException e) {
			System.out.println("LoadVerifyCert Error No BC Provider");
		} catch (IOException e) {
			System.out.println("IOException");
		}
		return x509 == null ? null : x509.getPublicKey();
	}

	public static PublicKey getPublicKeyInStr(String publicKeyString) {
		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
			X509EncodedKeySpec keySpec2 = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyString));
			return keyFactory.generatePublic(keySpec2);
		} catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static RSAPrivateKey getPrivateKeyInStr(String privateKeyString) {
		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyString));
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 私钥加密数据
	 * 
	 * @param dataString
	 * @param privateKey
	 * @return
	 */
	public static String encrypt(String dataString, PrivateKey privateKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] cipherBytes = cipher.doFinal(dataString.getBytes(UTF_8));
			byte[] base64Bytes = Base64.encodeBase64(cipherBytes);
			return new String(base64Bytes, UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 公钥验签操作
	 * 
	 * @param dataString
	 * @param publicKey
	 * @return
	 */
	public static String decrypt(String dataString, PublicKey publicKey) {
		try {
			byte[] base64Bytes = Base64.decodeBase64(dataString.getBytes(UTF_8));
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			base64Bytes = cipher.doFinal(base64Bytes);
			return new String(base64Bytes, UTF_8);
		} catch (Exception e) {
			return "";
		}
	}
	
	
	/**
	 * 私钥加密数据
	 * 
	 * @param dataString
	 * @param publicKey
	 * @return
	 */
	public static String encrypt(String dataString, PublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] cipherBytes = cipher.doFinal(dataString.getBytes(UTF_8));
			byte[] base64Bytes = Base64.encodeBase64(cipherBytes);
			return new String(base64Bytes, UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 公钥验签操作
	 * 
	 * @param dataString
	 * @param privateKey
	 * @return
	 */
	public static String decrypt(String dataString, PrivateKey privateKey) {
		try {
			byte[] base64Bytes = Base64.decodeBase64(dataString.getBytes(UTF_8));
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			base64Bytes = cipher.doFinal(base64Bytes);
			return new String(base64Bytes, UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 导出私钥
	public static String stringPrivateKey(PrivateKey privateKey) {
		String encoded = Base64.encodeBase64String(privateKey.getEncoded());
		StringBuilder sb = new StringBuilder();
		sb.append("-----Begin Private Key-----\r\n");// 非必须
		sb.append(encoded);
		sb.append("\r\n-----End Private Key-----");// 非必须
		return sb.toString();
	}

	// 导出公钥
	public static String stringPublicKey(PublicKey publicKey) {
		String encoded = Base64.encodeBase64String(publicKey.getEncoded());
		StringBuilder sb = new StringBuilder();
		sb.append("-----Begin Public Key-----\r\n");// 非必须
		sb.append(encoded);
		sb.append("\r\n-----End Public Key-----");// 非必须
		return sb.toString();
	}

	/**
	 * 签名哈希算法<br/>
	 * SHA256计算后进行16进制转换<br/>
	 * 用DigestUtils.sha256Hex是一样的<br/>
	 * 
	 * @param data
	 * @return
	 */
	public static String sha256xHexStr(String data) {
		byte[] bytes = sha256(data.getBytes());
		StringBuilder sha256StrBuff = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
				sha256StrBuff.append("0").append(Integer.toHexString(0xFF & bytes[i]));
			} else {
				sha256StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
			}
		}
		return sha256StrBuff.toString();
	}

	private static byte[] sha256(byte[] data) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.reset();
			md.update(data);
			return md.digest();
		} catch (Exception e) {
			System.out.println("SHA256计算失败");
			return null;
		}
	}
}

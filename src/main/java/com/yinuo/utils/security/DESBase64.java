package com.yinuo.utils.security;


import com.yinuo.utils.toolbox.BytesKit;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class DESBase64 {
    private static final String Algorithm = "DESede";
    private static final String CipherMode = "DESede/ECB/PKCS5Padding";


    public static String encodeKey(String value, String key) throws UnsupportedEncodingException {
        if (value == null || "".equals(value)) {
            return "";
        }
        byte[] s1 = encode3Des(value.getBytes(), BytesKit.hexToBytes(key));
        if (s1.length == 0) {
			return "";
		}
        String res = new String(Base64.getEncoder().encode(s1), "UTF-8");
        return res;
    }

    public static byte[] encode3Des(byte[] input, byte[] key) throws UnsupportedEncodingException {
        try {
            SecretKey deskey = new SecretKeySpec(key, Algorithm);
            Cipher c1 = Cipher.getInstance(CipherMode);
            c1.init(Cipher.ENCRYPT_MODE, deskey); // 初始化为加密模式
            return c1.doFinal(input);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt3DES(byte[] input, byte[] key) throws Exception {
        Cipher c = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "DESede"));
        return c.doFinal(input);
    }

    public static byte[] encrypt3DES(byte[] input, byte[] key) throws Exception {
        Cipher c = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "DESede"));
        return c.doFinal(input);
    }

    public static String getDecryptedValue(String value, String key) throws Exception {
        if (null == value || "".equals(value)) {
            return "";
        }
        byte[] valueByte = Base64.getDecoder().decode(value);
        byte[] sl = decrypt3DES(valueByte, BytesKit.hexToBytes(key));
        String result = new String(sl);
        return result;
    }

    public static String getEncryptedValue(String value, String key) throws Exception {
        if (null == value || "".equals(value)) {
            return "";
        }
        byte[] sl = encrypt3DES(value.getBytes(), BytesKit.hexToBytes(key));
        byte[] valueByte = Base64.getEncoder().encode(sl);
        String result = new String(valueByte);
        return result;
    }

}

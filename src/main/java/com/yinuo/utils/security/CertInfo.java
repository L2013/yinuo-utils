package com.yinuo.utils.security;

import lombok.Data;
import sun.misc.BASE64Encoder;

import java.security.PublicKey;
import java.util.Date;

/**
 * @author liang
 * @create 2020-05-20 16:12
 */
@Data
public class CertInfo {
    private String ver;
    private String certId;
    private String certIdHex;
    private Date effectDate;
    private Date expiryDate;
    private String owner;
    private String issuer;
    private String alg;
    private String finger;
    private PublicKey publicKey;

    public String getPublicKeyStr() {
        //获取公钥对象
        PublicKey publicKey = this.getPublicKey();
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String publicKeyString = base64Encoder.encode(publicKey.getEncoded());
        String result = publicKeyString.replace("\r\n", "");
        System.out.println("-----------------公钥--------------------");
        System.out.println(result);
        System.out.println("-----------------公钥--------------------");
        return result;
    }
}

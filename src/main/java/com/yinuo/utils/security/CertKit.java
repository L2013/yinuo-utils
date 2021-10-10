package com.yinuo.utils.security;

import org.apache.commons.codec.digest.DigestUtils;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * @author liang
 * @create 2020-05-20 16:10
 */
public class CertKit {
    public static CertInfo getCertInfoFromString(String certString) throws Exception {
        // Base64解码
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] byteCert = decoder.decodeBuffer(certString);
        //转换成二进制流
        ByteArrayInputStream bain = new ByteArrayInputStream(byteCert);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate oCer = (X509Certificate) cf.generateCertificate(bain);
        return parseCertInfo(oCer);
    }

    public static CertInfo getCertInfoFromCerFile(String cerPath) throws Exception {
        CertInfo cer = new CertInfo();

        File file = new File(cerPath);
        InputStream ins = new FileInputStream(file);
        //创建x.509工厂类
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        //创建证书实例
        X509Certificate oCer = (X509Certificate) cf.generateCertificate(ins);

        ins.close();
        return parseCertInfo(oCer);

    }

    private static CertInfo parseCertInfo(X509Certificate oCer) throws CertificateEncodingException {
        CertInfo cer = new CertInfo();

        //证书版本
        cer.setVer(String.valueOf(oCer.getVersion()));

        // 获得证书序列号
        cer.setCertId(oCer.getSerialNumber().toString(10));
        cer.setCertIdHex(oCer.getSerialNumber().toString(16));

        // 获得证书有效期
        cer.setEffectDate(oCer.getNotBefore());
        cer.setExpiryDate(oCer.getNotAfter());

        // 获得证书主体信息
        cer.setOwner(oCer.getSubjectDN().getName());

        // 获得证书颁发者信息
        cer.setIssuer(oCer.getIssuerDN().getName());

        // 获得证书签名算法名称
        cer.setAlg(oCer.getSigAlgName());
        //证书指纹信息
        cer.setFinger(DigestUtils.sha1Hex(oCer.getEncoded()));
        //证书公钥信息
        cer.setPublicKey(oCer.getPublicKey());

        return cer;
    }
}

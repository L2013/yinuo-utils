package com.yinuo.utils.okhttp;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * @author liang
 * @deprecated 推荐使用cn.hutool.http.HttpUtil
 */
public class TrustAllCerts implements X509TrustManager {
    @Override  
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}  
  
    @Override  
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}  
  
    @Override  
    public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}  
}  
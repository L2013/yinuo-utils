package com.yinuo.utils.okhttp;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liang
 * @create 2020-06-29 19:53
 */
public class OkHttpSupport {
    public static Map<String, String> genUAHeader() {
        Map<String, String> headerMap = new HashMap<>();
        String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36";
        headerMap.put("User-Agent", ua);
        return headerMap;
    }

    public static SSLSocketFactory createSSLSocketFactory() {
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            return sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

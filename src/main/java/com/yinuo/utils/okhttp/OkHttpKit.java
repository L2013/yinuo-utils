package com.yinuo.utils.okhttp;

import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

/**
 * @author liang
 * @create 2020-06-29 19:56
 * @deprecated 推荐使用cn.hutool.http.HttpUtil
 */
public class OkHttpKit {

    private static final Map<String, String> UA_HEADER = OkHttpSupport.genUAHeader();

    private static boolean isSsl(String url) {
        return url != null && url.toLowerCase().startsWith("https");
    }

    public static String text(Response response) throws IOException {
        return response.body().string();
    }

    public static Response get(ProxyConfigurer proxy, String url) throws IOException {
        return get(proxy, url, OkHttpSupport.genUAHeader());
    }

    public static Response get( ProxyConfigurer proxy,String url, Map<String, String> headers) throws IOException{
        return OkHttpUtils.build(isSsl(url), OkHttpUtils.DEFAULT_READ_TIMEOUT, proxy).get(url, headers);
    }

    public static Response postJson(ProxyConfigurer proxy, String url,String json) throws IOException{
        return OkHttpUtils.build(isSsl(url), OkHttpUtils.DEFAULT_READ_TIMEOUT, proxy).postJson(url, json, UA_HEADER);
    }

    public static Response postJson(ProxyConfigurer proxy, String url, Map<String, String> headers,String json) throws IOException{
        return OkHttpUtils.build(isSsl(url), OkHttpUtils.DEFAULT_READ_TIMEOUT, proxy).postJson(url, json, headers);
    }

    public static Response postXml(ProxyConfigurer proxy, String url,String xml) throws IOException{
        return OkHttpUtils.build(isSsl(url), OkHttpUtils.DEFAULT_READ_TIMEOUT, proxy).postXml(url, xml, UA_HEADER);
    }

    public static Response postXml(ProxyConfigurer proxy, String url, Map<String, String> headers,String xml) throws IOException{
        return OkHttpUtils.build(isSsl(url), OkHttpUtils.DEFAULT_READ_TIMEOUT, proxy).postXml(url, xml, headers);
    }

    public static Response postForm(ProxyConfigurer proxy, String url,Map<String, String> params) throws IOException{
        return OkHttpUtils.build(isSsl(url), OkHttpUtils.DEFAULT_READ_TIMEOUT, proxy).postForm(url, params, UA_HEADER);
    }

    public static Response postForm(ProxyConfigurer proxy, String url, Map<String, String> headers,Map<String, String> params) throws IOException{
        return OkHttpUtils.build(isSsl(url), OkHttpUtils.DEFAULT_READ_TIMEOUT, proxy).postForm(url, params, headers);
    }

    public static Response postFileForm(ProxyConfigurer proxy, String url, Map<String, String> headers,Map<String, Object> params) throws IOException{
        return OkHttpUtils.build(isSsl(url), OkHttpUtils.DEFAULT_READ_TIMEOUT, proxy).postFormWithFile(url, headers, params);
    }
}

package com.yinuo.utils.okhttp;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 针对应用为简化雷同代码对OkHttp做的浅层封装
 * 
 * @author liangliang
 *
 */
public class OkHttpUtils {
	private OkHttpClient okHttpClient;

	public static final int DEFAULT_READ_TIMEOUT = 30;

	public static final MediaType JSON = MediaType.parse("application/json;charset=UTF-8");
	public static final MediaType XML = MediaType.parse("text/xml;charset=UTF-8");

	private OkHttpUtils(OkHttpClient okHttpClient) {
		this.okHttpClient = okHttpClient;
	}

	public static OkHttpUtils build(){
		return build(false, DEFAULT_READ_TIMEOUT, null);
	}

	public static OkHttpUtils build(ProxyConfigurer proxyConfigurer){
		return build(false, DEFAULT_READ_TIMEOUT, proxyConfigurer);
	}

	public static OkHttpUtils build(boolean ssl, int readTimeout, ProxyConfigurer proxyConfigurer){
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		// 弱化证书校验
		if (ssl) {
			builder.sslSocketFactory(OkHttpSupport.createSSLSocketFactory());
			builder.hostnameVerifier(new TrustAllHostnameVerifier());
		}
		System.out.println("using weak ssl:" + ssl);

		// 代理设置
		if(proxyConfigurer !=null && proxyConfigurer.isEnable()){
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyConfigurer.getHost(), proxyConfigurer.getPort()));
			builder.proxy(proxy);
			System.out.println("using proxyConfigurer:" + proxy) ;
		}
		OkHttpClient okHttpClient = builder.readTimeout(readTimeout, TimeUnit.SECONDS).addInterceptor(OkHttpUtils.getLogInterceptor()).build();
		return new OkHttpUtils(okHttpClient);
	}

	private static HttpLoggingInterceptor getLogInterceptor() {
		HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
		logInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
		return logInterceptor;
	}

	private FormBody form(Map<String, String> m) {
		Builder builder = new Builder();
		for (String key : m.keySet()) {
			builder.add(key, m.get(key));
		}
		return builder.build();
	}

	public Response get(String url) throws IOException {
		return get(url, null);
	}
	
	public Response get(String url, Map<String, String> headers) throws IOException {
		return call(url, headers);
	}


	private Response call(String url, Map<String, String> headers,MediaType mediaType,boolean post) throws IOException {
		return call(url, headers, null, false);
	}

	private Request request(String url, Map<String, String> headers){
		Request.Builder builder = new Request.Builder();
		if (headers != null) {
			builder.headers(Headers.of(headers));
		}
		return builder.url(url).build();
	}

	private Request request(String url, Map<String, String> headers,MediaType mediaType,String body){
		Request.Builder builder = new Request.Builder();
		if (headers != null) {
			builder.headers(Headers.of(headers));
		}
		RequestBody requestBody = RequestBody.create(mediaType, body);
		return builder.url(url).post(requestBody).build();
	}

	private Response call(String url, Map<String, String> headers,MediaType mediaType,String body) throws IOException {
		return okHttpClient.newCall(request(url, headers, mediaType, body)).execute();
	}

	private Response call(String url, Map<String, String> headers) throws IOException {
		return okHttpClient.newCall(request(url,headers)).execute();
	}

	public Response postJson(String url, String body, Map<String, String> headers) throws IOException {
		return call(url,headers,JSON,body);
	}

	public Response postXml(String url, String xml, Map<String, String> headers) throws IOException {
		return call(url,headers,XML,xml);
	}

	public Response postForm(String url, Map<String, String> params, Map<String, String> headers)
			throws IOException {
		Request.Builder builder = new Request.Builder();
		for (Entry<String, String> e : headers.entrySet()) {
			builder.addHeader(e.getKey(), e.getValue());
		}
		Request request = builder.url(url).post(form(params)).build();
		return okHttpClient.newCall(request).execute();
	}

	public Response postFormWithFile(String url, Map<String, String> headers, Map<String, Object> params )
			throws IOException {
		Request.Builder builder = new Request.Builder();
		if (headers != null) {
			builder.headers(Headers.of(headers));
		}
		MultipartBody.Builder multiBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
		for (String key : params.keySet()) {
			Object param = params.get(key);
			if (param instanceof File) {
				RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), (File) param);
				multiBuilder.addFormDataPart(key, ((File) param).getName(), fileBody);
			} else {
				multiBuilder.addFormDataPart(key, (String) param);
			}
		}
		RequestBody requestBody = multiBuilder.build();
		Request request = builder.url(url).post(requestBody).build();
		return okHttpClient.newCall(request).execute();
	}


}

package com.yinuo.utils.okhttp;

/**
 * @author liang
 * @create 2020-06-29 18:38
 * @deprecated 推荐使用cn.hutool.http.HttpUtil
 */
public class ProxyConfigurer {
    private boolean enable;
    private String host;
    private int port;

    public boolean isEnable() {
        return enable;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public ProxyConfigurer(String enable, String host, String port) {
        try {
            this.enable = Boolean.valueOf(enable);
            this.host = host;
            this.port = Integer.parseInt(port);
        }catch (Exception e){
            e.printStackTrace();
            this.enable = false;
        }
    }

    public ProxyConfigurer(boolean enable, String host, int port) {
        this.enable = enable;
        this.host = host;
        this.port = port;
    }
}

package common.net;

import io.netty.handler.codec.http.HttpRequest;


public class HttpPacket {
    private long userId;
    private String token;
    private int protocol;
    private String data;
    private HttpRequest request;
    private String ip;

    public HttpPacket(long userId, String token, int protocol, String data, String ip, HttpRequest request) {
        this.userId = userId;
        this.token = token;
        this.protocol = protocol;
        this.data = data;
        this.ip = ip;
        this.request = request;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}

package test;

import http.Header;
import http.UserAgent;
import utils.HttpRequest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws IOException {
        String url = "https://ip-api.com/json";
        Map<String,String> headers = new HashMap<String, String>();
        headers.put(Header.USER_AGENT.getValue(), UserAgent.ADGE.getValue());
        headers.put(Header.ACCEPT.getValue(),"text/html");
        Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress("183.234.215.11",8443));
        System.out.println("用代理");
        System.out.println(HttpRequest.doGet(url).setProxy(proxy).setReadTimeout(10000).getResponse());
        System.out.println("不用代理");
        System.out.println(HttpRequest.doGet(url).getResponse());
        System.out.println(HttpRequest.doGet("https://www.haoziy.cn/").getResponse());
        System.out.println(HttpRequest.doGet("https://www.haoziy.cn/").setProxy(proxy).setReadTimeout(30000).getResponse());
    }
}

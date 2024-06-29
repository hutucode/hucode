package test;

import utils.HttpRequest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class Test {
    public static void main(String[] args) throws IOException {
        String url = "http://ip-api.com/json";
        Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress("183.234.215.11",8443));
        System.out.println("用代理");
        System.out.println(HttpRequest.doGet(url).setProxy(proxy).getResponse());
        System.out.println("不用代理");
        System.out.println(HttpRequest.doGet(url).getResponse());
    }
}

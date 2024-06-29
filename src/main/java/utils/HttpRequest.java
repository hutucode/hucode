package utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;

public class HttpRequest {
    private static HttpURLConnection httpURLConnection;
    private static HttpsURLConnection httpsURLConnection;
    private static URL url;



    public static HttpRequest doGet(String baseUrl) {
        try {
            url = new URL(baseUrl);
            if (isHttp(url.getProtocol())) {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
            } else if (isHttps(url.getProtocol())) {
                httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("GET");

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return new HttpRequest();
    }

    public HttpRequest setProxy(Proxy proxy) throws IOException {
        if (isHttp(url.getProtocol())) {
            httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
        } else if (isHttps(url.getProtocol())) {
            httpsURLConnection = (HttpsURLConnection) url.openConnection(proxy);
        } else {
            throw new ProtocolException();
        }
        return new HttpRequest();
    }


    public HttpRequest setReadTimeout(int readTimeOut)  {
        if (isHttp(url.getProtocol())) {
            httpURLConnection.setReadTimeout(readTimeOut);
        } else if (isHttps(url.getProtocol())) {
            httpsURLConnection.setReadTimeout(readTimeOut);
        }

        return new HttpRequest();
    }
    public HttpRequest setConnectionTimeout(int connectionTimeout){
        if (isHttp(url.getProtocol())) {
            httpURLConnection.setConnectTimeout(connectionTimeout);
        } else if (isHttps(url.getProtocol())) {
            httpsURLConnection.setConnectTimeout(connectionTimeout);

        }

        return new HttpRequest();
    }
    public HttpRequest setHeaders(Map<String,String>headers){
        Set<String>keySet = headers.keySet();
        if (isHttp(url.getProtocol())) {
            for (String key:keySet
                 ) {
                httpURLConnection.setRequestProperty(key,headers.get(key));
            }
        } else if (isHttps(url.getProtocol())) {
            for (String key:keySet
            ) {
                httpsURLConnection.setRequestProperty(key,headers.get(key));
            }

        }

        return new HttpRequest();
    }

    private static Boolean isHttp(String baseUrl) {
        return baseUrl.equalsIgnoreCase("http");
    }

    private static Boolean isHttps(String baseUrl) {
        return baseUrl.equalsIgnoreCase("https");
    }

    private static Boolean isConnected(URLConnection isConnection) {
        try {
            isConnection.getRequestProperties();
            if (isConnection instanceof HttpURLConnection) {
                httpURLConnection.disconnect();
            } else if (isConnection instanceof HttpsURLConnection) {
                httpsURLConnection.disconnect();
            }
            return false;
        } catch (IllegalStateException e) {
            return true;
        }
    }

    public String getResponse() throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader bis;
        try {
            bis = new BufferedReader(new InputStreamReader(httpsURLConnection==null?httpURLConnection.getInputStream():httpsURLConnection.getInputStream()));
            while ((line = bis.readLine()) != null) {
                line = new String(line.getBytes(), Charset.defaultCharset());
                sb.append(line);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return sb.toString();

    }
}

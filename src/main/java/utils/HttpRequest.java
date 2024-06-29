package utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.Charset;

public class HttpRequest extends URLConnection {
    private static HttpURLConnection httpURLConnection;
    private static HttpsURLConnection httpsURLConnection;
    private static URL url;

    /**
     * Constructs a URL connection to the specified URL. A connection to
     * the object referenced by the URL is not created.
     *
     * @param url the specified URL.
     */
    protected HttpRequest(URL url) {
        super(url);
    }

    public static HttpRequest doGet(String baseUrl) {
        HttpRequest httpRequest;
        try {
            url = new URL(baseUrl);
            httpRequest = new HttpRequest(url);
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

        return httpRequest;
    }

    public HttpRequest setProxy(Proxy proxy) throws IOException {
        if (isHttp(url.getProtocol())) {
            httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
        } else if (isHttps(url.getProtocol())) {
            httpsURLConnection = (HttpsURLConnection) url.openConnection(proxy);
        } else {
            throw new ProtocolException();
        }
        return new HttpRequest(url);
    }

    @Override
    public void connect() throws IOException {

    }

    public void setReadTimeout(int readTimeOut) {
        if (isHttp(url.getProtocol())) {
            httpsURLConnection.setReadTimeout(readTimeOut);

        } else if (isHttps(url.getProtocol())) {
            httpsURLConnection.setReadTimeout(readTimeOut);
        }
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

    public String getResponse() {
        StringBuilder sb = new StringBuilder();
        String line;
        URLConnection urlConnection = null;
        if (httpURLConnection != null) {
            urlConnection = httpURLConnection;
        } else if (httpsURLConnection != null) {
            urlConnection = httpsURLConnection;
        }

        assert urlConnection != null;
        BufferedReader bis = null;
        try {
            bis = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((line = bis.readLine()) != null) {
                line = new String(line.getBytes(), Charset.defaultCharset());
                sb.append(line);
            }
            bis.close();
        } catch (IOException e) {

        }
        return sb.toString();

    }
}

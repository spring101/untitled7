package com.sc2tv.spring.tx;

import com.sc2tv.spring.tx.model.ProxyUnit;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.net.ProtocolException;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

public class WebClient {

    ClientConnectionManager connManager;// = new PoolingClientConnectionManager();
    private DefaultHttpClient httpClient;

    public WebClient() {
        this.httpClient = new DefaultHttpClient();
    }


    ProxyUnit proxyUnit;

    public void setProxyUnit(ProxyUnit proxyUnit) {
        this.proxyUnit = proxyUnit;
        HttpHost proxy = new HttpHost(proxyUnit.getHost().split(":")[0], Integer.parseInt(proxyUnit.getHost().split(":")[1]), "http");
        httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
    }

    public String requestChatCookie(String url, Map<String, String> params) {
        if (params != null)
            if (!params.isEmpty()) {
                url = url + "?";
                for (Map.Entry<String, String> e : params.entrySet()) {
                    try {
                        url = url + e.getKey() + "=" + URLEncoder.encode(e.getValue(), "UTF-8") + "&";
                    } catch (UnsupportedEncodingException e1) {
                    }
                }
            }
        HttpGet get = new HttpGet(url);
        get.setHeader("Referer", "http://chat.sc2tv.ru/index.htm?channelId=143492&stop=");
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        HttpResponse response = null;
        int tries = 0;
        try {
            response = httpClient.execute(get);
        } catch (HttpHostConnectException httpHostConnectException) {

        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        String responseText = "";
        try {
            responseText = IOUtils.toString(response.getEntity().getContent());

            response.getEntity().getContent().close();
            EntityUtils.consume(response.getEntity());
        } catch (ZipException exp) {
        } catch (IOException e) {
        }

        return responseText;
    }

    public String executeGet(String url) {
        HttpGet get = new HttpGet(url);
        get.setHeader("Referer", url);
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        HttpResponse response = null;
        int tries = 0;
        try {
            response = httpClient.execute(get);
        } catch (HttpHostConnectException httpHostConnectException) {

        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        String responseText = "";
        try {
            responseText = IOUtils.toString(response.getEntity().getContent());

            response.getEntity().getContent().close();
            EntityUtils.consume(response.getEntity());
        } catch (ZipException exp) {
        } catch (IOException e) {
        }

        return responseText;
    }

    public String executeGet(String url, Map<String, String> params) {
        if (params != null)
            if (!params.isEmpty()) {
                url = url + "?";
                for (Map.Entry<String, String> e : params.entrySet()) {
                    try {
                        url = url + e.getKey() + "=" + URLEncoder.encode(e.getValue(), "UTF-8") + "&";
                    } catch (UnsupportedEncodingException e1) {
                    }
                }
            }
        HttpGet get = new HttpGet(url);
        get.setHeader("Referer", url);
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        HttpResponse response = null;
        int tries = 0;
        try {
            response = httpClient.execute(get);
        } catch (HttpHostConnectException httpHostConnectException) {

        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        String responseText = "";
        try {
            responseText = IOUtils.toString(response.getEntity().getContent());

            response.getEntity().getContent().close();
            EntityUtils.consume(response.getEntity());
        } catch (ZipException exp) {
        } catch (IOException e) {
        }

        return responseText;
    }

    public byte[] executeGetByte(String url) {
        HttpGet get = new HttpGet(url);
        get.setHeader("Referer", url);
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        HttpResponse response = null;

        try {

            response = httpClient.execute(get);
        } catch (HttpHostConnectException httpHostConnectException) {
            get.releaseConnection();
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        Byte[] responseText = null;
        try {
            HttpEntity entity = response.getEntity();
            byte[] toReturn = EntityUtils.toByteArray(entity);
            entity.getContent().close();
            EntityUtils.consume(response.getEntity());
            return toReturn;
        } catch (ZipException exp) {
        } catch (IOException e) {
        }

        return null;
    }

    public byte[] executeGetByte(String url, Map<String, String> params) {
        if (params != null)
            if (!params.isEmpty()) {
                url = url + "?";
                for (Map.Entry<String, String> e : params.entrySet()) {
                    try {
                        url = url + e.getKey() + "=" + URLEncoder.encode(e.getValue(), "UTF-8") + "&";
                    } catch (UnsupportedEncodingException e1) {
                    }
                }
            }
        HttpGet get = new HttpGet(url);
        get.setHeader("Referer", url);
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        HttpResponse response = null;
            try {
                response = httpClient.execute(get);
            } catch (HttpHostConnectException httpHostConnectException) {
                get.releaseConnection();
            } catch (ClientProtocolException e) {
            } catch (IOException e) {

        }
        Byte[] responseText = null;
        try {
            HttpEntity entity = response.getEntity();
            byte[] toReturn = EntityUtils.toByteArray(entity);
            EntityUtils.consume(response.getEntity());
            return toReturn;
        } catch (ZipException exp) {
        } catch (IOException e) {
        }

        return null;
    }

    public List<Cookie> getCookies() {
        CookieStore cookieStore = httpClient.getCookieStore();
        return cookieStore.getCookies();
    }
    public String executePost(String url, List<NameValuePair> params) {
        HttpPost post;
        post = new HttpPost(url);
        post.setHeader("Referer", url);
        post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        post.setHeader("Accept-Encoding", "gzip, deflate");
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Accept-Language", "ru,en-us;q=0.7,en;q=0.3");
        post.setHeader("Accept-Charset", "windows-1251,utf-8;q=0.7,*;q=0.7");

        //  post.setHeader("Cache-Control", "max-age=0");
        // post.setHeader("Host", "www.rupoll.com");
        //   post.setHeader("Origin", "http://www.rupoll.com");

        post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));

        HttpResponse response = null;
        try {

            response = httpClient.execute(post);
        } catch (IOException e) {
         //   e.printStackTrace();
        }
        if(response == null)
            return "";
        String responseText = null;
        try {
            InputStream instream = response.getEntity().getContent();
            Header contentEncoding = response.getFirstHeader("Content-Encoding");
            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                instream = new GZIPInputStream(instream);
            }
            responseText = IOUtils.toString(instream);

            response.getEntity().getContent().close();
        } catch (Exception e) {
            System.out.println("error");

          //  e.printStackTrace();
          }
        return responseText;
    }

    public String executePostMultipart(String url) throws IOException {
        StringBuilder toReturn = new StringBuilder();
        String readLine;
        PostMethod filePost = new PostMethod("http://antigate.com/in.php");
        org.apache.commons.httpclient.HttpClient httpClient1 = new org.apache.commons.httpclient.HttpClient();

        BufferedImage img = ImageIO.read(new URL(url));
        File f = new File(url.substring(url.lastIndexOf("/") + 1));
        ImageIO.write(img, "jpg", f);

        Part[] parts = {new StringPart("method", "post"), new StringPart("key", "893298284603e7a13e4e0d42140b6935"), new FilePart("file", new File(url))};
        filePost.setRequestEntity(new MultipartRequestEntity(parts,
                filePost.getParams()));
        BufferedReader br = null;
        try {
            int status = httpClient1.executeMethod(filePost);
            if (status == HttpStatus.SC_NOT_IMPLEMENTED) {
                // still consume the response body
                filePost.getResponseBodyAsString();
            } else {
                br = new BufferedReader(new InputStreamReader(filePost.getResponseBodyAsStream()));
                while (((readLine = br.readLine()) != null)) {
                    toReturn.append(readLine);
                }
            }
        } catch (Exception e) {
        } finally {
            filePost.releaseConnection();
            if (br != null) try {
                br.close();
            } catch (Exception fe) {
            }
        }
        return toReturn.toString();
    }

    HttpResponse executeGetRaw(String url) throws IOException {
        HttpGet get = new HttpGet(url);
        get.setHeader("Referer", url);
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        HttpResponse response = httpClient.execute(get);
        IOUtils.closeQuietly(response.getEntity().getContent());
        return response;
    }


    public void sendPostStrawpool(int id, int vote) {

        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(String.format("http://strawpoll.me/ajax/vote")).openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyUnit.getHost().split(":")[0], Integer.parseInt(proxyUnit.getHost().split(":")[1]))));
        } catch (IOException e) {
        }

        //add reuqest header
        con.setRequestProperty("Accept", "\tapplication/json, text/javascript, */*; q=0.01");
        con.setRequestProperty("Accept-Encoding", "\tgzip, deflate");
        con.setRequestProperty("Cache-Control", "no-cache");
        con.setRequestProperty("Connection", "\tkeep-alive");
        con.setRequestProperty("Content-Length", "23");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        con.setRequestProperty("Host", "Host");
        con.setRequestProperty("If-None-Match", "63a87");
        con.setRequestProperty("Pragma", "no-cache");
        con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        try {
            con.setRequestMethod("POST");
        } catch (ProtocolException e) {
        }
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Referer", String.format("http://strawpoll.me/%d", vote));
        String urlParameters = "votes%5B%5D=" + id + "&id=" + vote;

        // Send post request
        con.setDoOutput(true);
        try {
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
        } catch (Exception exp) {
        }
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception exp) {
        }
    }

    public String sendPostRupool(String id, String vote) {

        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(String.format("http://rupoll.com/vote.php")).openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyUnit.getHost().split(":")[0], Integer.parseInt(proxyUnit.getHost().split(":")[1]))));
        } catch (IOException e) {
        }

        //add reuqest header
        con.setRequestProperty("Accept", "\tapplication/json, text/javascript, */*; q=0.01");
        con.setRequestProperty("Accept-Encoding", "\tgzip, deflate");
        con.setRequestProperty("Cache-Control", "no-cache");
        con.setRequestProperty("Connection", "\tkeep-alive");
        con.setRequestProperty("Content-Length", "23");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        con.setRequestProperty("Host", "Host");
        con.setRequestProperty("If-None-Match", "63a87");
        con.setRequestProperty("Pragma", "no-cache");
        con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        try {
            con.setRequestMethod("POST");
        } catch (ProtocolException e) {
        }
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Referer", String.format("http://rupoll.com/%s.html", id));
        String urlParameters = "poll_id=" + id + "&vote=" + vote;

        // Send post request
        con.setDoOutput(true);
        try {
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
        } catch (Exception exp) {
            // System.out.println(String.format("Timeout!, %s", exp.getMessage()));
        }
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            return response.toString();
        } catch (Exception exp) {
        }
        return "";
    }
}

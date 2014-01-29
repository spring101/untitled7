package com.sc2tv.spring.tx.model;

import org.apache.commons.httpclient.ProxyClient;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.regexp.recompile;

@Entity
@Table(name="PROXYUNIT", uniqueConstraints=
@UniqueConstraint(columnNames = {"host", "port"}) )
public class ProxyUnit implements Serializable{
    private static final long serialVersionUID = 1L;

    public ProxyUnit(String host, int port, int version) {
        this.host = host;
        this.port = port;
        this.version = version;
    }
    @Id
    @Column(name="id")
    @GeneratedValue
    private int id ;
    String host;
    int port;
    private int version;

    public void setVersion(int version) {
        this.version = version;
    }


    public Integer getId() {
        return id;
    }

    @Column(name="host")
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Column(name="port")
    public int getPort() {
        return port;
    }
    @Version
    @Column(name="VERSION")
    public int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "ProxyUnit{" +
                "id=" + id +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", version=" + version +
                '}';
    }
    public int checkProxy() {
        ProxyClient client = new ProxyClient();

        boolean error=false;
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            HttpHost target = new HttpHost("issues.apache.org", 443, "https");
            HttpHost proxy = new HttpHost(host, port, "http");

            RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
            HttpGet request = new HttpGet("/");
            request.setConfig(config);
            CloseableHttpResponse response = httpclient.execute(target, request);
            try {
                HttpEntity entity = response.getEntity();
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            error = true;
        } catch (IOException e) {
            error = true;
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
            }
        }
        if(!error){
           // System.out.println("Coutched! "+ host);
            return 1;
        }
        else
            return 0;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public void setPort(Integer port) {
        this.port = port;
    }

    public ProxyUnit(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public ProxyUnit() {

    }
    public ProxyUnit(String proxy) {
        try{
        this.host = proxy.split(":")[0];
        this.port = Integer.parseInt(proxy.split(":")[1]);
        }
        catch(Exception exp){};
    }
}

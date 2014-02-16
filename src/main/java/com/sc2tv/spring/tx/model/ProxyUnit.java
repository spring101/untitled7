package com.sc2tv.spring.tx.model;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;

@Entity
@Table(name = "PROXY", uniqueConstraints =
@UniqueConstraint(columnNames = {"host"}))
public class ProxyUnit implements Serializable {
    private static final long serialVersionUID = 1L;

    public ProxyUnit(String host, int usages) {
        this.host = host;
        this.usages = usages;
    }

    @Id
    @Column(name = "host")
    @GeneratedValue
    String host;
    int usages;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean checkProxyReachable() {
        boolean connectionStatus = false;
        try {
            InetAddress addr = InetAddress.getByName(getHost().split(":")[0]);//here type proxy server ip
            connectionStatus = addr.isReachable(3000); // 1 second time for response
        } catch (Exception e) {
        }

        return connectionStatus;

    }

    public boolean checkProxy(String targetHost) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpHost target = new HttpHost(targetHost);
            HttpHost proxy = new HttpHost(host.split(":")[0], Integer.parseInt(host.split(":")[1]));
            RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
            HttpGet request = new HttpGet("/");
            request.setConfig(config);
            CloseableHttpResponse response = null;
            try{
              response = httpclient.execute(target, request);
            }
            catch (Exception exp){
            }
            try {
                HttpEntity entity = response.getEntity();
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
            }
        }
        return true;
    }


    public ProxyUnit(String host, Integer usages) {
        this.host = host;
        this.usages = usages;
    }

    public ProxyUnit() {

    }

    public ProxyUnit(String proxy) {
        this.host = proxy;
        this.usages = 0;
    }
}

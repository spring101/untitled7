package com.sc2tv.spring.tx.proxy;

import com.sc2tv.spring.tx.model.ProxyUnit;

import java.io.IOException;
import java.util.List;

public interface ProxyManager {
    ProxyUnit insertProxyUnit(ProxyUnit proxyUnit);
    ProxyUnit getProxyUnitById(int i);
    int getCount();
    List<ProxyUnit> getProxyInRange(int begin, int end);

    ProxyUnit getProxyUnit(String host, int port);
    List<ProxyUnit> getProxyUnitList();

    List<ProxyUnit> getAvaliableProxies();

    void removeProxy(ProxyUnit proxyUnit);

    ProxyUnit getAvaliable();

    void addFromFile(String path) throws IOException;

    int getPool();

    void setPool(int pool);

    void run();
}

package com.sc2tv.spring.tx.dao;

import com.sc2tv.spring.tx.model.ProxyUnit;

import java.util.List;

public interface ProxyUnitDAO {
    ProxyUnit insertProxyUnit(ProxyUnit proxyUnit);
    ProxyUnit getProxyUnit(String host, int port);
    ProxyUnit getProxyUnit(int i);
    List<ProxyUnit> geProxyUnitlist();

    List<ProxyUnit> getProxyInRange(int begin, int end);

    int getCount();
}

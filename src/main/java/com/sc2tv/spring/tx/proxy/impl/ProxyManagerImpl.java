package com.sc2tv.spring.tx.proxy.impl;

import com.sc2tv.spring.tx.dao.ProxyUnitDAO;
import com.sc2tv.spring.tx.model.ProxyUnit;
import com.sc2tv.spring.tx.proxy.ProxyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class ProxyManagerImpl implements ProxyManager, Runnable {
    @Autowired
    private ProxyUnitDAO proxyUnitDAO;

    @Override
    public ProxyUnit insertProxyUnit(ProxyUnit proxyUnit) {
        return proxyUnitDAO.insertProxyUnit(proxyUnit);
    }

    @Override
    public ProxyUnit insertProxyUnit(String proxy) {
        if (proxy.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}:\\d{0,6}")) {
            return proxyUnitDAO.insertProxyUnit(new ProxyUnit(proxy));
        }
        return null;
    }

    @Override
    public int getCount() {
        return proxyUnitDAO.getCount();
    }

    @Override
    public List<ProxyUnit> getProxyInRange(int begin, int end) {
        return proxyUnitDAO.getProxyInRange(begin, end);
    }

    @Override
    public ProxyUnit getProxyUnit(String host) {
        return proxyUnitDAO.getProxyUnit(host);
    }

    @Override
    public List<ProxyUnit> getProxyUnitList() {
        return proxyUnitDAO.getProxyUnitlist();
    }

    @Override
    public List<ProxyUnit> getAvaliableProxies() {
        return avaliableProxies;
    }

    List<ProxyUnit> avaliableProxies = new ArrayList<>();

    @Override
    public void removeProxy(ProxyUnit proxyUnit) {
        avaliableProxies.remove(proxyUnit);
    }

    @Override
    public ProxyUnit getAvaliable() {
        if (avaliableProxies.isEmpty())
            return null;
        Random random = new Random();
        int number = random.nextInt(avaliableProxies.size());
        return avaliableProxies.get(number);
    }

    @Override
    public void addFromFile(String path) throws IOException {
        BufferedReader br = null;
        String line = null;

        br = new BufferedReader(new FileReader(path));

        line = br.readLine();


        while (line != null) {
            String[] lines = line.split(":");
            if (lines.length < 2)
                continue;
            else {
                String host = line.split(":")[0];
                String port = line.split(":")[1];
                try {
                    insertProxyUnit(new ProxyUnit(host, Integer.parseInt(port)));
                } catch (Exception exp) {
                }
                line = br.readLine();
            }
        }
        try {
            br.close();
        } catch (Exception exp) {
        }
    }

    @Override
    public int getPool() {
        return pool;
    }

    @Override
    public void setPool(int pool) {
        this.pool = pool;
    }

    int pool;

    @Override
    public void run() {
        List<ProxyUnit> proxyUnits = getProxyUnitList();
        while (true) {
            ExecutorService executor = Executors.newFixedThreadPool(pool);
            for (final ProxyUnit proxyUnit: proxyUnits) {
                executor.submit(new Runnable() {
                    public void run() {
                        try{
                        if (proxyUnit.checkProxyReachable())
                            if (proxyUnit.checkProxy("sc2tv.ru")) {
                                if (!avaliableProxies.contains(proxyUnit)) {
                                    avaliableProxies.add(proxyUnit);
                                }
                            }// else
                               // removeProxy(proxyUnit);
                        }
                        catch(Exception exp){
                        }
                    }
                });
            }
            try {
                executor.shutdown();
                executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

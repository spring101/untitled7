package com.sc2tv.spring.tx.proxy.impl;

import com.sc2tv.spring.tx.dao.ProxyUnitDAO;
import com.sc2tv.spring.tx.model.ProxyUnit;
import com.sc2tv.spring.tx.proxy.ProxyManager;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
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

@Service
@Transactional
@Repository
public class ProxyManagerImpl implements ProxyManager, Runnable {
    private ProxyUnitDAO proxyUnitDAO;

    @Override
    public ProxyUnit insertProxyUnit(ProxyUnit proxyUnit) {
       /* try{
            if(getProxyUnit(proxyUnit.getHost(),proxyUnit.getPort())==null)
                proxyUnitDAO.insertProxyUnit(proxyUnit);
        }
        catch (IndexOutOfBoundsException exp){*/
        return proxyUnitDAO.insertProxyUnit(proxyUnit);
    }
    @Override
    public ProxyUnit getProxyUnitById(int i) {
        return proxyUnitDAO.getProxyUnit(i);
    }
    @Override
    public int getCount(){
        return proxyUnitDAO.getCount();
    }
    @Override
    public List<ProxyUnit> getProxyInRange(int begin, int end){
        return proxyUnitDAO.getProxyInRange(begin, end);
    }
    @Override
    public ProxyUnit getProxyUnit(String host, int port) {
        return proxyUnitDAO.getProxyUnit(host, port);
    }

    @Override
    public List<ProxyUnit> getProxyUnitList() {
        return proxyUnitDAO.geProxyUnitlist();
    }
    @Override
    public List<ProxyUnit> getAvaliableProxies() {
        return avaliableProxies;
    }

    List<ProxyUnit> avaliableProxies = new ArrayList<>();
    @Override
    public void removeProxy(ProxyUnit proxyUnit)
    {
        avaliableProxies.remove(proxyUnit);
    }
    @Override
    public ProxyUnit getAvaliable() {
        if(avaliableProxies.isEmpty())
            return null;
        Random random = new Random();
        int number = random.nextInt(avaliableProxies.size());
        return avaliableProxies.get(number);
    }
    private void checker(int start, int end)
    {

    }
    @Override
    public void addFromFile(String path) throws IOException {
        BufferedReader br = null;
        String line = null;

            br = new BufferedReader(new FileReader(path));

            line = br.readLine();


            while (line != null) {
                String[] lines = line.split(":");
                if(lines.length<2)
                    continue;
                else
                {
                String host = line.split(":")[0];
                String port = line.split(":")[1];
                try{
                insertProxyUnit(new ProxyUnit(host, Integer.parseInt(port)));
                }
                catch (ConstraintViolationException exp){}
                    line = br.readLine();
            }
            }
        try{
            br.close();
        }
        catch (Exception exp){}
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
        ExecutorService executor = Executors.newFixedThreadPool(pool);
            int count = getCount();
            while(true)
            for(int i=0; i<count;i+=pool){
                for(final ProxyUnit proxyUnit: getProxyInRange(i, i+pool)){
                    executor.submit(new Runnable(){
                        public void run() {
                            if(proxyUnit.checkProxy()==1)
                                avaliableProxies.add(proxyUnit);
                            else
                                removeProxy(proxyUnit);
                        }
                    });
                }
                try {
                    new Thread().sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }

    }
}

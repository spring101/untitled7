package com.sc2tv.spring.tx.dao.impl;

import com.sc2tv.spring.tx.dao.ProxyUnitDAO;
import com.sc2tv.spring.tx.model.ProxyUnit;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
@Repository
public class ProxyUnitDAOImpl implements ProxyUnitDAO{
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public ProxyUnit insertProxyUnit(ProxyUnit proxyUnit) {

        sessionFactory.getCurrentSession().saveOrUpdate(proxyUnit);
        return proxyUnit;
    }

    @Override
    public ProxyUnit getProxyUnit(String host, int port) {
        Query query = sessionFactory.
                getCurrentSession().createQuery("from ProxyUnit where host = :host and port = :port");
        query.setParameter("host", host);
        query.setParameter("port", port);
        return (ProxyUnit)query.list().get(0);
    }

    @Override
    public ProxyUnit getProxyUnit(int i) {
        Query query = sessionFactory.
                getCurrentSession().createQuery("from ProxyUnit where id = :id");
        query.setParameter("id", i);
        return (ProxyUnit)query.list().get(0);
    }

    @Override
    public List<ProxyUnit> geProxyUnitlist() {
        Criteria criteria = sessionFactory.
                getCurrentSession().
                createCriteria(ProxyUnit.class);
        return criteria.list();
    }

    @Override
    public List<ProxyUnit> getProxyInRange(int begin, int end) {
        if(begin>end)
            return null;
        Criteria criteria = sessionFactory.
                getCurrentSession().
                createCriteria(ProxyUnit.class).add(Restrictions.between("id", begin, end));
        return criteria.list();
    }

    @Override
    public int getCount() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProxyUnit.class);
        criteria.setProjection(Projections.rowCount());
        return ((Long)criteria.uniqueResult()).intValue();
    }


}

package com.sc2tv.spring.tx.dao.impl;

import com.sc2tv.spring.tx.dao.ProxyUnitDAO;
import com.sc2tv.spring.tx.model.ProxyUnit;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProxyUnitDAOImpl implements ProxyUnitDAO {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public ProxyUnit insertProxyUnit(ProxyUnit proxyUnit) {

        sessionFactory.getCurrentSession().saveOrUpdate(proxyUnit);
        return proxyUnit;
    }

    @Override
    public ProxyUnit getProxyUnit(String host) {
        Query query = sessionFactory.
                getCurrentSession().createQuery("from ProxyUnit where host = :host");
        query.setParameter("host", host);
        return (ProxyUnit) query.list().get(0);
    }


    @Override
    public List<ProxyUnit> getProxyUnitlist() {
        Criteria criteria = sessionFactory.
                getCurrentSession().
                createCriteria(ProxyUnit.class);
        return criteria.list();
    }

    @Override
    public List<ProxyUnit> getProxyInRange(int begin, int end) {
        if (begin > end)
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
        return ((Long) criteria.uniqueResult()).intValue();
    }
}

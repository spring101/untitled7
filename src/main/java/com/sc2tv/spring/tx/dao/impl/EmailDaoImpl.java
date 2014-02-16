package com.sc2tv.spring.tx.dao.impl;

import com.sc2tv.spring.tx.dao.EmailDao;
import com.sc2tv.spring.tx.model.EmailClass;
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
public class EmailDaoImpl implements EmailDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public EmailClass insertEmail(EmailClass emailClass) {
        sessionFactory.getCurrentSession().saveOrUpdate(emailClass);
        return emailClass;
    }

    @Override
    public EmailClass getEmail(String email) {
        Query query = sessionFactory.
                getCurrentSession().createQuery("from EmailClass where emailClass = :emailClass");
        query.setParameter("email", email);
        EmailClass toReturn;
        try {
            toReturn = (EmailClass) query.list().get(0);
        } catch (Exception exp) {
            return null;
        }
        return toReturn;
    }

    @Override
    public List<EmailClass> getEmaillist() {
        Criteria criteria = sessionFactory.
                getCurrentSession().
                createCriteria(EmailClass.class);
        return criteria.list();
    }

    @Override
    public List<EmailClass> getNotUsed() {
        Criteria criteria = sessionFactory.
                getCurrentSession().
                createCriteria(EmailClass.class).add(Restrictions.eq("used", 0));
        return criteria.list();
    }

    @Override
    public int getCount() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EmailClass.class);
        criteria.setProjection(Projections.rowCount());
        return ((Long) criteria.uniqueResult()).intValue();
    }

    @Override
    public EmailClass getFirstNotUsed() {
        EmailClass toReturn = (EmailClass) sessionFactory.
                getCurrentSession().createQuery("from EmailClass where used = 0").setMaxResults(1).uniqueResult();
        return toReturn;
    }
}

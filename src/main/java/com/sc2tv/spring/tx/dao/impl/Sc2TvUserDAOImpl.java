package com.sc2tv.spring.tx.dao.impl;

import com.sc2tv.spring.tx.dao.Sc2TvUserDAO;
import com.sc2tv.spring.tx.model.Sc2TvUser;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional

public class Sc2TvUserDAOImpl implements Sc2TvUserDAO {
    @Autowired
    SessionFactory sessionFactory;

    @Override
    public void insertSc2TvUser(Sc2TvUser sc2TvUser){
        sessionFactory.getCurrentSession().save(sc2TvUser);
    }

    @Override
    public Sc2TvUser getSc2TvUser(int userId){
        return (Sc2TvUser)sessionFactory.
                getCurrentSession().
                get(Sc2TvUser.class, userId);
    }

    @Override
    public Sc2TvUser getSc2TvUserByName(String username){
        Query query = sessionFactory.
                getCurrentSession().createQuery("from Sc2TvUser where username = :username");
        query.setParameter("username", username);
        return (Sc2TvUser)query.list().get(0);
    }

    @Override
    public List<String> getEmails(boolean banned) {
        Query query = sessionFactory.
                getCurrentSession().createQuery("select email from Sc2TvUser where banned = :banned");
        query.setParameter("banned", banned);
        return (List<String>)query.list();
    }

    @Override
    public Sc2TvUser getSc2TvUserByEmail(String email){
        Query query = sessionFactory.
                getCurrentSession().createQuery("from Sc2TvUser where email = :email");
        query.setParameter("email", email);
        return (Sc2TvUser)query.list().get(0);
    }
    @Override
    public List<Sc2TvUser> getSc2TvUsers(){
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Sc2TvUser.class);
        return criteria.list();
    }

    @Override
    public List<Sc2TvUser> getNotBanned() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Sc2TvUser.class);
        criteria.add(Restrictions.like("BANNED", "false"));
        return criteria.list();
    }
}

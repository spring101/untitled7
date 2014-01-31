package com.sc2tv.spring.tx.user.impl;

import com.sc2tv.spring.tx.User;
import com.sc2tv.spring.tx.dao.Sc2TvUserDAO;
import com.sc2tv.spring.tx.model.Sc2TvUser;
import com.sc2tv.spring.tx.user.UserManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserManagerImpl implements UserManager {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("../webapp/WEB-INF/spring-config.xml");
    Sc2TvUserDAO sc2TvUserDAO = (Sc2TvUserDAO) ctx.getBean("Sc2TvUserDAOImpl");
    @Override
    public User getUserById(int userId) {
        return (User)sc2TvUserDAO.getSc2TvUser(userId);
    }

    @Override
    public Sc2TvUser getUser(String username) {
        return sc2TvUserDAO.getSc2TvUserByName(username);
    }

    @Override
    public List<User> getUsers() {
        return (List<User>)(List<?>)sc2TvUserDAO.getSc2TvUsers();
    }

    @Override
    public void setProxyUnit(int i) {

    }

    @Override
    public List<User> getNotBanned() {
        return (List<User>)(List<?>)sc2TvUserDAO.getNotBanned();
    }


}

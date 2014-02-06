package com.sc2tv.spring.tx.user.impl;

import com.sc2tv.spring.tx.User;
import com.sc2tv.spring.tx.dao.Sc2TvUserDAO;
import com.sc2tv.spring.tx.model.Sc2TvUser;
import com.sc2tv.spring.tx.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserManagerImpl implements UserManager {
    @Autowired
    Sc2TvUserDAO sc2TvUserDAO;

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

package com.sc2tv.spring.tx.user.impl;

import com.sc2tv.spring.tx.dao.Sc2TvUserDAO;
import com.sc2tv.spring.tx.model.Sc2TvUser;
import com.sc2tv.spring.tx.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserManagerImpl implements UserManager {

    @Autowired
    private Sc2TvUserDAO sc2TvUserDAO;

    @Override
    public void insertUser(Sc2TvUser user) {
        sc2TvUserDAO.insertSc2TvUser(user);
    }

    @Override
    public Sc2TvUser getUserById(int userId) {
        return sc2TvUserDAO.getSc2TvUser(userId);
    }

    @Override
    public Sc2TvUser getUser(String username) {
        return sc2TvUserDAO.getSc2TvUserByName(username);
    }

    @Override
    public List<Sc2TvUser> getUsers() {
        return sc2TvUserDAO.getSc2TvUsers();
    }

    @Override
    public void setProxyUnit(int i) {

    }

    @Override
    public List<Sc2TvUser> getNotBanned() {
        return sc2TvUserDAO.getNotBanned();
    }


}

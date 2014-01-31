package com.sc2tv.spring.tx.user;

import com.sc2tv.spring.tx.User;
import com.sc2tv.spring.tx.model.Sc2TvUser;

import java.util.List;

public interface UserManager {

    Sc2TvUser getUserById(int userId);

    Sc2TvUser getUser(String username);

    List<User> getUsers();

    void setProxyUnit(int i);

    List<User> getNotBanned();
}

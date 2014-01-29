package com.sc2tv.spring.tx.user;
import com.sc2tv.spring.tx.model.Sc2TvUser;

import java.util.List;

public interface UserManager {

    void insertUser(Sc2TvUser sc2TvUser);

    Sc2TvUser getUserById(int userId);

    Sc2TvUser getUser(String username);

    List<Sc2TvUser> getUsers();

    void setProxyUnit(int i);

    List<Sc2TvUser> getNotBanned();
}

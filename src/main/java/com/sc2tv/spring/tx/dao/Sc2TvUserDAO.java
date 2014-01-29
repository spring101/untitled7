package com.sc2tv.spring.tx.dao;

import com.sc2tv.spring.tx.model.Sc2TvUser;

import java.util.List;

public interface Sc2TvUserDAO {
    void insertSc2TvUser(Sc2TvUser sc2TvUser);

    Sc2TvUser getSc2TvUser(int userId);

    Sc2TvUser getSc2TvUserByName(String username);
    List<String> getEmails(boolean banned);

    Sc2TvUser getSc2TvUserByEmail(String email);
    List<Sc2TvUser> getSc2TvUsers();
    List<Sc2TvUser> getNotBanned();
   /* void setProxyUnit(int i);
    ProxyUnit getProxyUnit(int i);*/

}

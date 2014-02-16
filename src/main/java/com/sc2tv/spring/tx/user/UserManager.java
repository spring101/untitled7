package com.sc2tv.spring.tx.user;

import com.sc2tv.spring.tx.User;
import com.sc2tv.spring.tx.model.Sc2TvUser;

import java.util.List;
import java.util.Map;

public interface UserManager {

    Map<String, User> getAvaliableUsers();

    Sc2TvUser insertUser(User user);

    Sc2TvUser insertUser(Sc2TvUser user);

    User getUserById(int userId);

    Sc2TvUser getUser(String username);

    List<User> getUsers();

    void setProxyUnit(int i);

    int getLoggedIn();

    void voteForBan(String banUserId, String userName, String messageId, String reasonId);

    User getAvaliableUser();

    void sendMessage(String message, String channelId);

    void sendMessage(String message, String channelId, String user);

    void writeAll(String message, String channelId);

    void banAll();

    void whatAGame();

    public void init();
    List<User> getNotBanned();
}

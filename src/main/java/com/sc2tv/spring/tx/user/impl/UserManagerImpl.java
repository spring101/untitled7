package com.sc2tv.spring.tx.user.impl;

import com.sc2tv.spring.tx.User;
import com.sc2tv.spring.tx.chat.Channel;
import com.sc2tv.spring.tx.chat.Channels;
import com.sc2tv.spring.tx.dao.Sc2TvUserDAO;
import com.sc2tv.spring.tx.model.Sc2TvUser;
import com.sc2tv.spring.tx.proxy.ProxyManager;
import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.Impl.WriteInChat;
import com.sc2tv.spring.tx.service.IOService.ActionService.Param;
import com.sc2tv.spring.tx.service.IOService.ActionService.ParamType;
import com.sc2tv.spring.tx.user.UserManager;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UserManagerImpl implements UserManager {


    @Autowired
    Sc2TvUserDAO sc2TvUserDAO;
    @Autowired
    ProxyManager proxyManager;
    @Autowired
    WriteInChat writeInChat;
    @Override
    public Map<String, User> getAvaliableUsers() {
        return avaliableUsers;
    }

    @Override
    public Sc2TvUser insertUser(User user) {
        sc2TvUserDAO.insertSc2TvUser(user);
        return user;
    }
    @Override
    public Sc2TvUser insertUser(Sc2TvUser user) {
        sc2TvUserDAO.insertSc2TvUser(user);
        return user;
    }
    private Map<String, User> avaliableUsers;

    @Override
    public User getUserById(int userId) {
        return (User) sc2TvUserDAO.getSc2TvUser(userId);
    }

    @Override
    public Sc2TvUser getUser(String username) {
        return sc2TvUserDAO.getSc2TvUserByName(username);
    }

    @Override
    public List<User> getUsers() {
        return (List<User>) (List<?>) sc2TvUserDAO.getSc2TvUsers();
    }

    @Override
    public void setProxyUnit(int i) {

    }

    @Override
    public List<User> getNotBanned() {
        return (List<User>) (List<?>) sc2TvUserDAO.getNotBanned();
    }
    @Override
    public int getLoggedIn(){
        return avaliableUsers.size();
    }
    @Override
    public void voteForBan(String banUserId, String userName, String messageId, String reasonId){
        Set<User> users = new HashSet<>();
        if(avaliableUsers.size()<3)
            return;
        while(users.size()!=3){
            users.add(getAvaliableUser());
        }
        for(User user: users){
            user.voteBan(banUserId, userName, messageId, reasonId);
        }
    }
    @Override
    public User getAvaliableUser(){
        Random r = new Random();
        List<String> keys  = new ArrayList<String>(avaliableUsers.keySet());
        int i = r.nextInt(keys.size());
        return avaliableUsers.get(keys.get(i));
    }
    @Override
    public void sendMessage(String message, String channelId){
        Map<Param, Object> params = new HashMap<>();
        params.put(new Param("channelId", true, ParamType.STRING), channelId);
        params.put(new Param("message", true, ParamType.STRING), message);
        writeInChat.execute(params);
    }
    @Override
    public void sendMessage(String message, String channelId, String user){
        Map<Param, Object> params = new HashMap<>();
        params.put(new Param("channelId", true, ParamType.STRING), channelId);
        params.put(new Param("message", true, ParamType.STRING), message);
        params.put(new Param("user", true, ParamType.STRING), user);

        writeInChat.execute(params);
    }
    @Override
    public void writeAll(final String message, final String channelId){
        final Map<String, User> users = avaliableUsers;
        List<String> names = (List<String>) users.keySet();
        Collections.shuffle(names);
        ExecutorService executionService = Executors.newFixedThreadPool(20);
        for(final String s: names){
            executionService.submit(new Runnable() {
                @Override
                public void run() {
                    sendMessage(message, channelId, s);
                }
            });
        }
        executionService.shutdown();
    }
    @Override
    public void banAll(){
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        Set<String> names = new HashSet<>();
        while (true) {
            Channels ch = new Channels();
            JSONArray messages = ch.getModMessages();
            for(Object message: messages){
                final String name = (String) ((JSONObject) message).get("name");
                if(names.contains(name)) {
                    continue;
                } else {
                    names.add(name);
                }
                final String id = (String) ((JSONObject) message).get("id");
                final String uid = (String) ((JSONObject) message).get("uid");
                int counter = 3;
                while(counter--!=0) {
                    executorService.submit(new Runnable() {
                                        @Override
                                        public void run() {
                                            getAvaliableUser().voteBan(uid, name, id, "4");
                                        }
                                    });
                }
            }
        }
    }
    @Override
    public void whatAGame(){
        final List<String> smiles = new ArrayList<String>(){{
            add(":s:peka:");
            add(":s:grumpy:");
            add(":s:cry:");
            add(":s:okay:");
            add(":s:neponi:");
            add(":s:jae:");
            add(":s:wb:");
            add(":s:kot:");
            add(":s:mvp:");
            add(":s:fu:");
            add(":s:whut:");
            add(":s:huh:");
            add(":s:yao:");
            add(":s:ploho:");
            add(":s:lucky:");
            add(":s:epeka:");
            add(":s:bin:");
            add(":s:adolf:");
            add(":s:adolf:");
            add(":s:adolf:");
            add(":s:adolf:");
            add(":s:adolf:");
            add(":s:aws:");
        }};
        final List<String> messages = new ArrayList<String>(){{
            add("мочератор");
            add("мочерастия");
            add("нарушение правил нарушение правил");
            add("негражданин - уничтожить");
            add("я модератор, мне можно");
            add("нарушение пункта 12.5");
            add("правила правила правила");
            add("по штанине потекло");
            add("моча моча моча");
            add("еще один хертстоун");
            add("еще одна лига легенд");
            add("еще одна дота");
            add("минус за неадекватность");
            add("минус за неадекватность");
        }};
        final Random r = new Random();
        final Map<String, Channel> channelMap = new Channels(1).getAllChannels();
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        List<String> keys = new ArrayList<String> (channelMap.keySet());
        Collections.shuffle(keys);
        int counter = 0;
        Map<Integer, Map<String, String>> messToSend = new HashMap<>();
        for(int i=0; i<100; i++){
            final Map<String, String> toAdd = new HashMap<>();
        for(final String key: keys){

            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    String smile = "";
                    String message;
                    int num = r.nextInt(10);
                    if(num>4)
                        smile = smiles.get(r.nextInt(smiles.size()));
                    num = r.nextInt(5);
                    if(num>2)
                        message = smile + messages.get(r.nextInt(messages.size()));
                    else
                        message = messages.get(r.nextInt(messages.size())) + smile;
                    toAdd.put(key, message);
                  //  System.out.println("STREAMER: " + channelMap.get(key).getStreamerName() + " " + message);
                }
            });
            messToSend.put(i, toAdd);
        }
        }
        for(Integer key: messToSend.keySet()){
            for(String key2: messToSend.get(key).keySet()){
                sendMessage(messToSend.get(key).get(key2), "146975");
            }
        }
        int g=5;
    }
    @Override
    public void init() {
        avaliableUsers = new HashMap<>();
        final List<Sc2TvUser> totalUsers = sc2TvUserDAO.getSc2TvUsers();


        ExecutorService executorService = Executors.newFixedThreadPool(80);
        for (final Sc2TvUser sc2TvUser : totalUsers) {
                executorService.submit(new Runnable() {
                    public void run() {
                        User toAdd = new User(sc2TvUser);
                        int counter=0;
                        if(avaliableUsers.get(toAdd.getUsername())==null)
                        {while (!toAdd.isLoggedIn()) {
                            counter++;
                            toAdd.setProxyUnit(proxyManager.getAvaliable());
                            toAdd.logIn();
                            if(counter>5){
                                System.out.println("error!!! " + toAdd.getEmail());
                                return;
                            }
                        }
                            if (toAdd.isLoggedIn()) {
                                avaliableUsers.put(toAdd.getUsername(), toAdd);
                        }else
                            System.out.println("error!!! " + toAdd.getEmail());

                        }

                }


});
}
    }}
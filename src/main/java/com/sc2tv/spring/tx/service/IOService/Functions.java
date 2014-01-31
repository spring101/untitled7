package com.sc2tv.spring.tx.service.IOService;

import com.sc2tv.spring.tx.User;
import com.sc2tv.spring.tx.chat.Channels;
import com.sc2tv.spring.tx.service.strawpool.Strawpool;
import com.sc2tv.spring.tx.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class Functions {
    @Autowired
    private Strawpool strawpool;
    @Autowired
    private Channels channels;
    public final static Functions FUNCTIONS = new Functions();
    public class Bot{
        public String writeInChat(User user, String channel, String message){
            user.sendMessage(message, channel);
            return String.format("Sending message using %s", user.getUsername());
        };
       // public String writeInChat(UserManager userManager, String channel, String message, Map<String, String> options){};
        public String voteFor(UserManager userManager, String channel, int vote){
            if(vote==1){
                for(User user: userManager.getNotBanned())
                {
                        user.voteUp(channel);
                }
                return String.format("Voting UP for %s - %s - %s", channel, channels.getStreamerNameByChannelId(channel), channels.getTitleByChannelId(channel));
            }
            else if(vote==-1){
                for(User user: userManager.getNotBanned())
                {
                    user.voteDown(channel);
                }
                return String.format("Voting DOWN for %s - %s - %s", channel, channels.getStreamerNameByChannelId(channel), channels.getTitleByChannelId(channel));
            }
            else{
                return "Wrong vote option";
            }
        };
        public String voteFor(UserManager userManager, String channel, int amount, int vote){
            int counter = 0;
            if(vote==1){
                for(User user: userManager.getNotBanned())
                {
                    if(counter==amount){
                        break;
                    }
                    user.voteUp(channel);
                    counter++;
                }
                return String.format("Voting UP for %s - %s - %s", channel, channels.getStreamerNameByChannelId(channel), channels.getTitleByChannelId(channel));
            }
            else if(vote==-1){
                for(User user: userManager.getNotBanned())
                {
                    if(counter==amount){
                        break;
                    }
                    user.voteDown(channel);
                    counter++;
                }
                return String.format("Voting DOWN for %s - %s - %s", channel, channels.getStreamerNameByChannelId(channel), channels.getTitleByChannelId(channel));
            }
            else{
                return "Wrong vote option";
            }
        };
        };
    public class Pool{
        public String scanAll(int[] options, int threads, boolean money){
            strawpool.scanAll(options, threads, money);
            return "Starting to scan all chats for pools";
        };
        public String scanChat(String channelId, int[] options, int threads, boolean money){
            strawpool.scanSingle(channelId, threads, options, money);
            return String.format("Starting to scan %s chat for pools", channels.getStreamerNameByChannelId(channelId));
        };
        public String redChat(UserManager userManager, String channelId, int[] options, int votes, int[] delay){
            return "Not implemented yet";
        };
    }
}


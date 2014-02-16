package com.sc2tv.spring.tx.web.controller;

import com.sc2tv.spring.tx.WebClient;
import com.sc2tv.spring.tx.chat.Channels;
import com.sc2tv.spring.tx.proxy.ProxyManager;
import com.sc2tv.spring.tx.service.CaptchaRecognition;
import com.sc2tv.spring.tx.service.IOService.ActionService.Factory.Impl.FunctionFactoryImpl;
import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.Functions;
import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.Impl.WriteInChat;
import com.sc2tv.spring.tx.service.IOService.Command;
import com.sc2tv.spring.tx.service.IOService.commands.Commands;
import com.sc2tv.spring.tx.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("/")
public class WebController {
    @Autowired
    CaptchaRecognition captchaRecognition;
    @Autowired
    Functions functions;
    @Autowired
    Commands commands;
    @Autowired
    UserManager userManager;
    @Autowired
    WriteInChat writeInChat;
    @Autowired
    FunctionFactoryImpl functionFactory;
    @Autowired
    ProxyManager proxyManager;

    @RequestMapping(value = "/action/{action}", method = RequestMethod.POST)
    public ResponseEntity printWelcome(@PathVariable final String action, @MatrixVariable final Map<String, String> parameters) {
        try {
            parameters.remove("$");
        } catch (Exception exp) {
        }
        ;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                commands.addCommand(new Command(new Date().toString(), action, parameters));
            }
        });
        executorService.shutdown();
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
    @RequestMapping(value = "/api/chat/ban", method = RequestMethod.POST)
    public ResponseEntity banUser(@RequestParam("banUserId") String banUserId, @RequestParam("userName") String userName,
                        @RequestParam("messageId") String messageId, @RequestParam("reasonId") String reasonId){
        userManager.voteForBan(banUserId, userName, messageId, reasonId);
        return new ResponseEntity(HttpStatus.ACCEPTED);

    };
    @RequestMapping(value = "/action/writeall", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity writeAll(@RequestParam("message") String message, @RequestParam("channelId") String channelId) {
        userManager.writeAll(message, channelId);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
    @RequestMapping("/api/avaliableproxies")
    @ResponseBody
    public String proxyReader() {
        return String.valueOf(proxyManager.getAvaliableProxies().size());
    }
    @RequestMapping("/api/usercount")
    @ResponseBody
    public String userCount() {
        if(userManager!=null)
            return String.valueOf(userManager.getAvaliableUsers().size());
        return "0";
    }
    @RequestMapping("/api/antigate/balance")
    @ResponseBody
    public String gateStatus() {
        return "$" + captchaRecognition.getBalance();
    }

    @RequestMapping(value = "/action/command", method =  RequestMethod.GET)
    public void inputCommand() {

    }

    @RequestMapping("/channels")
    public String printChannels(ModelMap model) {
        Channels ch = new Channels();
        model.put("channels", ch.getChannels());
        return "channels";
    }

    @RequestMapping(value = "/json/channel")
    @ResponseBody
    public String channelProxyResponse(@RequestParam("channelId") String channelId) {
        return new WebClient().executeGet("http://chat.sc2tv.ru/memfs/channel-" + channelId + ".json");
    }

    @RequestMapping(value = "/json/modChannel")
    @ResponseBody
    public String modChannelProxyResponse() {
        return new WebClient().executeGet("http://chat.sc2tv.ru/memfs/channel-moderator.json");
    }

    @RequestMapping(value = "/channels/channel")
    public String readChannel(ModelMap model, @RequestParam("id") String channelId, @RequestParam(value = "mod", required = false) String mod) {
        String htmlCode = new Channels().getPlayer(channelId);
        model.put("player", htmlCode);
        model.put("title", new Channels().getTitleByChannelId(channelId));
        model.put("channelId", channelId);
        model.put("functions", functions);
        return "channel";
    }

}

package com.sc2tv.spring.tx.web.controller;

import com.sc2tv.spring.tx.WebClient;
import com.sc2tv.spring.tx.chat.Channels;
import com.sc2tv.spring.tx.service.IOService.ActionService.Functions.Functions;
import com.sc2tv.spring.tx.service.IOService.Command;
import com.sc2tv.spring.tx.service.IOService.commands.Commands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/")
public class HelloController {
    @Autowired
    Functions functions;
    @Autowired
    Commands commands;
	@RequestMapping(value="/action/{action}", method = RequestMethod.POST)
	public ResponseEntity printWelcome(@PathVariable String action, @MatrixVariable Map<String, String> parameters) {
        parameters.remove("$");
        if(commands.addCommand(new Command(new Date().toString(), action, parameters))){
            System.out.println("well done");
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        else{
            System.out.println("error");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
	}
    @RequestMapping(value="/action/command", method = RequestMethod.GET)
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
    public String channelProxyResponse(@RequestParam("channelId") String channelId){
        return new WebClient().executeGet("http://chat.sc2tv.ru/memfs/channel-"+channelId+".json");
    }

    @RequestMapping(value = "/json/modChannel")
    @ResponseBody
    public String modChannelProxyResponse(){
        return new WebClient().executeGet("http://chat.sc2tv.ru/memfs/channel-moderator.json");
    }
    @RequestMapping(value = "/channels/channel")
    public String readChannel(ModelMap model, @RequestParam("id") String channelId, @RequestParam(value = "mod", required=false) String mod) {
        model.put("functions", functions);
        return "channel";
    }

}

package com.sc2tv.spring.tx.web.controller;

import com.sc2tv.spring.tx.WebClient;
import com.sc2tv.spring.tx.chat.Channels;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class HelloController {
	@RequestMapping(method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		return "layout";
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
    public String readChannel(ModelMap model, @RequestParam("id") String channelId, @RequestParam("mod") String mod) {
      //  model.put("channel", new Channel(channelId));
        return "channel";
    }

}

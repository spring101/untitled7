package com.sc2tv.spring.tx.web.controller;

import com.sc2tv.spring.tx.chat.Channel;
import com.sc2tv.spring.tx.chat.Channels;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
    @RequestMapping(value = "/channels/channel")
    public String getFoosBySimplePathWithPathVariable(ModelMap model,
                                                      @RequestParam("id") String channelId) {
        Channel channel = new Channel(channelId);
        model.put("channel", channel);
        return "channel";
    }

}

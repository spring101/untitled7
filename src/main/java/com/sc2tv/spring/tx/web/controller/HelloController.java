package com.sc2tv.spring.tx.web.controller;

import com.sc2tv.spring.tx.Channels;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HelloController {
	@RequestMapping(method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		return "layout";
	}
    @RequestMapping("/channel/{channelId:[\\d]+}")
    public String printChannel(ModelMap model) {

        return "channel";
    }
    @RequestMapping("/channels/{channelId:[\\d]+}")
    public String printChannels(ModelMap model) {
        Channels ch = new Channels();
        model.put("channels", ch.getChannels());
        return "channels";
    }

}

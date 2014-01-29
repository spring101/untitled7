package com.sc2tv.spring.tx.web;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class Launcher {
    @PostConstruct
    public void startup() {
        System.out.println("done");
        System.out.println("done");
        System.out.println("done");
        System.out.println("done");
        System.out.println("done");
        System.out.println("done");
        System.out.println("done");
        System.out.println("done");
    }
}

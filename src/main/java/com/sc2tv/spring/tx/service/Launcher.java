package com.sc2tv.spring.tx.service;

import com.sc2tv.spring.tx.proxy.ProxyManager;
import com.sc2tv.spring.tx.registraton.RegistrationManager;
import com.sc2tv.spring.tx.service.strawpool.Strawpool;
import com.sc2tv.spring.tx.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
public class Launcher {
    @Autowired
    ProxyManager proxyManager;
    @Autowired
    Strawpool strawpool;
    @Autowired
    RegistrationManager registrationManager;
    @Autowired
    UserManager userManager;
    @PostConstruct
    public void startup() throws InterruptedException {
       // registrationManager.checkMailsForRegistered();
        strawpool.scanAll(new int[]{-1}, 60, true);
        proxyManager.setPool(65);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new Runnable() {
            public void run() {
                proxyManager.run();
            }
        });
    /*    new Thread().sleep(10_000);
        ExecutorService executor2 = Executors.newSingleThreadExecutor();
        executor2.submit(new Runnable() {
            public void run() {
                userManager.init();
            }
        });
      ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                while(true){
                    userManager.whatAGame();
                }
            }
        });
*/
      //  new Thread().sleep(10_000);
     //   while(userManager.getLoggedIn()<10){};
     //   userManager.banAll();


       // NicknameGenerator nicknameGenerator = new NicknameGenerator();
       // String[] names = nicknameGenerator.generate(700_000);
      //  for(String s: names)
      //  System.out.println(s);
      //  ExecutorService executorService = Executors.newFixedThreadPool(10);
        //for(final String s: names){
            //executorService.submit(
                   // new Runnable() {
                   //     public void run() {
          //                 registrationManager.register(s);
                    //    }
                    //});
        }
    }


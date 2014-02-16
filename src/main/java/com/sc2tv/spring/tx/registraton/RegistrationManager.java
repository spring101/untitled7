package com.sc2tv.spring.tx.registraton;

import com.sc2tv.spring.tx.model.EmailClass;

public interface RegistrationManager {
    // public int registerUser(WebClient webClient, User user, int tries) throws IOException, URISyntaxException;
    public boolean register(String username);
    public void checkMailsForRegistered();
    public void check(EmailClass emailClass);
}

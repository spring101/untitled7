package com.sc2tv.spring.tx.registraton;

import java.io.IOException;
import java.net.URISyntaxException;

public interface RegistrationManager {
    public int registerUser(int tries) throws IOException, URISyntaxException;
    public int setPassword(String url) throws IOException;
}

package com.sc2tv.spring.tx.service;

import com.sc2tv.spring.tx.model.ProxyUnit;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.SearchTerm;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailClass {
    private final static String smtp = "smpt.mail.ru";
    private final static String imap = "imap.mail.ru";
    private final static String pop3 = "pop.mail.ru";

    public ProxyUnit getProxyUnit() {
        return proxyUnit;
    }

    public void setProxyUnit(ProxyUnit proxyUnit) {
        this.proxyUnit = proxyUnit;
    }

    ProxyUnit proxyUnit;

    MailClass(String mail, String pass) {
        this.email = mail;
        this.password = pass;
    }

    String email;
    String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @SuppressWarnings("unchecked")
    SearchTerm term = new SearchTerm() {
        public boolean match(Message message) {
            try {
                if (((InternetAddress) message.getFrom()[0]).getAddress().contains("sc2tv.ru")) {
                    return true;
                }
            } catch (MessagingException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    };

    public boolean checkAvailability() throws MessagingException, IOException {
        if (proxyUnit != null) {
            System.setProperty("http.proxyHost", proxyUnit.getHost().split(":")[0]);
            System.setProperty("http.proxyPort", proxyUnit.getHost().split(":")[1]);
        }
        Properties properties = System.getProperties();
        Session session = Session.getDefaultInstance(properties);
        Store store = session.getStore("pop3");
        try {
            store.connect(pop3, email, password);
        } catch (IllegalStateException exp) {
            if (!store.isConnected())
                return false;
        }
        Folder folder = store.getFolder("inbox");
        folder.open(Folder.READ_ONLY);
        Message[] messages = folder.search(term);
        folder.close(false);
        store.close();
        if (messages.length > 0)
            return false;
        return true;
    }

    public String userName() throws MessagingException, IOException, InterruptedException {
        Properties properties = System.getProperties();
        Session session = Session.getDefaultInstance(properties);
        Store store = session.getStore("pop3");
        try {
            store.connect(pop3, email, password);
        } catch (IllegalStateException exp) {
            if (!store.isConnected())
                return "";
        }
        Folder folder = store.getFolder("inbox");
        folder.open(Folder.READ_ONLY);
        Message[] messages = folder.search(term);
        for (Message message : messages) {
            String content = message.getContent().toString();
            Pattern pattern = Pattern.compile("(?<=имя пользователя:\\s)(\\w+)");
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                folder.close(false);
                store.close();
                return matcher.group();
            }
        }
        folder.close(false);
        return "";
    }

    public void returnData(String[] data) throws MessagingException, IOException, InterruptedException {
        Properties properties = System.getProperties();
        Session session = Session.getDefaultInstance(properties);
        Store store = session.getStore("pop3");
        try {
            store.connect(pop3, email, password);
        } catch (IllegalStateException exp) {
            if (!store.isConnected())
                return;
        }
        Folder folder = store.getFolder("inbox");
        folder.open(Folder.READ_ONLY);
        Message[] messages = folder.search(term);
        for (Message message : messages) {
            String content = message.getContent().toString();
            Pattern pattern = Pattern.compile("(?<=пароль:\\s)(\\w+)");
            Pattern pattern2 = Pattern.compile("(?<=пользователя:\\s)([^пароль]+)");

            Matcher matcher = pattern.matcher(content);
            Matcher matcher2 = pattern2.matcher(content);
            content.contains("пароль");
            if (matcher.find()) {
                folder.close(false);
                store.close();
                data[0] = matcher.group();
                data[1] = content.substring(content.indexOf("пользователя: ") + "пользователя: ".length(), content.indexOf("пароль: ")).replaceAll("\\n\\r", "");
                return;
            }
        }
        folder.close(false);
        return;
    }

    public String returnLoginUrl() throws MessagingException, IOException, InterruptedException {
        Properties properties = System.getProperties();
        Session session = Session.getDefaultInstance(properties);
        Store store = session.getStore("pop3");
        try {
            store.connect(pop3, email, password);
        } catch (IllegalStateException exp) {
            if (!store.isConnected())
                return "";
        }
        Folder folder = store.getFolder("inbox");
        folder.open(Folder.READ_ONLY);
        Message[] messages = folder.search(term);
        for (Message message : messages) {
            String content = message.getContent().toString();
            Pattern pattern = Pattern.compile("(?<=пароль:\\s)(\\w+)");
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                folder.close(false);
                store.close();
                return matcher.group();
            }
        }
        folder.close(false);
        return "";
    }
}

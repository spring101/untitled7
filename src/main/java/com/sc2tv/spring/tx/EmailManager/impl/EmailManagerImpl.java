package com.sc2tv.spring.tx.EmailManager.impl;

import com.sc2tv.spring.tx.EmailManager.EmailManager;
import com.sc2tv.spring.tx.dao.EmailDao;
import com.sc2tv.spring.tx.model.EmailClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.SearchTerm;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailManagerImpl implements EmailManager {
    @Autowired
    EmailDao emailDao;

    private final static String smtp = "smpt.mail.ru";
    private final static String imap = "imap.mail.ru";
    private final static String pop3 = "pop.mail.ru";
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

    @Override
    public List<EmailClass> getEmailList() {
        return emailDao.getEmaillist();
    }

    @Override
    public List<EmailClass> getEmailListNotUsed() {
        return emailDao.getNotUsed();
    }

    @Override
    public EmailClass getEmail(String email) {
        return emailDao.getEmail(email);
    }

    @Override
    public EmailClass inrest(EmailClass emailClass) {
        return emailDao.insertEmail(emailClass);
    }

    @Override
    public boolean checkAvaliable(EmailClass emailClass) {
        Properties properties = System.getProperties();
        Session session = Session.getDefaultInstance(properties);
        try {
            Store store = session.getStore("pop3");
            try {
                store.connect(pop3, emailClass.getEmail(), emailClass.getPassword());
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
        } catch (Exception exp) {
            return false;
        }
    }
    @Override
    public String getName(EmailClass emailClass) throws Exception {
        Properties properties = System.getProperties();
        String name = "";
        Session session = Session.getDefaultInstance(properties);
        Store store = session.getStore("pop3");
        try {
            store.connect(pop3, emailClass.getEmail(), emailClass.getPassword());
        } catch (IllegalStateException exp) {
            if (!store.isConnected())
                return "";
        }
        Folder folder = store.getFolder("inbox");
        folder.open(Folder.READ_ONLY);
        Message[] messages = folder.search(term);
        for (Message message : messages) {
            String content = message.getContent().toString();
            Pattern pattern2 = Pattern.compile("(?<=пользователя:\\s)([^пароль]+)");

            Matcher matcher2 = pattern2.matcher(content);
            content.contains("пароль");
            if (matcher2.find()) {
                folder.close(false);
                store.close();
                name = content.substring(content.indexOf("пользователя: ") + "пользователя: ".length(), content.indexOf("пароль: ")).replaceAll("\\n\\r", "");
                return name;
            }
        }
        folder.close(false);
        return name;
    }
    @Override
    public String getPassword(EmailClass emailClass) {
        Properties properties = System.getProperties();
        Session session = Session.getDefaultInstance(properties);
        try {
            Store store = session.getStore("pop3");
            try {
                store.connect(pop3, emailClass.getEmail(), emailClass.getPassword());
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
                } else {
                    folder.close(false);
                    store.close();
                    return "";
                }
            }
        } catch (Exception exp) {
        }
        return "";
    }

    @Override
    public EmailClass getFirstNotUsed() {
        return emailDao.getFirstNotUsed();
    }
}

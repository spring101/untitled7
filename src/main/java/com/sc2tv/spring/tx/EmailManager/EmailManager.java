package com.sc2tv.spring.tx.EmailManager;

import com.sc2tv.spring.tx.model.EmailClass;

import java.util.List;

public interface EmailManager {
    List<EmailClass> getEmailList();

    List<EmailClass> getEmailListNotUsed();

    EmailClass getEmail(String email);

    EmailClass inrest(EmailClass emailClass);

    boolean checkAvaliable(EmailClass emailClass);

    String getPassword(EmailClass emailClass);

    EmailClass getFirstNotUsed();
    String getName(EmailClass emailClass) throws Exception;
}

package com.sc2tv.spring.tx.dao;

import com.sc2tv.spring.tx.model.EmailClass;

import java.util.List;

public interface EmailDao {
    EmailClass insertEmail(EmailClass emailClass);

    EmailClass getEmail(String email);

    List<EmailClass> getEmaillist();

    List<EmailClass> getNotUsed();

    int getCount();

    EmailClass getFirstNotUsed();
}

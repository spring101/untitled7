package com.sc2tv.spring.tx.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SC2TVUSER")
public class Sc2TvUser {
    @Id
    @Column(name = "USERNAME", nullable = false)
    private String username;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "EMAIL", nullable = false)
    private String email;

    public String getEmailPass() {
        return emailPass;
    }

    public void setEmailPass(String emailPass) {
        this.emailPass = emailPass;
    }

    @Column(name = "EMAILPASS", nullable = false)
    private String emailPass;

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String banned) {
        this.proxy = proxy;
    }

    @Column(name = "PROXY")
    private String proxy;

    public Sc2TvUser() {

    }

    public Sc2TvUser(String email) {
        username = email.substring(0, email.indexOf("@"));
        this.email = email;
    }

    public Sc2TvUser(String username, String password, String email, String emailPass, String proxy) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.proxy = proxy;
        this.emailPass = emailPass;
    }

    public Sc2TvUser(Sc2TvUser sc2TvUser) {
        this.username = sc2TvUser.getUsername();
        this.password = sc2TvUser.getPassword();
        this.email = sc2TvUser.getEmail();
        this.proxy = sc2TvUser.getProxy();
        this.emailPass = sc2TvUser.getEmailPass();
    }

    public String checkPassword(String email) {
        return null;
    }

    private String kaptchaToStrign() {
        return "";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}

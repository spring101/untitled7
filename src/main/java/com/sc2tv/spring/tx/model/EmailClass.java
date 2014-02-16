package com.sc2tv.spring.tx.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "EMAILS", uniqueConstraints =
@UniqueConstraint(columnNames = {"email"}))
public class EmailClass implements Serializable {
    private static final long serialVersionUID = 1L;

    public EmailClass() {
    }

    @Id
    @Column(name = "email")
    @GeneratedValue
    String email;
    @Column(name = "password")
    String password;
    @Column(name = "used")
    int used;

    public EmailClass(String email, String password) {
        this.password = password;
        this.email = email;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
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

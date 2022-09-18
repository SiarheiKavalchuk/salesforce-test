package com.salesforce.test.util.entity;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PasswordAuthenticator extends Authenticator {
    private final String username;
    private final String password;

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}
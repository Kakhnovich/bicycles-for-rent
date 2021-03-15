package com.epam.jwd.rent.model;

import java.math.BigDecimal;

public class UserDto {
    private final String role;
    private final String login;
    private final BigDecimal balance;

    public UserDto(String role, String login, BigDecimal balance) {
        this.role = role;
        this.login = login;
        this.balance = balance;
    }

    public String getRole() {
        return role;
    }

    public String getLogin() {
        return login;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "login='" + login + '\'' +
                ", balance=" + balance +
                '}';
    }
}

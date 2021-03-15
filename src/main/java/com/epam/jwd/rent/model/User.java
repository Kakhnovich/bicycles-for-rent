package com.epam.jwd.rent.model;

import java.math.BigDecimal;

public class User {
    public static int count = 0;
    private final int id;
    private final int roleId;
    private final String login;
    private final String password;
    private final BigDecimal balance;
    private final boolean banned;

    private User(int id, int role_id, String login, String password, BigDecimal balance, boolean banned) {
        this.id = id;
        this.roleId = role_id;
        this.login = login;
        this.password = password;
        this.balance = balance;
        this.banned = banned;
    }

    private User(String login, String password) {
        count++;
        this.id = count;
        this.roleId = 2;
        this.login = login;
        this.password = password;
        this.balance = BigDecimal.ZERO;
        this.banned = false;
    }

    static class UserBuilder {
        private int id;
        private int roleId;
        private String login;
        private String password;
        private BigDecimal balance;
        private boolean banned;

        public UserBuilder id(int id) {
            this.id = id;
            return this;
        }

        public UserBuilder roleId(int roleId) {
            this.roleId = roleId;
            return this;
        }

        public UserBuilder login(String login) {
            this.login = login;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder balance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public UserBuilder banned(boolean banned) {
            this.banned = banned;
            return this;
        }

        public User build() {
            if (this.id == 0) {
                return new User(
                        this.login,
                        this.password
                );
            }
            return new User(
                    this.id,
                    this.roleId,
                    this.login,
                    this.password,
                    this.balance,
                    this.banned
            );
        }
    }

    static UserBuilder builder() {
        return new UserBuilder();
    }

    public int getId() {
        return id;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public boolean isBanned() {
        return banned;
    }

    @Override
    public String toString() {
        return "User{" +
                super.toString() +
                ", balance=" + balance +
                ", banned=" + banned +
                '}';
    }
}

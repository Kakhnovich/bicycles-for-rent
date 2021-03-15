package com.epam.jwd.rent.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserFactory {
    private static final UserFactory INSTANCE = new UserFactory();
    private static final String ID_COLUMN_NAME = "id";
    private static final String STATUS_ID_COLUMN_NAME = "status_id";
    private static final String LOGIN_COLUMN_NAME = "login";
    private static final String PASSWORD_COLUMN_NAME = "password";
    private static final String BALANCE_COLUMN_NAME = "balance";
    private static final String IS_BANNED_COLUMN_NAME = "banned";

    private UserFactory() {
    }

    public static UserFactory getInstance() {
        return INSTANCE;
    }

    public User create(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getInt(ID_COLUMN_NAME))
                .roleId(resultSet.getInt(STATUS_ID_COLUMN_NAME))
                .login(resultSet.getString(LOGIN_COLUMN_NAME))
                .password(resultSet.getString(PASSWORD_COLUMN_NAME))
                .balance(resultSet.getBigDecimal(BALANCE_COLUMN_NAME))
                .banned(resultSet.getBoolean(IS_BANNED_COLUMN_NAME))
                .build();
    }

    public User create(String login, String password) {
        return User.builder()
                .login(login)
                .password(password)
                .build();
    }
}

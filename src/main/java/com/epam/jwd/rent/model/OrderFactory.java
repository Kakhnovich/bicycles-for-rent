package com.epam.jwd.rent.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class OrderFactory {
    private static final OrderFactory INSTANCE = new OrderFactory();
    private static final String ID_COLUMN_NAME = "id";
    private static final String USER_ID_COLUMN_NAME = "person_id";
    private static final String BICYCLE_ID_COLUMN_NAME = "bicycle_id";
    private static final String HOURS_COLUMN_NAME = "hours";
    private static final String STATUS_COLUMN_NAME = "status";
    private static final String DATE_TIME_COLUMN_NAME = "date";

    private OrderFactory() {
    }

    public static OrderFactory getInstance() {
        return INSTANCE;
    }

    public Order create(ResultSet resultSet) throws SQLException {
        return Order.builder()
                .id(resultSet.getInt(ID_COLUMN_NAME))
                .user_id(resultSet.getInt(USER_ID_COLUMN_NAME))
                .bicycle_id(resultSet.getInt(BICYCLE_ID_COLUMN_NAME))
                .hours(resultSet.getInt(HOURS_COLUMN_NAME))
                .status(resultSet.getString(STATUS_COLUMN_NAME))
                .date(resultSet.getDate(DATE_TIME_COLUMN_NAME).toLocalDate())
                .build();
    }

    public Order create(int user_id, int bicycle_id, int hours, LocalDate date) {
        return Order.builder()
                .user_id(user_id)
                .bicycle_id(bicycle_id)
                .hours(hours)
                .date(date)
                .build();
    }
}

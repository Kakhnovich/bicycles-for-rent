package com.epam.jwd.rent.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BicycleFactory {
    private static final BicycleFactory INSTANCE = new BicycleFactory();
    private static final String ID_COLUMN_NAME = "id";
    private static final String MODEL_COLUMN_NAME = "model";
    private static final String PRICE_COLUMN_NAME = "price";
    private static final String ADDRESS_COLUMN_NAME = "address";
    private static final String COUNT_COLUMN_NAME = "count";

    private BicycleFactory() {
    }

    public static BicycleFactory getInstance() {
        return INSTANCE;
    }

    public Bicycle create(ResultSet resultSet) throws SQLException {
        return Bicycle.builder()
                .id(resultSet.getInt(ID_COLUMN_NAME))
                .model(resultSet.getString(MODEL_COLUMN_NAME))
                .price(resultSet.getBigDecimal(PRICE_COLUMN_NAME))
                .place(resultSet.getString(ADDRESS_COLUMN_NAME))
                .count(resultSet.getInt(COUNT_COLUMN_NAME))
                .build();
    }

    public Bicycle create(String model, BigDecimal price, String place, int count) {
        return Bicycle.builder()
                .model(model)
                .price(price)
                .place(place)
                .count(count)
                .build();
    }
}

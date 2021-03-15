package com.epam.jwd.rent.model;

import java.math.BigDecimal;

public class BicycleDto {
    private final String model;
    private final BigDecimal price;
    private final String place;
    private final int count;

    public BicycleDto(String model, BigDecimal price, String place, int count) {
        this.model = model;
        this.price = price;
        this.place = place;
        this.count = count;
    }

    public String getModel() {
        return model;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getPlace() {
        return place;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "BicycleDto{" +
                "model='" + model + '\'' +
                ", price=" + price +
                ", place='" + place + '\'' +
                ", count=" + count +
                '}';
    }
}

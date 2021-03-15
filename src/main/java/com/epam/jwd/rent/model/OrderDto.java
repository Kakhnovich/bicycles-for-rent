package com.epam.jwd.rent.model;

import java.time.LocalDate;

public class OrderDto {
    private final String userName;
    private final String bicycleModel;
    private final int hours;
    private final String status;
    private final LocalDate date;

    public OrderDto(String userName, String bicycleModel, int hours, String status, LocalDate date) {
        this.userName = userName;
        this.bicycleModel = bicycleModel;
        this.hours = hours;
        this.status = status;
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public String getBicycleModel() {
        return bicycleModel;
    }

    public int getHours() {
        return hours;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getDate() {
        return date;
    }
}

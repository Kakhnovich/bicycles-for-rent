package com.epam.jwd.rent.model;

import java.time.LocalTime;

public class Place {
    private final String address;
    private final LocalTime openTime;
    private final LocalTime closeTime;

    public Place(String address, LocalTime openTime, LocalTime closeTime) {
        this.address = address;
        this.openTime = openTime.minusHours(3);
        this.closeTime = closeTime.minusHours(3);
    }

    public String getAddress() {
        return address;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }
}

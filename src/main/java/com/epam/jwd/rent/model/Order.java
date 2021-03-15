package com.epam.jwd.rent.model;

import java.time.LocalDate;

public class Order {
    public static int count = 0;
    private final int id;
    private final int user_id;
    private final int bicycle_id;
    private final int hours;
    private final String status;
    private final LocalDate date;

    private Order(int id, int user_id, int bicycle_id, int hours, String status, LocalDate date) {
        this.id = id;
        this.user_id = user_id;
        this.bicycle_id = bicycle_id;
        this.hours = hours;
        this.status = status;
        this.date = date;
    }

    private Order(int user_id, int bicycle_id, int hours, LocalDate date) {
        count++;
        this.id = count;
        this.user_id = user_id;
        this.bicycle_id = bicycle_id;
        this.hours = hours;
        this.status = "in processing";
        this.date = date;
    }

    static class OrderBuilder {
        private int id;
        private int user_id;
        private int bicycle_id;
        private int hours;
        private String status;
        private LocalDate date;

        public OrderBuilder id(int id) {
            this.id = id;
            return this;
        }

        public OrderBuilder user_id(int user_id) {
            this.user_id = user_id;
            return this;
        }

        public OrderBuilder bicycle_id(int bicycle_id) {
            this.bicycle_id = bicycle_id;
            return this;
        }

        public OrderBuilder hours(int hours) {
            this.hours = hours;
            return this;
        }

        public OrderBuilder status(String status) {
            this.status = status;
            return this;
        }

        public OrderBuilder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Order build() {
            if (this.id == 0) {
                return new Order(
                        this.user_id,
                        this.bicycle_id,
                        this.hours,
                        this.date
                );
            }
            return new Order(
                    this.id,
                    this.user_id,
                    this.bicycle_id,
                    this.hours,
                    this.status,
                    this.date
            );
        }
    }

    static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public static int getCount() {
        return count;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getBicycle_id() {
        return bicycle_id;
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

package com.epam.jwd.rent.model;

import java.math.BigDecimal;

public class Bicycle {
    public static int bicycleCount = 0;
    private final int id;
    private final String model;
    private final BigDecimal price;
    private final String place;
    private final int count;

    Bicycle(int id, String model, BigDecimal price, String place, int count) {
        this.id = id;
        this.model = model;
        this.place = place;
        this.price = price;
        this.count = count;
    }

    Bicycle(String model, BigDecimal price, String place, int count) {
        bicycleCount++;
        this.id = bicycleCount;
        this.model = model;
        this.place = place;
        this.price = price;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getPlace() {
        return place;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }

    static class BicycleBuilder {
        private int id;
        private String model;
        private BigDecimal price;
        private String place;
        private int count;

        public BicycleBuilder id(int id) {
            this.id = id;
            return this;
        }

        public BicycleBuilder model(String model) {
            this.model = model;
            return this;
        }

        public BicycleBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public BicycleBuilder place(String place) {
            this.place = place;
            return this;
        }

        public BicycleBuilder count(int count) {
            this.count = count;
            return this;
        }

        public Bicycle build() {
            if (this.id == 0) {
                return new Bicycle(
                        this.model,
                        this.price,
                        this.place,
                        this.count);
            }
            return new Bicycle(
                    this.id,
                    this.model,
                    this.price,
                    this.place,
                    this.count);
        }
    }

    static BicycleBuilder builder() {
        return new BicycleBuilder();
    }

    @Override
    public String toString() {
        return "Bicycle{" +
                "model='" + model +
                ", place='" + place +
                ", price=" + price +
                ", count=" + count +
                '}';
    }
}

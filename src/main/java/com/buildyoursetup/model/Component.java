package com.buildyoursetup.model;

import com.buildyoursetup.patterns.visitor.ComponentVisitor;

public abstract class Component {

    private final String name;
    private final String brand;
    private final double price;

    public Component(String name, String brand, double price) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException("Brand cannot be empty");
        }

        if (price < 0 || !Double.isFinite(price)) {
            throw new IllegalArgumentException("Invalid price");
        }

        this.name = name;
        this.brand = brand;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return brand + " " + name + " - " + price + " KZT";
    }

    public abstract void accept(ComponentVisitor visitor);
}

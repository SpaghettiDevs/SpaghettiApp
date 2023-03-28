package com.bitebybyte.backend.local;

public class Ingredient {
    private String name;
    private int amount;
    private String measurement;

    public Ingredient() {
        this.name = "";
        this.amount = 0;
        this.measurement = "";
    }

    public Ingredient(String name, int amount, String measurement) {
        this.name = name;
        this.amount = amount;
        this.measurement = measurement;
    }

    public int getAmount() {
        return amount;
    }

    public String getMeasurement() {
        return measurement;
    }

    public String getName() {
        return name;
    }
}

package com.bitebybyte.backend.models;

/**

 A class representing an ingredient, including its name, amount, and measurement.
 CURRENTLY UNUSED AS INGREDIENT BECAME A STRING INSTEAD OF A CLASS
 */
public class Ingredient {
    // The name of the ingredient
    private final String name;
    // The amount of the ingredient
    private final int amount;
    // The measurement for the amount (e.g. "cups", "teaspoons")
    private final String measurement;

    /**

     Constructs a new Ingredient object with default values.
     */
    public Ingredient() {
        this.name = "";
        this.amount = 0;
        this.measurement = "";
    }
    /**

     Constructs a new Ingredient object with the specified name, amount, and measurement.
     @param name the name of the ingredient
     @param amount the amount of the ingredient
     @param measurement the measurement for the amount (e.g. "cups", "teaspoons")
     */
    public Ingredient(String name, int amount, String measurement) {
        this.name = name;
        this.amount = amount;
        this.measurement = measurement;
    }
    /**

     Gets the amount of the ingredient.
     @return the amount of the ingredient
     */
    public int getAmount() {
        return amount;
    }
    /**

     Gets the measurement for the amount (e.g. "cups", "teaspoons").
     @return the measurement for the amount
     */
    public String getMeasurement() {
        return measurement;
    }
    /**

     Gets the name of the ingredient.
     @return the name of the ingredient
     */
    public String getName() {
        return name;
    }
}
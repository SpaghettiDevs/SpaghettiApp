package com.bitebybyte.backend.models;

/**
 * A class representing a recipe, including its methods, ingredients, and preparation time.
 */
public class Recipe {
    // The methods for preparing the recipe
    private final String methods;
    // The ingredients required for the recipe
    private final String ingredients;
    // The preparation time for the recipe, in minutes
    private final int preparationTime;
    // The scale for the preparation time (e.g. "minutes", "hours")
    private final String preparationTimeScale;

    /**
     * Constructs a new Recipe object with default values.
     */
    public Recipe() {
        methods = "";
        preparationTime = 0;
        ingredients = "";
        preparationTimeScale = "minutes";
    }

    /**
     * Constructs a new Recipe object with the specified methods, ingredients, preparation time, and time scale.
     *
     * @param methods              the methods for preparing the recipe
     * @param ingredients          the ingredients required for the recipe
     * @param preparationTime      the preparation time for the recipe
     * @param preparationTimeScale the scale for the preparation time (e.g. "minutes", "hours")
     */
    public Recipe(String methods, String ingredients, int preparationTime, String preparationTimeScale) {
        this.methods = methods;
        this.ingredients = ingredients;
        this.preparationTime = preparationTime;
        this.preparationTimeScale = preparationTimeScale;
    }

    /**
     * Gets the preparation time for the recipe, in minutes.
     *
     * @return the preparation time for the recipe, in minutes
     */
    public int getPreparationTime() {
        return preparationTime;
    }

    /**
     * Gets the ingredients required for the recipe.
     *
     * @return the ingredients required for the recipe
     */
    public String getIngredients() {
        return ingredients;
    }

    /**
     * Gets the methods for preparing the recipe.
     *
     * @return the methods for preparing the recipe
     */
    public String getMethods() {
        return methods;
    }

    /**
     * Gets the scale for the preparation time (e.g. "minutes", "hours").
     *
     * @return the scale for the preparation time
     */
    public String getPreparationTimeScale() {
        return preparationTimeScale;
    }
}
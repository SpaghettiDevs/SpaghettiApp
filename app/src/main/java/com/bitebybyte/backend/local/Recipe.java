package com.bitebybyte.backend.local;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String methods;
    //private List<Ingredient> ingredients;
    private String ingredients;
    private int preparationTime;
    private String preparationTimeScale;

    public Recipe() {
        methods = "";
        preparationTime = 0;
        //ingredients = new ArrayList<>();
        ingredients = "";
        preparationTimeScale = "minutes";
    }

    public Recipe(String methods, String ingredients, int preparationTime) {
        this.methods = methods;
        this.ingredients = ingredients;
        this.preparationTime = preparationTime;
    }

    public Recipe(String methods, String ingredients, int preparationTime, String preparationTimeScale) {
        this.methods = methods;
        this.ingredients = ingredients;
        this.preparationTime = preparationTime;
        this.preparationTimeScale = preparationTimeScale;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getMethods() {
        return methods;
    }

    public String getPreparationTimeScale() {
        return preparationTimeScale;
    }
}

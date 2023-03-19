package com.bitebybyte.backend.local;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String methods;
    private List<Ingredient> ingredients;
    private int preparationTime;

    public Recipe() {
        methods = "";
        preparationTime = 0;
        ingredients = new ArrayList<>();
    }

    public Recipe(String methods, List<Ingredient> ingredients, int preparationTime) {
        this.methods = methods;
        this.ingredients = ingredients;
        this.preparationTime = preparationTime;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getMethods() {
        return methods;
    }
}

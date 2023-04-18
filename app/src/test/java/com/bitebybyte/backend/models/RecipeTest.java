package com.bitebybyte.backend.models;

import static org.junit.Assert.*;
import org.junit.Test;

public class RecipeTest {

    private final String methods = "test methods";

    private final String ingredients = "test fruit";

    private final int preparationTime = 10;

    private final String preparationTimeScale = "minutes";
    private final Recipe instance = new Recipe(methods, ingredients, preparationTime, preparationTimeScale);


    /* Test for checking that the method is not null when instantiated. */
    @Test
    public void getMethodNullTest() {
        assertNotNull(instance.getMethods());
    }

    /* Test for checking that the ingredients is not null when instantiated. */
    @Test
    public void getIngredientsNullTest() {
        assertNotNull(instance.getIngredients());
    }

    /* Test for checking that the preparation time scale is not null when instantiated. */
    @Test
    public void getPreparationTimeScaleNullTest() {
        assertNotNull(instance.getPreparationTimeScale());
    }

    /* Test for checking if the getMethods returns the expected value. */
    @Test
    public void getMethodsTest() {
        assertEquals(methods, instance.getMethods());
    }

    /* Test for checking if the getIngredients returns the expected value. */
    @Test
    public void getIngredientsTest() {
        assertEquals(ingredients, instance.getIngredients());
    }

    /* Test for checking if the getPreparationTime returns the expected value. */
    @Test
    public void getPreparationTimeTest() {
        assertEquals(preparationTime, instance.getPreparationTime());
    }

    /* Test for checking if the getPreparationTimeScale returns the expected value. */
    @Test
    public void getPreparationTimeScaleTest() {
        assertEquals(preparationTimeScale, instance.getPreparationTimeScale());
    }
}

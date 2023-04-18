package com.bitebybyte.backend.models;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FeedPostTest {
    private final String ownerId = "tester";
    private final String content = "testing content";
    private final String title = "testing title";
    private final String images = "test image";
    private final List<String> labels = new ArrayList<String>() {{
        add("label 1");
        add("label 2");
    } };
    private final Recipe recipe = new Recipe();
    private final FeedPost instance = new FeedPost(ownerId, content, title, images, labels, recipe);

    /* Test for checking that the content is not null when instantiated. */
    @Test
    public void getContentNullTest() {
        assertNotNull(instance.getContent());
    }

    /* Test for checking that the owner id is not null when instantiated. */
    @Test
    public void getOwnerIdNullTest() {
        assertNotNull(instance.getIdOwner());
    }

    /* Test for checking that the Date is not null when instantiated. */
    @Test
    public void getDateNullTest() {
        assertNotNull(instance.getDate());
    }

    /* Test for checking that the likes counter is not null when instantiated. */
    @Test
    public void getLikesNullTest() {
        assertNotNull(instance.getLikes());
    }

    /* Test for checking that the Post id is not null when instantiated. */
    @Test
    public void getPostIdNullTest() {
        assertNotNull(instance.getPostId());
    }

    /* Test for checking that the image is not null when instantiated. */
    @Test
    public void getImagesNullTest() {
        assertNotNull(instance.getImages());
    }

    /* Test for checking that the labels list is not null when instantiated. */
    @Test
    public void getLabelsNullTest() {
        assertNotNull(instance.getLabels());
    }

    /* Test for checking that the title is not null when instantiated. */
    @Test
    public void getTitleNullTest() {
        assertNotNull(instance.getTitle());
    }

    /* Test for checking that the recipe is not null when instantiated. */
    @Test
    public void getRecipeNullTest() {
        assertNotNull(instance.getRecipe());
    }

    /* Test for checking that the comments list is not null when instantiated. */
    @Test
    public void getCommentsNullTest() {
        assertNotNull(instance.getComments());
    }

    /* Test for checking if getTitle returns the expected value. */
    @Test
    public void getTitleTest() {
        assertEquals(title, instance.getTitle());
    }

    /* Test for checking if getRecipe returns the expected value. */
    @Test
    public void getRecipeTest() {
        assertEquals(recipe, instance.getRecipe());
    }

    /* Test for checking if getComments returns the expected value. */
    @Test
    public void getCommentsTest() {
        assertEquals(0, instance.getComments().size());
    }

    /* Test for checking if getImages returns the expected value. */
    @Test
    public void getImagesTest() {
        assertEquals(images, instance.getImages());
    }

    /* Test for checking if getLabels returns the expected value. */
    @Test
    public void getLabelsTest() {
        assertEquals(labels, instance.getLabels());
    }

    /* Test for checking if getContent returns the expected value. */
    @Test
    public void getContentTest() {
        assertEquals(content, instance.getContent());
    }

    /* Test for checking if getOwnerId returns the expected value. */
    @Test
    public void getOwnerIdTest() {
        assertEquals(ownerId, instance.getIdOwner());
    }
}

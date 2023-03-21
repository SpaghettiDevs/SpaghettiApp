package com.bitebybyte;

import org.junit.Test;

import static org.junit.Assert.*;

import android.media.Image;

import com.bitebybyte.backend.database.PostService;
import com.bitebybyte.backend.local.Ingredient;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PostServiceTest {

    private PostService service;
    public PostServiceTest() {
        System.out.println("before FirebaseApp");
        //FirebaseApp.initializeApp();
        System.out.println("after FirebaseApp before service");
        service = new PostService();
        System.out.println("after service");

    }

    // Testing with crate post requires to check manually what in the database!
    private void testCreatePost (String idOwner, String content, String title,
                                 List<Image> images, List<String> labels,
                                 String methods, List<Ingredient> ingredients, int preparationTime) {

        service.createPostWithRecipe(idOwner, content, title, images,
                labels, methods, ingredients, preparationTime);
    }

    @Test
    public void createNulls() {
        this.testCreatePost("", "", "", null,
                null, "", null, 0);
    }

    @Test
    public void createWithLabels() {
        List<String> labels = new ArrayList<>();
        labels.add("label 1");
        labels.add("label 2");
        this.testCreatePost("", "", "", null,
                labels, "", null, 0);
    }

    @Test
    public void createWithNoImagesNoIngredients() {
        List<String> labels = new ArrayList<>();
        labels.add("label 1");
        labels.add("label 2");
        labels.add("label 3");

        this.testCreatePost("randomOwnerID", "content will have 3 labels", "random recipe", null,
                labels, "No methods for with no ingredients", null, 0);
    }

    @Test
    public void createTwoPosts() {
        this.testCreatePost("USER1", "", "recipe1", null,
                null, "", null, 5);
        this.testCreatePost("USER1", "", "recipe2", null,
                null, "", null, 15);
    }

    @Test
    public void createTwoPostsTwoUsers() {
        this.testCreatePost("USER2.1", "", "recipe1", null,
                null, "", null, 5);
        this.testCreatePost("USER2.2", "", "recipe2", null,
                null, "", null, 15);
    }
}
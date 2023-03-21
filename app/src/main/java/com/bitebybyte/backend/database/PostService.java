package com.bitebybyte.backend.database;

import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bitebybyte.backend.local.FeedPost;
import com.bitebybyte.backend.local.Ingredient;
import com.bitebybyte.backend.local.Recipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PostService implements OnSuccessListener, OnFailureListener{
    private static final String TAG = "Database operation: ";
    private FeedPost post = null;

    PostService() { }

    PostService(FeedPost post) { this.post = post; }

    public void saveToDatabase(FeedPost post) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        this.post = post;

        db.collection("posts")
                .document(post.getPostId()).set(post)
                .addOnSuccessListener(this)
                .addOnFailureListener(this);
    }

    public void createPostWithRecipe (String idOwner, String content, String title,
                                      List<Image> images, List<String> labels,
                                      String methods, List<Ingredient> ingredients, int preparationTime) {
        Recipe recipe = createRecipe(methods, ingredients, preparationTime);
        createPost(idOwner, content, title, images, labels, recipe);
    }

    public void createPost(String idOwner, String content, String title,
                           List<Image> images, List<String> labels, Recipe recipe) {

        FeedPost post = new FeedPost(idOwner, content, title,
                images, labels, recipe);

        this.saveToDatabase(post);
    }

    public Recipe createRecipe(String methods, List<Ingredient> ingredients, int preparationTime) {
        return new Recipe(methods, ingredients, preparationTime);
    }

    @Override
    public void onSuccess(Object o) {
        Log.d(TAG, "Post with ID" + post.getPostId() + "successfully added to database!");
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.w(TAG, "Error adding the post to the database", e);
    }
}

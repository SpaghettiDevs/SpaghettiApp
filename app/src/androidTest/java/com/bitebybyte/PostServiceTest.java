package com.bitebybyte;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bitebybyte.backend.models.FeedPost;
import com.bitebybyte.backend.models.Recipe;
import com.bitebybyte.backend.services.PostService;
import com.bitebybyte.backend.services.UserService;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

import java.util.ArrayList;


@RunWith(AndroidJUnit4.class)
public class PostServiceTest {
    private FirebaseFirestore db;
    private FirebaseStorage dbStore;
    private FirebaseAuth auth;
    private UserService userService;
    private PostService postService;

    @Before
    public void setUp() {
        db = FirebaseFirestore.getInstance();
        dbStore = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        userService = new UserService();
        postService = new PostService();
    }

    @Test
    public void testSaveToDatabase() {

        // Create a mock FeedPost object to save
        FeedPost post = new FeedPost("test", "test content",
                        "test title", "image",new ArrayList<>(), new Recipe());

        // Save the post to the database
        postService.saveToDatabase(post, "test-posts");

        // Query the database to verify that the post was saved
        Task<QuerySnapshot> task = db.collection("test-posts")
                .whereEqualTo("postId", post.getPostId())
                .get().addOnFailureListener(e -> {
                    fail("Failed to query database: " + e.getMessage());
                });

        while (!task.isComplete()){}

        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
            FeedPost retrievedPost = documentSnapshot.toObject(FeedPost.class);
            assertEquals(post.getPostId(), retrievedPost.getPostId());
        }
    }
}

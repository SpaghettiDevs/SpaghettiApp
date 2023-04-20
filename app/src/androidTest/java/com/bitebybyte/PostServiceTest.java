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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.List;


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

    /////////////// creating posts //////////////////

    /* Test with all empty values in the post*/
    @Test
    public void CreatingPostTest1() {
        //creating a post with empty values
        String postId = postService.createPost("", "", "", "", new ArrayList<>(), new Recipe(), "test-posts");

        // Query the database to verify that the post was saved
        Task<QuerySnapshot> task = db.collection("test-posts")
                .whereEqualTo("postId", postId)
                .get().addOnFailureListener(e -> {
                    fail("Failed to query database: " + e.getMessage());
                });

        //waiting for the query to complete
        while (!task.isComplete()) {
        }

        //checking if all the fields have the right value
        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
            FeedPost retrievedPost = documentSnapshot.toObject(FeedPost.class);
            assertEquals(postId, retrievedPost.getPostId());
            assertEquals("", retrievedPost.getImages());
            assertEquals("", retrievedPost.getRecipe().getMethods());
            assertEquals("minutes", retrievedPost.getRecipe().getPreparationTimeScale());
            assertEquals("", retrievedPost.getRecipe().getIngredients());
            assertEquals(0, retrievedPost.getRecipe().getPreparationTime());
            assertEquals("", retrievedPost.getTitle());
            assertEquals("", retrievedPost.getContent());
            assertEquals(0, retrievedPost.getLabels().size());
            assertEquals(0, retrievedPost.getLikes().size());
            assertEquals("", retrievedPost.getIdOwner());
            assertEquals(0, retrievedPost.getComments().size());
        }

        resetTestEnvironment();
    }

    /* Test with normal case values in the post*/
    @Test
    public void CreatingPostTest2() {
        String idOwner = "tester";
        String content = "cooking potato";
        String title = "potato";
        String images = "";
        List<String> labels = new ArrayList<String>() {{
            add("cooking");
        }};
        Recipe recipe = new Recipe("methods", "potato", 6, "minutes");

        //creating a post
        String postId = postService.createPost(idOwner, content, title, images, labels, recipe, "test-posts");

        // Query the database to verify that the post was saved
        Task<QuerySnapshot> task = db.collection("test-posts")
                .whereEqualTo("postId", postId)
                .get().addOnFailureListener(e -> {
                    fail("Failed to query database: " + e.getMessage());
                });

        //waiting for the query to complete
        while (!task.isComplete()) {
        }

        //checking if all the fields have the right value
        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
            FeedPost retrievedPost = documentSnapshot.toObject(FeedPost.class);
            assertEquals(postId, retrievedPost.getPostId());
            assertEquals("", retrievedPost.getImages());
            assertEquals("methods", retrievedPost.getRecipe().getMethods());
            assertEquals("minutes", retrievedPost.getRecipe().getPreparationTimeScale());
            assertEquals("potato", retrievedPost.getRecipe().getIngredients());
            assertEquals(6, retrievedPost.getRecipe().getPreparationTime());
            assertEquals("potato", retrievedPost.getTitle());
            assertEquals("cooking potato", retrievedPost.getContent());
            assertEquals("cooking", retrievedPost.getLabels().get(0));
            assertEquals(0, retrievedPost.getLikes().size());
            assertEquals("tester", retrievedPost.getIdOwner());
            assertEquals(0, retrievedPost.getComments().size());
        }

        resetTestEnvironment();
    }

    /* Test with null values in the post*/
    @Test
    public void CreatingPostTest3() {
        String idOwner = null;
        String content = "cooking potato";
        String title = null;
        String images = "";
        List<String> labels = new ArrayList<String>() {{
            add("cooking");
        }};
        Recipe recipe = new Recipe("methods", null, 6, "minutes");

        assertThrows(IllegalArgumentException.class, () -> {
            //creating a post
            postService.createPost(idOwner, content, title, images, labels, recipe, "test-posts");
        });

        resetTestEnvironment();
    }
    /////////////////////////////////////////////////

    /////////////// deleting a post /////////////////

    /* normal case test for deleting a post */
    @Test
    public void DeletingPostTest1() {
        //creating a post for deletion
        FeedPost post = new FeedPost("", "", "", "", new ArrayList<>(), new Recipe());
        db.collection("test-posts").document(post.getPostId()).set(post);

        //call the delete method from postService
        postService.deletePost(post.getPostId(), "test-posts");

        // Query the database to verify that the post was saved
        Task<QuerySnapshot> task = db.collection("test-posts")
                .whereEqualTo("postId", post.getPostId())
                .get();

        //waiting for the query to complete
        while (!task.isComplete()) {
        }

        assertEquals(true, task.getResult().isEmpty());

        resetTestEnvironment();
    }

    /* try deleting a post that doesn't exist */
    @Test
    public void DeletingPostTest2() {
        assertThrows(IllegalArgumentException.class, () -> {
            //call the delete method from postService
                postService.deletePost("not a real post", "test-posts");

        });
        resetTestEnvironment();
    }
    /////////////////////////////////////////////////

    /////////////// updating likes //////////////////

    /*testing when the user has not liked the post yet*/
    @Test
    public void updatingLikesTest1() {
        //creating post to update likes on
        FeedPost post = new FeedPost("like-person", "like", "liking", "", new ArrayList<>(), new Recipe());
        db.collection("test-posts").document(post.getPostId()).set(post);

        //using the method under test
        postService.updateLikes(post, "test-posts");

        // Query the database to verify that the post was liked
        Task<QuerySnapshot> task = db.collection("test-posts")
                .whereEqualTo("postId", post.getPostId())
                .get();

        //waiting for the query to complete
        while (!task.isComplete()) {}

        //checking if all the fields have the right value
        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
            FeedPost retrievedPost = documentSnapshot.toObject(FeedPost.class);
            assertTrue(retrievedPost.getLikes().containsKey(auth.getCurrentUser().getUid()));
        }

        resetTestEnvironment();
    }

    /*testing when the user has liked the post*/
    @Test
    public void updatingLikesTest2() {
        //creating post to update likes on
        FeedPost post = new FeedPost("like-person", "like", "liking", "", new ArrayList<>(), new Recipe());
        db.collection("test-posts").document(post.getPostId()).set(post);

        //liking the post
        postService.updateLikes(post, "test-posts");

        // Query the database to verify that the post was liked
        Task<QuerySnapshot> task = db.collection("test-posts")
                .whereEqualTo("postId", post.getPostId())
                .get();

        //waiting for the query to complete
        while (!task.isComplete()) {}

        //checking if all the fields have the right value
        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
            FeedPost retrievedPost = documentSnapshot.toObject(FeedPost.class);
            assertTrue(retrievedPost.getLikes().containsKey(auth.getCurrentUser().getUid()));
        }

        //disliking the post
        postService.updateLikes(post, "test-posts");

        // Query the database to verify that the post was disliked
        Task<QuerySnapshot> task2 = db.collection("test-posts")
                .whereEqualTo("postId", post.getPostId())
                .get();

        //waiting for the query to complete
        while (!task2.isComplete()) {}

        //checking if all the fields have the right value
        for (QueryDocumentSnapshot documentSnapshot : task2.getResult()) {
            FeedPost retrievedPost = documentSnapshot.toObject(FeedPost.class);
            assertFalse(retrievedPost.getLikes().containsKey(auth.getCurrentUser().getUid()));
        }

        resetTestEnvironment();
    }
    /////////////////////////////////////////////////

    ////////////// adding comments //////////////////

    /*normal case of adding a comment*/
    @Test
    public void addingCommentTest1() {
        //creating post to update likes on
        FeedPost post = new FeedPost("", "", "", "", new ArrayList<>(), new Recipe());
        db.collection("test-posts").document(post.getPostId()).set(post);

        //using the method under test
        postService.addComment(post, "comment testing", "test-posts");

        // Query the database to verify that the post was liked
        Task<QuerySnapshot> task = db.collection("test-posts")
                .whereEqualTo("postId", post.getPostId())
                .get();

        //waiting for the query to complete
        while (!task.isComplete()) {}

        //checking if all the fields have the right value
        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
            FeedPost retrievedPost = documentSnapshot.toObject(FeedPost.class);
            assertEquals("comment testing", retrievedPost.getComments().get(0).getContent());
        }

        resetTestEnvironment();
    }

    /*trying to add a comment with and nothing in it*/
    @Test
    public void addingCommentTest2() {
        //creating post to update likes on
        FeedPost post = new FeedPost("", "", "", "", new ArrayList<>(), new Recipe());
        db.collection("test-posts").document(post.getPostId()).set(post);

        //using the method under test
        postService.addComment(post, "", "test-posts");

        // Query the database to verify that the post was liked
        Task<QuerySnapshot> task = db.collection("test-posts")
                .whereEqualTo("postId", post.getPostId())
                .get();

        //waiting for the query to complete
        while (!task.isComplete()) {}

        //checking if all the fields have the right value
        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
            FeedPost retrievedPost = documentSnapshot.toObject(FeedPost.class);
            assertEquals("", retrievedPost.getComments().get(0).getContent());
        }

        resetTestEnvironment();
    }

    /*trying to add a comment with a null value*/
    @Test
    public void addingCommentTest3() {
        //creating post to update likes on
        FeedPost post = new FeedPost("", "", "", "", new ArrayList<>(), new Recipe());
        db.collection("test-posts").document(post.getPostId()).set(post);

        assertThrows(IllegalArgumentException.class, () -> {
            //call the add comment method
            postService.addComment(post, null, "test-posts");
        });

        resetTestEnvironment();
    }
    /////////////////////////////////////////////////

    ////////////// deleting comments ////////////////
    /////////////////////////////////////////////////

    /////////// updating comment likes //////////////
    /////////////////////////////////////////////////

    /////////////// uploading image /////////////////
    /////////////////////////////////////////////////

    /////////////// date formatting /////////////////
    /////////////////////////////////////////////////

    ////////////// (optional) testing date desceinding when getting all posts ////////////
    ////////////////////////////////////////////////

    //////////// private helper methods /////////////

    private void resetTestEnvironment() {
        List<String> allPosts = new ArrayList<>();
        Task<QuerySnapshot> task = db.collection("test-posts").get();

        //wait for query to finish
        while (!task.isComplete()) {
        }

        for (QueryDocumentSnapshot doc : task.getResult()) {
            FeedPost post = doc.toObject(FeedPost.class);
            allPosts.add(post.getPostId());
        }

        for (String postId : allPosts) {
            Task<Void> task2 = db.collection("test-posts").document(postId).delete();
            while (!task2.isComplete()) {}
        }
        /////////////////////////////////////////////////
    }
}

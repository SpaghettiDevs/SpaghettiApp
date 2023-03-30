package com.bitebybyte.backend.database;

import androidx.annotation.NonNull;

import com.bitebybyte.backend.local.FeedPost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService implements OnSuccessListener, OnFailureListener {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private PostService postService;
    public UserService() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        postService = new PostService();
    }

    //get the username from the database for the current user.
    public String getUsername() {
        Task<DocumentSnapshot> task = db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .get();
        //wait for the query to finish, since its async.
        while (!task.isComplete()) {}

        return task.getResult().getData().get("username").toString();

    }

    //get the posts created by the current user from the database.
    public List<String> getMyPosts() {
        Task<DocumentSnapshot> task = db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .get();
        //wait for the query to finish, since its async.
        while (!task.isComplete()) {}

        return (List<String>) task.getResult().getData().get("myPosts");
    }

    //get the saved posts of the current user from the database.
    public List<String> getSavedPosts() {
        Task<DocumentSnapshot> task = db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .get();
        //wait for the query to finish, since its async.
        while (!task.isComplete()) {}

        return (List<String>) task.getResult().getData().get("savedPosts");

    }

    //check if an existent user already has the same username.
    //true if username doesn't exist, false otherwise
    public boolean usernameCheck(String username) {
        //query that looks through the users collection and checks the username field
        Task<QuerySnapshot> task = db.collection("users")
                .whereEqualTo("username", username)
                .get();

        //wait for the query to finish, since its async.
        while (!task.isComplete()) {}

        return task.getResult().isEmpty();
    }

    //updating the posts the user creates
    public void updateMyPosts(String postId) {
        List<String> myPosts = getMyPosts();
        DocumentReference postRef = db.collection("users").document(auth.getCurrentUser().getUid());
        if (myPosts.contains(postId)) {
            myPosts.remove(postId);
        } else {myPosts.add(postId);}

            postRef
                    .update("myPosts", myPosts)
                    .addOnSuccessListener(this)
                    .addOnFailureListener(this);
    }

    //updating the posts the user creates
    public void updateSavedPosts(FeedPost post) {
        List<String> savedPosts = getSavedPosts();
        DocumentReference postRef = db.collection("users").document(auth.getCurrentUser().getUid());
        if (savedPosts.contains(post.getPostId())) {
            savedPosts.remove(post.getPostId());
        } else {savedPosts.add(post.getPostId());}

        postRef
                .update("savedPosts", savedPosts)
                .addOnSuccessListener(this)
                .addOnFailureListener(this);
    }

    //save a new user to the database
    public void saveNewUserToDb(String username, String userId) {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("username", username);
        data.put("myPosts", new ArrayList<>());
        data.put("savedPosts", new ArrayList<>());

        db.collection("users")
                .document(userId).set(data)
                .addOnSuccessListener(this)
                .addOnFailureListener(this);
    }

    @Override
    public void onSuccess(Object o) {
        System.out.println("Successfully added");
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        System.out.println("Failed add" + e);
    }
}

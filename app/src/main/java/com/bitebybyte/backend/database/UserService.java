package com.bitebybyte.backend.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bitebybyte.backend.local.FeedPost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService implements OnSuccessListener, OnFailureListener {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    public UserService() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    //get the username from the database for the current user.
    public String getUsername() {
        Task<DocumentSnapshot> task = db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .get();
        //wait for the query to finish, since its async.
        while (!task.isComplete()) {}

        Map<String, Object> data = task.getResult().getData();

        if(data != null && data.get("username") != null)
            return data.get("username").toString();
        else
            return "Unknown user";

    }

    //get the posts created by the current user from the database.
    public List<String> getMyPosts() {
        Task<DocumentSnapshot> task = db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .get();
        //wait for the query to finish, since its async.
        while (!task.isComplete()) {}

        Map<String, Object> data = task.getResult().getData();

        if(data != null && data.get("myPosts") != null)
            return (List<String>) data.get("myPosts");
        else
            return new ArrayList<>();
    }

    //get the saved posts of the current user from the database.
    public List<String> getSavedPosts() {
        Task<DocumentSnapshot> task = db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .get();
        //wait for the query to finish, since its async.
        while (!task.isComplete()) {}

        Map<String, Object> data = task.getResult().getData();

        if(data != null && data.get("savedPosts") != null)
            return (List<String>) data.get("savedPosts");
        else
            return new ArrayList<>();

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
        } else if (myPosts.size() != 0) { // add(index, element) throws an exception if index out of array bound
                myPosts.add(0, postId); // add the new post to the to the beginning.
        } else {
            myPosts.add(postId);
        }


            postRef.update("myPosts", myPosts)
                    .addOnSuccessListener(this)
                    .addOnFailureListener(this);
    }

    private void updateSavedPosts(List<String> savedPosts)
    {
        DocumentReference postRef = db.collection("users").document(auth.getCurrentUser().getUid());
        postRef.update("savedPosts", savedPosts)
                .addOnSuccessListener(this)
                .addOnFailureListener(this);
    }

    //updating the posts the user creates
    public String updateSavedPosts(String postId) {
        String msg = "";
        List<String> savedPosts = getSavedPosts();
        if (savedPosts.contains(postId)) {
            savedPosts.remove(postId);
            msg = "Recipe unsaved";
        } else {
            savedPosts.add(postId);
            msg = "Recipe saved";
        }
        updateSavedPosts(savedPosts);
        return msg;
    }

    /**
     * Delete SavedPost on given index.
     *
     * @param index
     * @return
     */
    public String updateSavedPosts(int index) {
        List<String> savedPosts = getSavedPosts();
        savedPosts.remove(index);

        updateSavedPosts(savedPosts);
        return "Recipe unsaved";
    }

    /**
     * Checks if the user has saved a post
     * @param postId the post id to check
     * @return true if the user has saved the post, false otherwise
     */
    public boolean userSavedPost(String postId) {
        List<String> savedPosts = getSavedPosts();
        return savedPosts.contains(postId);
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

    public void deleteUser(String userId) {
        DocumentReference docRef = db.collection("users").document(userId);

        //Delete all fields first
        Map<String,Object> updates = new HashMap<>();
        updates.put("userId", FieldValue.delete());
        updates.put("username", FieldValue.delete());
        updates.put("myPosts", FieldValue.delete());
        updates.put("savedPosts", FieldValue.delete());

        docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                db.collection("users").document(userId).delete();
            }
        });
    }

    public void changeUsername(String userId, String newUsername) {
        if (newUsername.isEmpty() || newUsername.length() > 16) {
            Log.v("", "Error: username length is empty or longer than 16 characters");
            return;
        }

        DocumentReference docRef = db.collection("users").document(userId);

        //Override old username
        Map<String,Object> updates = new HashMap<>();
        updates.put("username", newUsername);

        docRef.update(updates);
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

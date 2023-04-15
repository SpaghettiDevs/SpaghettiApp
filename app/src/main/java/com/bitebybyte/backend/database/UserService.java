package com.bitebybyte.backend.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bitebybyte.ServiceableUserFragment;
import com.bitebybyte.backend.local.User;
import com.bitebybyte.ui.home.HomeFeedAdapter;
import com.bitebybyte.ui.post.PostCommentsAdapter;
import com.bitebybyte.ui.saved.ViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService implements OnSuccessListener, OnFailureListener {

    private static final String collection = "users";
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private static User currentUser = null;
    public UserService() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            setCurrentUserInstance();
        }
    }

    private void setCurrentUserInstance() {
        if (currentUser == null || auth.getCurrentUser().getUid() != currentUser.getUserId()) {
            String currentUserId = auth.getCurrentUser().getUid();
            DocumentReference reference = db.collection(collection).document(currentUserId);

            System.out.println(currentUserId);

            Task<DocumentSnapshot> task = reference.get();

            while(!task.isComplete()) {} // wait for the task to be completed

            DocumentSnapshot documentSnapshot = task.getResult();
            currentUser = documentSnapshot.toObject(User.class);
            Log.v("Firebase", "Current user fetched successfully");

        }
    }

    public String getCurrentUserId() {
        return currentUser.getUserId();
    }


    //get the username from the database for the current user.
    public String getCurrentUsername() {
        return currentUser.getUsername();
    }

    /**
     * Get user object given userId.
     * This method is async and sends a callback.
     *
     * @param userId
     * @param fragment the callback
     */
    public void getUser(String userId, ServiceableUserFragment fragment) {

        db.collection(collection).document(userId)
                .get().addOnCompleteListener(task -> {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (!documentSnapshot.exists()) {
                        Log.e("Firebase", "This user is deleted!");
                        // TODO what happens to the posts of a user account is removed
                        return;
                    }

                    Log.d("Firebase", "User data fetched successfully!");
                    fragment.addUserData(documentSnapshot.toObject(User.class));
                });
    }

    /**
     * Get user object given userId.
     * This method is async and sends a callback with the holder to be updated.
     *
     * @param userId
     * @param holder this parameter will be updated with the user data.
     * @param fragment the callback
     */
    public void getUser(String userId, HomeFeedAdapter.ViewHolder holder,
                        ServiceableUserFragment fragment) {

        db.collection(collection).document(userId)
                .get().addOnCompleteListener(task -> {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (!documentSnapshot.exists()) {
                        Log.e("Firebase", "This user is deleted!");
                        // TODO what happens to the posts of a user account is removed
                        return;
                    }

                    Log.d("Firebase", "User data fetched successfully!");
                    fragment.addUserData(documentSnapshot.toObject(User.class), holder);
                });
    }

    /**
     * Get user object given userId.
     * This method is async and sends a callback with the holder to be updated.
     *
     * @param userId
     * @param holder this parameter will be updated with the user data.
     * @param fragment the callback
     */
    public void getUser(String userId, PostCommentsAdapter.ViewHolder holder, ServiceableUserFragment fragment) {
        db.collection(collection).document(userId)
                .get().addOnCompleteListener(task -> {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (!documentSnapshot.exists()) {
                        Log.e("Firebase", "This user is deleted!");
                        // TODO what happens to the posts of a user account is removed
                        return;
                    }

                    Log.d("Firebase", "User data fetched successfully!");
                    fragment.addUserData(documentSnapshot.toObject(User.class), holder);
                });
    }

    /**
     * Get user object given userId.
     * This method is async and sends a callback with the holder to be updated.
     *
     * @param userId
     * @param holder this parameter will be updated with the user data.
     * @param fragment the callback
     */
    public void getUser(String userId, ViewHolder holder, ServiceableUserFragment fragment) {
        db.collection(collection).document(userId)
                .get().addOnCompleteListener(task -> {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (!documentSnapshot.exists()) {
                        Log.e("Firebase", "This user is deleted!");
                        // TODO what happens to the posts of a user account is removed
                        return;
                    }

                    Log.d("Firebase", "User data fetched successfully!");
                    fragment.addUserData(documentSnapshot.toObject(User.class), holder);
                });
    }

    //get the user id from the database with a username.
    //This method is outdated!
    public String getUserId(String username) {
        Map<String, Object> data =  null;
        Log.e("Local", "This method is outdated");
        Task<QuerySnapshot> task = db.collection("users")
            .whereEqualTo("username", username)
            .get();
        //wait for the query to finish, since its async.
        while (!task.isComplete()) {}

        for (QueryDocumentSnapshot document : task.getResult()) {
            data = document.getData();
        }

        if(data != null && data.get("userId") != null)
            return data.get("userId").toString();
        else
            return "Unknown user";
    }

    //get the posts created by the current user from the database.
    public List<String> getMyPosts() {
        return currentUser.getMyPosts();
    }

    //get the saved posts of the current user from the database.
    public List<String> getSavedPosts() {
        return currentUser.getSavedPosts();
    }

    /**
     * Checks whether username exists in the database
     *
     * @param username
     * @return true if username does not exists in database. False otherwise
     */
    public boolean usernameCheck(String username) {
        //query that looks through the users collection and checks the username field
        Task<QuerySnapshot> task = db.collection(collection)
                .whereEqualTo("username", username)
                .get();

        //wait for the query to finish, since its async.
        while (!task.isComplete()) {}

        return task.getResult().isEmpty();
    }

    //updating the posts the user creates
    public void updateMyPosts(String postId) {
        List<String> myPosts = getMyPosts();
        DocumentReference postsRef = db.collection(collection).document(currentUser.getUserId());
        if (myPosts.contains(postId)) {
            myPosts.remove(postId);
        } else if (myPosts.size() != 0) { // add(index, element) throws an exception if index out of array bound
                myPosts.add(0, postId); // add the new post to the to the beginning.
        } else {
            myPosts.add(postId);
        }

        postsRef.update("myPosts", myPosts)
                .addOnSuccessListener(this)
                .addOnFailureListener(this);
    }

    private void updateSavedPosts(List<String> savedPosts)
    {
        DocumentReference postsRef = db.collection(collection).document(currentUser.getUserId());
        postsRef.update("savedPosts", savedPosts)
                .addOnSuccessListener(this)
                .addOnFailureListener(this);
    }

    /**
     * Update saved posts given postId.
     *
     * @param postId
     * @return action message (saved / unsaved).
     */
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
     *
     * @param postId the post id to check
     * @return true if the user has saved the post, false otherwise
     */
    public boolean userSavedPost(String postId) {
        List<String> savedPosts = getSavedPosts();
        return savedPosts.contains(postId);
    }

    //save a new user to the database
    public void saveNewUserToDb(String username, String userId) {
        User user = new User(username, userId);
        
        db.collection(collection)
                .document(userId).set(user)
                .addOnSuccessListener(this)
                .addOnFailureListener(this);
    }

    public void deleteUser(String userId) {
        DocumentReference docRef = db.collection(collection).document(userId);

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
            Log.e("", "Error: username length is empty or longer than 16 characters");
            return;
        }

        DocumentReference docRef = db.collection(collection).document(userId);

        //Override old username
        Map<String,Object> updates = new HashMap<>();
        updates.put("username", newUsername);

        docRef.update(updates);
    }

    @Override
    public void onSuccess(Object o) {
        System.out.println("UserService operation successful");
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        System.out.println("UsedService operation failed " + e);
    }
}

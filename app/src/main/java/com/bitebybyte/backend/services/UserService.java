package com.bitebybyte.backend.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bitebybyte.ServiceableUserFragment;
import com.bitebybyte.backend.models.User;
import com.bitebybyte.holders.CommentsViewHolder;
import com.bitebybyte.holders.HomeFeedViewHolder;
import com.bitebybyte.holders.SavedViewHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
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

    /**
     * Sets the currentUser instance to the currently logged-in user.
     * This method retrieves the user data from Firestore and sets it to the currentUser instance.
     * The method uses a listener to execute code when the Firestore task is completed.
     * <p>
     * Should only be called when the user is logged in.
     */
    private void setCurrentUserInstance() {
        FirebaseUser currentAuthenticatedUser = auth.getCurrentUser();

        if (currentAuthenticatedUser == null) {
            Log.v("Firebase", "No user is currently logged in");
            return;
        }

        if (UserService.currentUser == null || !currentAuthenticatedUser.getUid().equals(UserService.currentUser.getUserId())) {
            String currentUserId = auth.getCurrentUser().getUid();
            DocumentReference reference = db.collection(collection).document(currentUserId);

            Task<DocumentSnapshot> task = reference.get();

            // This while loop is used to wait for the task to complete.
            // Normally we would use a listener, but we want this code to block as it's needed for the app to work.
            while (!task.isComplete()) {
                // wait for task to complete
            }

            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                UserService.currentUser = documentSnapshot.toObject(User.class);
                Log.v("Firebase", "Current user fetched successfully");
            } else {
                Log.v("Firebase", "Failed to fetch current user");
            }
        }
    }

    /**
     * Get the user-id of the currently logged in user.
     */
    public String getCurrentUserId() {
        return currentUser.getUserId();
    }


    /**
     * Get the username of the currently logged in user.
     */
    public String getCurrentUsername() {
        return currentUser.getUsername();
    }

    /**
     * Get user object given userId.
     * This method is async and sends a callback.
     *
     * @param userId   the user id of the user to be fetched
     * @param fragment the callback
     */
    public void getUser(String userId, ServiceableUserFragment fragment) {

        db.collection(collection).document(userId)
                .get().addOnCompleteListener(task -> {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (!documentSnapshot.exists()) {
                        Log.e("Firebase", "This user is deleted!");
                        // TODO: Q: what happens to the posts of a user account is removed
                        //TODO: A: They also get deleted - Tristan
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
     * @param userId   the user id of the user to be fetched
     * @param holder   this parameter will be updated with the user data.
     * @param fragment the callback
     */
    public void getUser(String userId, HomeFeedViewHolder holder,
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
     * @param userId   the user id of the user to be fetched
     * @param holder   this parameter will be updated with the user data.
     * @param fragment the callback
     */
    public void getUser(String userId, CommentsViewHolder holder, ServiceableUserFragment fragment) {
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
     * @param userId   the user id of the user to be fetched
     * @param holder   this parameter will be updated with the user data.
     * @param fragment the callback
     */
    public void getUser(String userId, SavedViewHolder holder, ServiceableUserFragment fragment) {
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
     * Get the posts the current user has created.
     *
     * @return a list of post ids
     */
    public List<String> getMyPosts() {
        return currentUser.getMyPosts();
    }

    /**
     * Get the posts the current user has saved.
     *
     * @return a list of post ids
     */
    public List<String> getSavedPosts() {
        return currentUser.getSavedPosts();
    }

    /**
     * Checks whether username exists in the database
     *
     * @param username the username to be checked
     * @return true if username does not exists in database. False otherwise
     */
    public boolean checkIfUsernameInUse(String username) {
        //Query that looks through the users collection and checks the username field
        Task<QuerySnapshot> task = db.collection(collection)
                .whereEqualTo("username", username)
                .get();

        //We have to wait for the task to complete
        while (!task.isComplete()) {
            // wait for task to complete
        }

        return task.getResult().isEmpty();
    }

    /**
     * Updates the list of posts created by the current user.
     * If the post already exists in the list, it is removed.
     * If the list is not empty, the new post is added to the beginning of the list.
     * If the list is empty, the new post is added to the list.
     *
     * @param postId the ID of the post to be added or removed from the list of the user's posts
     */
    public void updateMyPosts(String postId) {
        // retrieve the list of posts that the user has created
        List<String> myPosts = getMyPosts();

        // get a reference to the user's document in the Firestore database
        DocumentReference postsRef = db.collection(collection).document(currentUser.getUserId());

        if (myPosts.contains(postId)) {
            // if the post already exists in the list, remove it
            myPosts.remove(postId);
        } else if (myPosts.size() != 0) {
            // if the list is not empty, add the new post to the beginning of the list
            myPosts.add(0, postId);
        } else {
            // if the list is empty, add the new post to the list
            myPosts.add(postId);
        }

        // update the "myPosts" field of the user's document in the Firestore database with the new list of posts
        postsRef.update("myPosts", myPosts)
                .addOnSuccessListener(this)
                .addOnFailureListener(this);
    }

    /**
     * Update the saved posts list of the current user.
     *
     * @param savedPosts the new list of saved posts
     */
    private void updateSavedPosts(List<String> savedPosts) {
        DocumentReference postsRef = db.collection(collection).document(currentUser.getUserId());
        postsRef.update("savedPosts", savedPosts)
                .addOnSuccessListener(this)
                .addOnFailureListener(this);
    }

    /**
     * Update saved posts given postId.
     *
     * @param postId the post id to be added or removed from the list of saved posts
     * @return action message (saved / unsaved).
     */
    public boolean updateSavedPosts(String postId) {
        boolean saved = false;

        List<String> savedPosts = getSavedPosts();

        //The post is already saved, so we remove it from the list
        if (savedPosts.contains(postId)) {
            savedPosts.remove(postId);
            //The post is not saved, so we add it to the list
        } else {
            savedPosts.add(postId);
            saved = true;
        }

        //Update the saved posts list
        updateSavedPosts(savedPosts);

        return saved;
    }

    /**
     * Delete SavedPost on given index.
     *
     * @param index the index of the post to be deleted
     */
    public void removeSavedPost(int index) {
        List<String> savedPosts = getSavedPosts();
        savedPosts.remove(index);

        //Update the saved posts list
        updateSavedPosts(savedPosts);
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

    /**
     * Save new user to database.
     *
     * @param username the username of the new user
     * @param userId   the user id of the new user
     */
    public void saveNewUserToDb(String username, String userId) {
        User user = new User(username, userId);

        db.collection(collection)
                .document(userId).set(user)
                .addOnSuccessListener(this)
                .addOnFailureListener(this);
    }

    /**
     * Delete user from database.
     *
     * @param userId the user id of the user to be deleted
     */
    public void deleteUser(String userId) {
        DocumentReference docRef = db.collection(collection).document(userId);

        //Delete all fields first
        Map<String, Object> updates = new HashMap<>();
        updates.put("userId", FieldValue.delete());
        updates.put("username", FieldValue.delete());
        updates.put("myPosts", FieldValue.delete());
        updates.put("savedPosts", FieldValue.delete());

        docRef.update(updates).addOnCompleteListener(completedTask -> {
            db.collection("users").document(userId).delete();
        }).addOnFailureListener(this);
    }

    /**
     * Change the username of the user.
     * @pre Username is not already in use
     * @pre Username length is between 1 and 16 characters
     * @post Username is changed
     * @param userId the user id of the user
     * @param newUsername the new username
     */
    public void changeUsername(String userId, String newUsername) throws IllegalArgumentException {

        if(!checkIfUsernameInUse(newUsername)) {
            throw new IllegalArgumentException("Username is already in use");
        }

        if(newUsername.length() < 1 || newUsername.length() > 16) {
            throw new IllegalArgumentException("Username must be between 1 and 16 characters");
        }

        DocumentReference docRef = db.collection(collection).document(userId);

        //Override old username
        Map<String, Object> updates = new HashMap<>();
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

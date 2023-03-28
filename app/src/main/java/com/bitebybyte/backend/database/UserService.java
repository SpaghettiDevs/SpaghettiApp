package com.bitebybyte.backend.database;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import com.bitebybyte.backend.local.User;
import com.google.firebase.firestore.QuerySnapshot;

public class UserService implements OnSuccessListener, OnFailureListener {
    FirebaseFirestore db;
    public UserService() {
        db = FirebaseFirestore.getInstance();
    }

    //creates a new User object
    //and forwards new User to be saved on the database
    public void createUser(String username, String userId) {
        saveUserToDb(new User(userId, username));
    }

    //check if an existant user already has the same username.
    public boolean usernameCheck(String username) {
        //query that looks through the users collection and checks the username field
        Task<QuerySnapshot> task = db.collection("users")
                .whereEqualTo("username", username)
                .get();

        //wait for the query to finish, since its async.
        while (task.isComplete() == false) {}

        //if the result is empty return true that there is no the same username.
        //if not empty there is someone with the same username.
        if (task.getResult().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    //save a user to the database
    private void saveUserToDb(User user) {
        db.collection("users")
                .document(user.getUserId()).set(user)
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

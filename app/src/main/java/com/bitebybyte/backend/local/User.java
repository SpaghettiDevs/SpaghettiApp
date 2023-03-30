package com.bitebybyte.backend.local;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String userId;
    private String username;
    private List<String> myPosts;
    private List<String> savedPosts;
    private static User instance;

    //setting the user with the values
    //called at login
    public void setUser(String userId, String username) {
        this.userId = userId;
        this.username = username;
        this.myPosts = new ArrayList<>();
        this.savedPosts = new ArrayList<>();
    }

    //singleton pattern
    //to ensure every where the same User instance is used
    //called at login
    public static User getUserInstance() {
        if (instance == null) {
            instance = new User();
        }

        return instance;
    }

    public String getUserId() { return this.userId; }
    public String getUsername() { return this.username; }
    public List<String> getMyPosts() { return this.myPosts; }
    public List<String> getSavedPosts() { return this.savedPosts; }
}

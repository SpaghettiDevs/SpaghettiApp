package com.bitebybyte.backend.local;

import java.util.ArrayList;
import java.util.List;

public class User {
    private List<String> myPosts;
    private List<String> savedPosts;
    private String userId;
    private String username;

    public User() {
        myPosts = new ArrayList<>();
        savedPosts = new ArrayList<>();
    }

    public User(String username, String userId) {
        this();
        this.userId = userId;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getSavedPosts() {
        return savedPosts;
    }

    public List<String> getMyPosts() {
        return myPosts;
    }
}

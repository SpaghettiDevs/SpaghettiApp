package com.bitebybyte.backend.local;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String UserId;
    private String username;
    private List<String> myPosts;
    private List<String> savedPosts;

    public User(String UserId, String username) {
        this.UserId = UserId;
        this.username = username;
        this.myPosts = new ArrayList<>();
        this.savedPosts = new ArrayList<>();
    }

    public String getUserId() { return this.UserId; }
    public String getUsername() { return this.username; }
    public List<String> getMyPosts() { return this.myPosts; }
    public List<String> getSavedPosts() { return this.savedPosts; }
}

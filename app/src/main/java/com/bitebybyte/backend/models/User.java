package com.bitebybyte.backend.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the application.
 */
public class User {
    /**
     * The list of IDs of posts created by the user.
     */
    private final List<String> myPosts;

    /**
     * The list of IDs of posts saved by the user.
     */
    private final List<String> savedPosts;

    /**
     * The unique ID of the user.
     */
    private String userId;

    /**
     * The username of the user.
     */
    private String username;

    /**
     * Creates a new user with empty lists of posts.
     */
    public User() {
        myPosts = new ArrayList<>();
        savedPosts = new ArrayList<>();
    }

    /**
     * Creates a new user with the specified username and user ID.
     *
     * @param username the username of the user
     * @param userId the unique ID of the user
     */
    public User(String username, String userId) {
        this();
        this.userId = userId;
        this.username = username;
    }

    /**
     * Returns the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the unique ID of the user.
     *
     * @return the unique ID of the user
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Returns the list of IDs of posts saved by the user.
     *
     * @return the list of IDs of posts saved by the user
     */
    public List<String> getSavedPosts() {
        return savedPosts;
    }

    /**
     * Returns the list of IDs of posts created by the user.
     *
     * @return the list of IDs of posts created by the user
     */
    public List<String> getMyPosts() {
        return myPosts;
    }
}

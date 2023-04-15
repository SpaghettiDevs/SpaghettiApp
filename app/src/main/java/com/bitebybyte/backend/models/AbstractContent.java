package com.bitebybyte.backend.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * An abstract class representing content, which is the base class for both comments and feed posts.
 * Provides basic properties and methods that are common to all types of content.
 */
public abstract class AbstractContent {
    protected Map<String, Boolean> likes;
    protected Date date;

    protected String content;
    protected String idOwner; // This is currently the username of the owner. Refactor later! //TODO: Is this still true?
    protected String postId;

    /**
     * Constructs a new AbstractContent object with default values for all properties.
     * Required for Firebase.
     */
    public AbstractContent() {
    }

    /**
     * Constructs a new AbstractContent object with the specified values for all properties.
     *
     * @param idOwner the ID of the user who created the content
     * @param content the content of the post
     */
    public AbstractContent(String idOwner, String content) {
        this.likes = new HashMap<>();
        this.date = new Date();
        this.content = content;
        this.idOwner = idOwner;
        this.postId = String.valueOf(UUID.randomUUID());
    }

    /**
     * Returns the map of likes for this content.
     *
     * @return the map of likes for this content
     */
    public Map<String, Boolean> getLikes() {
        return likes;
    }

    /**
     * Returns the date on which this content was posted.
     *
     * @return the date on which this content was posted
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the content of this post.
     *
     * @return the content of this post
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns the ID of the user who created this content.
     *
     * @return the ID of the user who created this content
     */
    public String getIdOwner() {
        return idOwner;
    }

    /**
     * Returns the ID of this post.
     *
     * @return the ID of this post
     */
    public String getPostId() {
        return postId;
    }
}

package com.bitebybyte.backend.local;

import java.util.Date;
import java.util.UUID;

public abstract class AbstractContent {
    protected int likes;
    protected Date date;
    protected String content;
    protected String idOwner;
    protected String postId;

    public  AbstractContent() {

    }

    public AbstractContent(String idOwner, String content) {
        this.likes = 0;
        this.date = new Date();
        this.content = content;
        this.idOwner = idOwner;
        this.postId = String.valueOf(UUID.randomUUID());
    }

    public int getLikes() {
        return likes;
    }
    public void incrementLikes() {likes++;}

    public Date getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getIdOwner() {
        return idOwner;
    }

    public String getPostId() {
        return postId;
    }

}

package com.bitebybyte.backend.local;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractContent {
    protected Map<String, Boolean> likes;
    protected Date date;

    protected String content;
    protected String idOwner;
    protected String postId;

    public  AbstractContent() {

    }

    public AbstractContent(String idOwner, String content) {
        this.likes = new HashMap<>();
        this.date = new Date();
        this.content = content;
        this.idOwner = idOwner;
        this.postId = String.valueOf(UUID.randomUUID());
    }

    public Map<String, Boolean> getLikes() {
        return likes;
    }

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

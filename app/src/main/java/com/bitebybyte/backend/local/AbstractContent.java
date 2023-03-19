package com.bitebybyte.backend.local;

import java.util.Date;
import java.util.UUID;

public abstract class AbstractContent {
    protected int likesCount;
    protected Date datePublished;
    protected String content;
    protected String idOwner;
    protected String id;

    public AbstractContent(String idOwner, String content) {
        this.likesCount = 0;
        this.datePublished = new Date();
        this.content = content;
        this.idOwner = idOwner;
        this.id = String.valueOf(UUID.randomUUID());
    }

    public int getLikes() {
        return likesCount;
    }

    public Date getDate() {
        return datePublished;
    }

    public String getContent() {
        return content;
    }

    public String getIdOwner() {
        return idOwner;
    }

}

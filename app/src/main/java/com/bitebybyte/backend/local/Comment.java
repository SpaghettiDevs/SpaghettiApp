package com.bitebybyte.backend.local;

import java.util.ArrayList;
import java.util.List;

public class Comment extends AbstractContent{

    List<Comment> comments;

    public Comment() {
        comments = new ArrayList<>();
    }
    public Comment(String idOwner, String content) {
        super(idOwner, content);
        comments = new ArrayList<>();
    }
}

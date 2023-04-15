package com.bitebybyte.backend.models;

import java.util.ArrayList;
import java.util.List;

/**

 A class representing a comment, which is attached to a feed post.

 Extends the AbstractContent class, which provides basic properties and methods for content.
 */
public class Comment extends AbstractContent{

    List<Comment> comments;

    /**

     Constructs a new Comment object with the specified values for all properties.
     @param idOwner the ID of the user who created the comment
     @param content the content of the comment
     */
    public Comment(String idOwner, String content) {
        super(idOwner, content);
        comments = new ArrayList<>();
    }
}

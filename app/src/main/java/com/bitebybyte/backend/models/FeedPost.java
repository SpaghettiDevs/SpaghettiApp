package com.bitebybyte.backend.models;

import java.util.ArrayList;
import java.util.List;

/**

 A class representing a feed post, including its content, title, image, labels, and recipe.
 Extends the AbstractContent class, which provides basic properties and methods for content.
 */
public class FeedPost extends AbstractContent{
    private final String images; //Currently only one image
    private final List<String> labels;
    private String title;
    private Recipe recipe;
    private List<Comment> comments;


    /**
     * No-arg constructor for Firebase
     */
    public FeedPost() {
        super();
        labels = new ArrayList<>();
        images = this.postId; /// Design decision. Post and image associated with post are strongly coupled!!
        comments = new ArrayList<>();
    }

    /**

     Constructs a new FeedPost object with the specified values for all properties.

     @param idOwner the ID of the user who created the post
     @param content the content of the post
     @param title the title of the post
     @param images the image associated with the post
     @param labels the labels associated with the post
     @param recipe the recipe associated with the post
     */
    public FeedPost(String idOwner, String content, String title,
                    String images, List<String> labels, Recipe recipe) {
        super(idOwner, content);
        this.title = title;
        this.images = images;
        this.labels = labels != null ? labels : new ArrayList<>();
        this.recipe = recipe != null ? recipe : new Recipe();
        this.comments = comments != null ? comments : new ArrayList<>();
    }
    /**

     Gets the image associated with this post.
     @return the image associated with this post
     */
    public String getImages() {
        return images;
    }
    /**

     Gets the labels associated with this post.
     @return the labels associated with this post
     */
    public List<String> getLabels() {
        return labels;
    }
    /**

     Gets the title of this post.
     @return the title of this post
     */
    public String getTitle() {
        return title;
    }
    /**

     Gets the recipe associated with this post.
     @return the recipe associated with this post
     */
    public Recipe getRecipe() {
        return recipe;
    }
    /**

     Gets the comments on this post.
     @return the comments on this post
     */
    public List<Comment> getComments() {
        return comments;
    }
}
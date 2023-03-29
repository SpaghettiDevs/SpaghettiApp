package com.bitebybyte.backend.local;

import java.util.ArrayList;
import java.util.List;

public class FeedPost extends AbstractContent{
    private String images; //Currently only one image
    private List<String> labels;
    private String title;
    private Recipe recipe;
    private List<Comment> comments;

    public FeedPost() {
        super();
        labels = new ArrayList<>();
        images = this.postId; /// Design decision. Post and image associated with post are strongly coupled!!
        comments = new ArrayList<>();
    }

    public FeedPost(String idOwner, String content, String title,
                    String images, List<String> labels, Recipe recipe) {
        super(idOwner, content);
        this.title = title;
        this.images = images;
        this.labels = labels != null ? labels : new ArrayList<>();
        this.recipe = recipe != null ? recipe : new Recipe();
    }

    public String getImages() {
        return images;
    }

    public List<String> getLabels() {
        return labels;
    }

    public String getTitle() {
        return title;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public List<Comment> getComments() {
        return comments;
    }
}

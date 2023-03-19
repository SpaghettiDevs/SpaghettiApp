package com.bitebybyte.backend.local;

import android.media.Image;

import java.util.ArrayList;
import java.util.List;

public class FeedPost extends AbstractContent{

    private List<Image> images;
    private List<String> labels;
    private String title;
    private Recipe recipe;

    public FeedPost(String idOwner, String content, String title,
                    List<Image> images, List<String> labels, Recipe recipe) {
        super(idOwner, content);
        this.title = title;
        this.images = images != null ? images : new ArrayList<>();
        this.labels = labels != null ? labels : new ArrayList<>();
        this.recipe = recipe != null ? recipe : new Recipe();
    }

    public List<Image> getImages() {
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
}

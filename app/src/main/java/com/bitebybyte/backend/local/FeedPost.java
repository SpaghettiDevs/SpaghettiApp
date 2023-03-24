package com.bitebybyte.backend.local;

import android.media.Image;
import androidx.camera.core.ImageCapture;

import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class FeedPost extends AbstractContent{
    private String images;
    private List<String> labels;
    private String title;
    private Recipe recipe;

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
}

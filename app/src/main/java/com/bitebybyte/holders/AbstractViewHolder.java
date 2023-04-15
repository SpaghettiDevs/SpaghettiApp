package com.bitebybyte.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class AbstractViewHolder extends RecyclerView.ViewHolder{

    protected TextView postTitle;
    protected TextView postAuthor;
    protected TextView postCookingTime;
    protected ImageView postImage;
    protected ImageView postAuthorImage;
    protected ImageView deletePostButton;

    public AbstractViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public TextView getPostTitle() {
        return postTitle;
    }

    public TextView getPostAuthor() {
        return postAuthor;
    }

    public TextView getPostCookingTime() {
        return postCookingTime;
    }

    public ImageView getPostImage() {
        return postImage;
    }

    public ImageView getPostAuthorImage() {
        return postAuthorImage;
    }

    public ImageView getDeletePostButton() {
        return deletePostButton;
    }
}

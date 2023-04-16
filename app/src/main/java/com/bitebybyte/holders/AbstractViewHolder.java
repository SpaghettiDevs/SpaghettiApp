package com.bitebybyte.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * An abstract representing view holders.
 * ViewHolders are used to help connect the data from the backend / firebase to the UI
 * when there are multiple views present.
 */
public abstract class AbstractViewHolder extends RecyclerView.ViewHolder {

    protected TextView postTitle;
    protected TextView postAuthor;
    protected TextView postCookingTime;
    protected ImageView postImage;
    protected ImageView postAuthorImage;
    protected ImageView deletePostButton;

    /**
     * Constructor to connect the view item with the RecyclerView.
     *
     * @param itemView single view item.
     */
    public AbstractViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    /**
     * Getter method.
     *
     * @return the title of the post
     */
    public TextView getPostTitle() {
        return postTitle;
    }

    /**
     * Getter method.
     *
     * @return the author of the post
     */
    public TextView getPostAuthor() {
        return postAuthor;
    }

    /**
     * Getter method.
     *
     * @return the time it takes to cook the meal.
     */
    public TextView getPostCookingTime() {
        return postCookingTime;
    }

    /**
     * Getter method.
     *
     * @return the image of the post
     */
    public ImageView getPostImage() {
        return postImage;
    }

    /**
     * Getter method.
     *
     * @return the profile picture of the author
     */
    public ImageView getPostAuthorImage() {
        return postAuthorImage;
    }

    /**
     * Getter method.
     *
     * @return the delete button
     */
    public ImageView getDeletePostButton() {
        return deletePostButton;
    }
}

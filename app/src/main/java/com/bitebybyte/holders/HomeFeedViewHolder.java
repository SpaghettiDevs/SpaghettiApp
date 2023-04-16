package com.bitebybyte.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitebybyte.R;

/**
 * A class representing a view holder for home feed posts.
 * Extends the AbstractViewHolder class, which provides basic properties and methods for the holders.
 */
public class HomeFeedViewHolder extends AbstractViewHolder {
    private final TextView postTimeStamp;
    private final TextView  postLikesAmount;
    private final TextView  postCommentsAmount;
    private final ImageView postLikeButton;

    /**
     * Connects the view with the home feed post UI.
     *
     * @param itemView view to connect with recycler view
     */
    public HomeFeedViewHolder(@NonNull View itemView) {
        super(itemView);
        postTitle          = itemView.findViewById(R.id.postTitleTextView);
        postTimeStamp      = itemView.findViewById(R.id.postTimeStampTextView);
        postCookingTime    = itemView.findViewById(R.id.postCookingTimeTextView);
        postAuthor         = itemView.findViewById(R.id.postAuthor);
        postLikesAmount    = itemView.findViewById(R.id.postLikesAmountTextView);
        postLikeButton     = itemView.findViewById(R.id.postLikesIcon);
        postCommentsAmount = itemView.findViewById(R.id.postCommentsAmountTextView);

        postAuthorImage = itemView.findViewById(R.id.postAuthorProfilePicture);
        postImage                = itemView.findViewById(R.id.postImageView);

    }

    /**
     *  The methods below are getter methods.
     */

    public TextView getPostTimeStamp() {
        return postTimeStamp;
    }

    public TextView getPostLikesAmount() {
        return postLikesAmount;
    }

    public TextView getPostCommentsAmount() {
        return postCommentsAmount;
    }

    public ImageView getPostAuthorProfilePicture() {
        return postAuthorImage;
    }

    public ImageView getPostLikeButton() {
        return postLikeButton;
    }
}

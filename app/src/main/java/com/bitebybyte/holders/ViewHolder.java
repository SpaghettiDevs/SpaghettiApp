package com.bitebybyte.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView postTitle;
    private final TextView postAuthor;
    private final TextView postCookingTime;
    private final ImageView postImage;
    private final ImageView postAuthorImage;
    private final ImageView deletePostButton;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        postTitle = itemView.findViewById(R.id.savedPostTitleTextView);
        postAuthor = itemView.findViewById(R.id.savedPostAuthor);
        postCookingTime = itemView.findViewById(R.id.savedPostCookingTimeTextView);
        postImage = itemView.findViewById(R.id.savedPostImageView);
        postAuthorImage = itemView.findViewById(R.id.savedPostAuthorProfilePicture);
        deletePostButton = itemView.findViewById(R.id.savedPostDeleteIcon);
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
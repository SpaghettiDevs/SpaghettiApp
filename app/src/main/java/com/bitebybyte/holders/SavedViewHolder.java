package com.bitebybyte.holders;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bitebybyte.R;

public class SavedViewHolder extends AbstractViewHolder {
    protected final ImageView deletePostButton;

    public SavedViewHolder(@NonNull View itemView) {
        super(itemView);
        postTitle = itemView.findViewById(R.id.savedPostTitleTextView);
        postAuthor = itemView.findViewById(R.id.savedPostAuthor);
        postCookingTime = itemView.findViewById(R.id.savedPostCookingTimeTextView);
        postImage = itemView.findViewById(R.id.savedPostImageView);
        postAuthorImage = itemView.findViewById(R.id.savedPostAuthorProfilePicture);
        deletePostButton = itemView.findViewById(R.id.savedPostDeleteIcon);
    }

    public ImageView getDeletePostButton() {
        return deletePostButton;
    }
}
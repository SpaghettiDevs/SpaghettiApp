package com.bitebybyte.holders;

import android.view.View;

import androidx.annotation.NonNull;

import com.bitebybyte.R;

/**
 * A class representing a view holder for saved and owned posts.
 * Extends the AbstractViewHolder class, which provides basic properties and methods for the holders.
 */
public class SavedViewHolder extends AbstractViewHolder {

    /**
     * Connects the view with the saved and owned posts UI.
     *
     * @param itemView view to connect with recycler view
     */
    public SavedViewHolder(@NonNull View itemView) {
        super(itemView);
        postTitle = itemView.findViewById(R.id.savedPostTitleTextView);
        postAuthor = itemView.findViewById(R.id.savedPostAuthor);
        postCookingTime = itemView.findViewById(R.id.savedPostCookingTimeTextView);
        postImage = itemView.findViewById(R.id.savedPostImageView);
        postAuthorImage = itemView.findViewById(R.id.savedPostAuthorProfilePicture);
        deletePostButton = itemView.findViewById(R.id.savedPostDeleteIcon);
    }
}
package com.bitebybyte.ui.saved.recipes;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;

import java.util.function.BiFunction;

public class SavedRecipesAdapter extends RecyclerView.Adapter<SavedRecipesAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // specify which xml layout to use for the recycler view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_saved, parent, false);

        return new ViewHolder(view);
    }

    //TODO: Connect the view with firebase, load data here!
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getPostTitle().setText("Saved Recipe Title");
        holder.getPostAuthor().setText("Saved Recipe Author");
        holder.getPostCookingTime().setText(String.format("%d days", 2));

        //TODO: Load image from firebase
        //TODO: Load user profile image from firebase if it is set

        //Add delete button listener
        holder.getDeletePostButton().setOnClickListener(v -> {
            System.out.println("Delete button clicked");
        });

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
}



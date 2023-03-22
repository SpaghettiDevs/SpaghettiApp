package com.bitebybyte.ui.saved.my;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;

import java.util.function.BiFunction;

public class MyRecipesAdapter extends RecyclerView.Adapter<MyRecipesAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // specify which xml layout to use for the recycler view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_saved, parent, false);

        return new ViewHolder(view);
    }

    //TODO: Connect the view with firebase, load data here!
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.getPostTitle().setText("Test My recipe Title");
        holder.getPostAuthor().setText("Test My recipe Author");
        holder.getPostTimeStamp().setText(String.format("%dd ago", 1));
        holder.getPostCookingTime().setText(String.format("%d min", 1));
        holder.getPostCommentsAmount().setText(String.format("%d", 1));
        holder.getPostLikesAmount().setText(String.format("%d", 1));
    }

    @Override
    public int getItemCount()
    {
        return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView postTitle;
        private final TextView postAuthor;
        private final TextView postTimeStamp;
        private final TextView postCookingTime;
        private final TextView postCommentsAmount;
        private final TextView postLikesAmount;
        private final ImageView postImage;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            postTitle = itemView.findViewById(R.id.savedPostTitleTextView);
            postAuthor = itemView.findViewById(R.id.savedPostAuthor);
            postTimeStamp = itemView.findViewById(R.id.savedPostTimeStampTextView);
            postCookingTime = itemView.findViewById(R.id.savedPostCookingTimeTextView);
            postCommentsAmount = itemView.findViewById(R.id.savedPostCommentsAmountTextView);
            postLikesAmount = itemView.findViewById(R.id.savedPostLikesAmountTextView);
            postImage = itemView.findViewById(R.id.savedPostImageView);
        }

        public TextView getPostTitle()
        {
            return postTitle;
        }

        public TextView getPostAuthor()
        {
            return postAuthor;
        }

        public TextView getPostTimeStamp()
        {
            return postTimeStamp;
        }

        public TextView getPostCookingTime()
        {
            return postCookingTime;
        }

        public TextView getPostCommentsAmount()
        {
            return postCommentsAmount;
        }

        public TextView getPostLikesAmount()
        {
            return postLikesAmount;
        }

        public ImageView getPostImage()
        {
            return postImage;
        }
    }
}



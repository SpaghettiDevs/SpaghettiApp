package com.bitebybyte.ui.post;

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

public class PostCommentsAdapter extends RecyclerView.Adapter<PostCommentsAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // specify which xml layout to use for the recycler view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_post, parent, false);

        return new ViewHolder(view);
    }

    //TODO: Connect the view with firebase, load data here!
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //TODO: Load text, author and likes from firebase
        //TODO: Load user profile image from firebase if it is set

        //Add delete button listener
        holder.getLikeButton().setOnClickListener(v -> {
            //Handle like button click
            System.out.println("Like button clicked");
        });

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView content;
        private final TextView authorName;
        private final ImageView authorImage;
        private final TextView postedDateTime;
        private final TextView likesCount;
        private final ImageView likeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.post_comment_text);
            authorName = itemView.findViewById(R.id.post_comment_author_name);
            authorImage = itemView.findViewById(R.id.post_comment_author_picture);
            postedDateTime = itemView.findViewById(R.id.post_comment_date_time);
            likesCount = itemView.findViewById(R.id.post_comment_likes_amount);
            likeButton = itemView.findViewById(R.id.post_comment_likes_icon);
        }

        public TextView getContent() {
            return content;
        }

        public TextView getAuthorName() {
            return authorName;
        }

        public ImageView getAuthorImage() {
            return authorImage;
        }

        public TextView getPostedDateTime() {
            return postedDateTime;
        }

        public TextView getLikesCount() {
            return likesCount;
        }

        public ImageView getLikeButton() {
            return likeButton;
        }
    }
}



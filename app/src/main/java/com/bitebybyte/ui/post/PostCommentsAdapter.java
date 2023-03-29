package com.bitebybyte.ui.post;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;
import com.bitebybyte.backend.database.PostService;
import com.bitebybyte.backend.local.Comment;
import com.bitebybyte.backend.local.FeedPost;

import java.util.List;

public class PostCommentsAdapter extends RecyclerView.Adapter<PostCommentsAdapter.ViewHolder> {

    private List<Comment> comments;
    private PostService postService;

    protected PostCommentsAdapter(FeedPost post) {
        comments = post.getComments();
        postService = new PostService();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // specify which xml layout to use for the recycler view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_post, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);

        holder.getAuthorName().setText(comment.getIdOwner());
        holder.getContent().setText(comment.getContent());

        String formattedDate = postService.dateFormat(comment.getDate());
        holder.postedDateTime.setText(formattedDate);
        holder.likesCount.setText(Integer.toString(comment.getLikes().size()));

        //Add delete button listener

        holder.getLikeButton().setOnClickListener(v -> {
            postService.updateLikes(comment);
            holder.getLikesCount().setText(Integer.toString(comment.getLikes().size()));
        });

    }

    @Override
    public int getItemCount() {
        return comments.size();
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



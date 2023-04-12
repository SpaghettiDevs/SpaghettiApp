package com.bitebybyte.ui.post;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;
import com.bitebybyte.ServiceableUserFragment;
import com.bitebybyte.backend.database.PostService;
import com.bitebybyte.backend.database.UserService;
import com.bitebybyte.backend.local.Comment;
import com.bitebybyte.backend.local.FeedPost;
import com.bitebybyte.backend.local.User;
import com.bitebybyte.ui.home.HomeFeedAdapter;

import java.util.List;

public class PostCommentsAdapter extends RecyclerView.Adapter<PostCommentsAdapter.ViewHolder>
    implements ServiceableUserFragment {

    private List<Comment> comments;
    private FeedPost post;
    private PostService postService;
    private final UserService userService;

    protected PostCommentsAdapter(FeedPost post) {
        comments = post.getComments();
        postService = new PostService();
        userService = new UserService();
        this.post = post;
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

        if (!comment.getIdOwner().equals(userService.getCurrentUserId())) {
            holder.getDeleteButton().setVisibility(View.GONE);
        }

        userService.getUser(comment.getIdOwner(), holder, this);
        holder.getContent().setText(comment.getContent());

        String formattedDate = postService.dateFormat(comment.getDate());
        holder.getPostedDateTime().setText(formattedDate);
        holder.getLikesCount().setText(Integer.toString(comment.getLikes().size()));

        //Add delete button listener
        holder.getDeleteButton().setOnClickListener(v -> {
            postService.deleteComment(comments, post.getPostId(), position);
            Toast.makeText(holder.content.getContext(), "Comment deleted", Toast.LENGTH_SHORT).show();
        });

        holder.getLikeButton().setOnClickListener(v -> {
            postService.updateLikes(comments, post.getPostId(), position);
            holder.getLikesCount().setText(Integer.toString(comment.getLikes().size()));
        });

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public void addUserData(User user) {

    }

    @Override
    public void addUserData(User user, HomeFeedAdapter.ViewHolder viewHolder) {

    }

    @Override
    public void addUserData(User user, ViewHolder viewHolder) {
        viewHolder.getAuthorName().setText(user.getUsername());
    }

    @Override
    public void addUserData(User user, com.bitebybyte.ui.saved.ViewHolder viewHolder) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView content;
        private final TextView authorName;
        private final ImageView authorImage;
        private final TextView postedDateTime;
        private final TextView likesCount;
        private final ImageView likeButton;
        private final ImageButton deleteButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.post_comment_text);
            authorName = itemView.findViewById(R.id.post_comment_author_name);
            authorImage = itemView.findViewById(R.id.post_comment_author_picture);
            postedDateTime = itemView.findViewById(R.id.post_comment_date_time);
            likesCount = itemView.findViewById(R.id.post_comment_likes_amount);
            likeButton = itemView.findViewById(R.id.post_comment_likes_icon);
            deleteButton = itemView.findViewById(R.id.post_comment_delete_button);
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

        public ImageButton getDeleteButton() {return deleteButton;}
    }
}



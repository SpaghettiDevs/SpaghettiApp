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

/**
 * Adapter for the RecyclerView in the PostCommentsFragment
 */
public class PostCommentsAdapter extends RecyclerView.Adapter<PostCommentsAdapter.ViewHolder>
    implements ServiceableUserFragment {

    private List<Comment> comments;
    private FeedPost post;
    private PostService postService;
    private final UserService userService;

    /**
     * Constructor for the PostCommentsAdapter
     * @param post The post to get the comments from
     */
    protected PostCommentsAdapter(FeedPost post) {
        comments = post.getComments();
        postService = new PostService();
        userService = new UserService();
        this.post = post;
    }

    /**
     * Create a new ViewHolder for the Comment layout
     * @param parent The parent ViewGroup
     * @param viewType The type of the view
     * @return The ViewHolder for the Comment layout
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // specify which xml layout to use for the recycler view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_post, parent, false);

        return new ViewHolder(view);
    }

    /**
     * Bind the data for each Comment to its ViewHolder
     * @param holder The ViewHolder to bind the data to
     * @param position The position of the Comment in the list
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);

        hideDeleteButtonIfNotOwner(holder, comment);
        loadUserDataForCommentOwner(holder, comment);
        setCommentContent(holder, comment);
        setCommentPostedDate(holder, comment);
        setCommentLikesCount(holder, comment);

        holder.getDeleteButton().setOnClickListener(v -> onDeleteButtonClicked(holder, position));
        holder.getLikeButton().setOnClickListener(v -> onLikeButtonClicked(holder, comment, position));
    }

    /**
     * Hides the delete button for a comment if the current user is not the owner of the comment.
     *
     * @param holder  the ViewHolder that contains the delete button
     * @param comment the Comment object to check the ownership of
     */
    private void hideDeleteButtonIfNotOwner(ViewHolder holder, Comment comment) {
        if (!comment.getIdOwner().equals(userService.getCurrentUserId())) {
            holder.getDeleteButton().setVisibility(View.GONE);
        }
    }

    /**
     * Loads the user data for the owner of a comment.
     *
     * @param holder  the ViewHolder that will contain the user data
     * @param comment the Comment object to get the owner's ID from
     */
    private void loadUserDataForCommentOwner(ViewHolder holder, Comment comment) {
        userService.getUser(comment.getIdOwner(), holder, this);
    }

    /**
     * Sets the content of a comment in a ViewHolder.
     *
     * @param holder  the ViewHolder that contains the comment
     * @param comment the Comment object to get the content from
     */
    private void setCommentContent(ViewHolder holder, Comment comment) {
        holder.getContent().setText(comment.getContent());
    }

    /**
     * Sets the posted date of a comment in a ViewHolder.
     *
     * @param holder  the ViewHolder that contains the comment
     * @param comment the Comment object to get the date from
     */
    private void setCommentPostedDate(ViewHolder holder, Comment comment) {
        String formattedDate = postService.dateFormat(comment.getDate());
        holder.getPostedDateTime().setText(formattedDate);
    }

    /**
     * Sets the number of likes for a comment in a ViewHolder.
     *
     * @param holder  the ViewHolder that contains the comment
     * @param comment the Comment object to get the likes from
     */
    private void setCommentLikesCount(ViewHolder holder, Comment comment) {
        int likesCount = comment.getLikes().size();
        holder.getLikesCount().setText(likesCount);
    }

    /**
     * Handles the onClick event for the delete button of a comment.
     * Displays a toast message to confirm the deletion.
     * @param holder   the ViewHolder that contains the delete button
     * @param position the position of the comment in the adapter's list
     */
    private void onDeleteButtonClicked(ViewHolder holder, int position) {
        postService.deleteComment(comments, post.getPostId(), position);
        Toast.makeText(holder.content.getContext(), "Comment deleted", Toast.LENGTH_SHORT).show();
    }

    /**
     * Handles the onClick event for the like button of a comment.
     * Updates the number of likes for the comment.
     * @param holder   the ViewHolder that contains the like button
     * @param comment  the Comment object to get the likes from
     * @param position the position of the comment in the adapter's list
     */
    private void onLikeButtonClicked(ViewHolder holder, Comment comment, int position) {
        postService.updateLikes(comments, post.getPostId(), position);
        int likesCount = comment.getLikes().size();
        holder.getLikesCount().setText(likesCount);
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



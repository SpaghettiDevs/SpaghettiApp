package com.bitebybyte.ui.post;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;
import com.bitebybyte.ServiceableUserFragment;
import com.bitebybyte.backend.models.Comment;
import com.bitebybyte.backend.models.FeedPost;
import com.bitebybyte.backend.models.User;
import com.bitebybyte.backend.services.PostService;
import com.bitebybyte.backend.services.UserService;
import com.bitebybyte.holders.AbstractViewHolder;
import com.bitebybyte.holders.CommentsViewHolder;

import java.util.List;

/**
 * Adapter for the RecyclerView in the PostCommentsFragment
 */
public class PostCommentsAdapter extends RecyclerView.Adapter<CommentsViewHolder>
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
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // specify which xml layout to use for the recycler view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_post, parent, false);

        return new CommentsViewHolder(view);
    }

    /**
     * Bind the data for each Comment to its ViewHolder
     * @param holder The ViewHolder to bind the data to
     * @param position The position of the Comment in the list
     */
    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        Comment comment = comments.get(position);

        // always show the delete button if the current user is a moderator
        if (!userService.isCurrentUserModerator()) {
            hideDeleteButtonIfNotOwner(holder, comment);
        }

        loadUserDataForCommentOwner(holder, comment);
        setCommentContent(holder, comment);
        setCommentPostedDate(holder, comment);
        setCommentLikesCount(holder, comment);

        holder.getDeleteButton().setOnClickListener(v -> onDeleteButtonClicked(holder, holder.getAdapterPosition()));
        holder.getLikeButton().setOnClickListener(v -> onLikeButtonClicked(holder, comment, holder.getAdapterPosition()));

        if (postService.hasLikedContent(comment))
            //Update the like icon to be solid if the user has liked the post
            holder.getLikeButton().setImageResource(R.drawable.baseline_favorite_24);
        else
            //Update the like icon to be outline if the user has liked the post
            holder.getLikeButton().setImageResource(R.drawable.round_favorite_border_24);
    }

    /**
     * Hides the delete button for a comment if the current user is not the owner of the comment.
     *
     * @param holder  the ViewHolder that contains the delete button
     * @param comment the Comment object to check the ownership of
     */
    private void hideDeleteButtonIfNotOwner(CommentsViewHolder holder, Comment comment) {
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
    private void loadUserDataForCommentOwner(CommentsViewHolder holder, Comment comment) {
        userService.getUser(comment.getIdOwner(), holder, this);
    }

    /**
     * Sets the content of a comment in a ViewHolder.
     *
     * @param holder  the ViewHolder that contains the comment
     * @param comment the Comment object to get the content from
     */
    private void setCommentContent(CommentsViewHolder holder, Comment comment) {
        holder.getContent().setText(comment.getContent());
    }

    /**
     * Sets the posted date of a comment in a ViewHolder.
     *
     * @param holder  the ViewHolder that contains the comment
     * @param comment the Comment object to get the date from
     */
    private void setCommentPostedDate(CommentsViewHolder holder, Comment comment) {
        String formattedDate = postService.dateFormat(comment.getDate());
        holder.getPostedDateTime().setText(formattedDate);
    }

    /**
     * Sets the number of likes for a comment in a ViewHolder.
     *
     * @param holder  the ViewHolder that contains the comment
     * @param comment the Comment object to get the likes from
     */
    private void setCommentLikesCount(CommentsViewHolder holder, Comment comment) {
        int likesCount = comment.getLikes().size();
        holder.getLikesCount().setText(String.valueOf(likesCount));
    }

    /**
     * Handles the onClick event for the delete button of a comment.
     * Displays a toast message to confirm the deletion.
     * @param holder   the ViewHolder that contains the delete button
     * @param position the position of the comment in the adapter's list
     */
    private void onDeleteButtonClicked(CommentsViewHolder holder, int position) {
        postService.deleteComment(comments, post.getPostId(), position, "posts");
        Toast.makeText(holder.getContent().getContext(), "Comment deleted", Toast.LENGTH_SHORT).show();

        //Refresh the comments list
        notifyItemRemoved(position);
    }

    /**
     * Handles the onClick event for the like button of a comment.
     * Updates the number of likes for the comment.
     * @param holder   the ViewHolder that contains the like button
     * @param comment  the Comment object to get the likes from
     * @param position the position of the comment in the adapter's list
     */
    private void onLikeButtonClicked(CommentsViewHolder holder, Comment comment, int position) {
        postService.updateLikes(comments, post.getPostId(), position, "posts");

        if (postService.hasLikedContent(comment))
            //Update the like icon to be solid if the user has liked the post
            holder.getLikeButton().setImageResource(R.drawable.baseline_favorite_24);
        else
            //Update the like icon to be outline if the user has liked the post
            holder.getLikeButton().setImageResource(R.drawable.round_favorite_border_24);

        setCommentLikesCount(holder, comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public void addUserData(User user) {

    }

    @Override
    public void addUserData(User user, AbstractViewHolder viewHolder) {
        viewHolder.getPostAuthor().setText(user.getUsername());
    }

}



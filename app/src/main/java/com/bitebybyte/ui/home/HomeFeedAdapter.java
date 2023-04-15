package com.bitebybyte.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;
import com.bitebybyte.ServiceableUserFragment;
import com.bitebybyte.backend.database.PostService;
import com.bitebybyte.backend.database.UserService;
import com.bitebybyte.backend.local.FeedPost;
import com.bitebybyte.backend.local.User;
import com.bitebybyte.ui.post.PostCommentsAdapter;

import java.util.List;

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.ViewHolder>
    implements ServiceableUserFragment
{
    private NavController navController;
    int itemCount;
    List<FeedPost> posts;
    PostService postService;
    UserService userService;

    public HomeFeedAdapter(List<FeedPost> posts) {
        this.itemCount = posts.size();
        this.posts = posts;
        postService = new PostService();
        userService = new UserService();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // specify which xml layout to use for the recycler view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_home, parent,
                                                                     false);
        navController = Navigation.findNavController(parent);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        FeedPost post = posts.get(position);

        // Set the post title
        holder.getPostTitle().setText(post.getTitle());

        // Get the username and user image
        loadUserData(holder, post);

        // Set the post timestamp and cooking time
        setPostDetails(holder, post);

        // Load the post image and set the number of comments
        setPostImageAndComments(holder, post);

        // Set the number of likes and the like button's appearance
        setLikeButtonAppearance(holder, post);

        // Update the likes when someone presses the like button
        holder.getPostLikeButton().setOnClickListener(event -> onLikeButtonClicked(holder, post));

        // Navigate to the post detail page when the user clicks on the post title or image
        holder.getPostTitle().setOnClickListener(event -> navigateToPostDetail(post.getPostId()));
        holder.getPostImage().setOnClickListener(event -> navigateToPostDetail(post.getPostId()));
    }


    /**
     * Navigate to the post detail page for the given post.
     * @param postId The ID of the post to navigate to
     */
    private void navigateToPostDetail(String postId) {
        NavDirections action = HomeFragmentDirections.actionNavigationHomeToPostDetail(postId);
        navController.navigate(action);
    }

    /**
     * Set the number of likes and the like button's appearance for the given post.
     * @param holder The ViewHolder to be updated
     * @param post The post to set the like button appearance for
     */
    private void onLikeButtonClicked(ViewHolder holder, FeedPost post) {
        int oldLikes = post.getLikes().size();
        postService.updateLikes(post);
        int newLikes = post.getLikes().size();

        holder.getPostLikesAmount().setText(newLikes);

        if (oldLikes < newLikes)
            //Update the like icon to be solid if the user has liked the post
            holder.getPostLikeButton().setImageResource(R.drawable.baseline_favorite_24);
        else
            //Update the like icon to be outline if the user has liked the post
            holder.getPostLikeButton().setImageResource(R.drawable.round_favorite_border_24);
    }

    /**
     * Load the user data for the given post.
     *
     * @param holder The ViewHolder to be updated
     * @param post   The post to load the user data for
     */
    private void loadUserData(ViewHolder holder, FeedPost post) {
        userService.getUser(post.getIdOwner(), holder, this);
    }

    /**
     * Set the post details (timestamp and cooking time) for the given post.
     *
     * @param holder The ViewHolder to be updated
     * @param post   The post to set the details for
     */
    private void setPostDetails(ViewHolder holder, FeedPost post) {
        String completeDate = postService.dateFormat(post.getDate());
        holder.getPostTimeStamp().setText(completeDate);
        holder.getPostCookingTime().setText(String.format("%d %s", post.getRecipe().getPreparationTime(), post.getRecipe().getPreparationTimeScale()));
    }

    /**
     * Load the post image and set the number of comments for the given post.
     *
     * @param holder The ViewHolder to be updated
     * @param post   The post to set the image and comments for
     */
    private void setPostImageAndComments(ViewHolder holder, FeedPost post) {
        holder.getPostCommentsAmount().setText(Integer.toString(post.getComments().size()));
        postService.loadImage(holder.getPostImage(), post.getPostId());
    }

    /**
     * Set the number of likes and the like button's appearance for the given post.
     *
     * @param holder The ViewHolder to be updated
     * @param post   The post to set the likes and like button for
     */
    private void setLikeButtonAppearance(ViewHolder holder, FeedPost post) {
        holder.getPostLikesAmount().setText(Integer.toString(post.getLikes().size()));

        if (postService.hasLikedPost(post)) {
            holder.getPostLikeButton().setImageResource(R.drawable.baseline_favorite_24);
        } else {
            holder.getPostLikeButton().setImageResource(R.drawable.round_favorite_border_24);
        }
    }

    @Override
    public int getItemCount()
    {
        return itemCount;
    }

    @Override
    public void addUserData(User user) {

    }

    @Override
    public void addUserData(User user, ViewHolder holder) {
        holder.getPostAuthor().setText(user.getUsername());
    }

    @Override
    public void addUserData(User user, PostCommentsAdapter.ViewHolder viewHolder) {

    }

    @Override
    public void addUserData(User user, com.bitebybyte.ui.saved.ViewHolder viewHolder) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView postTitle;
        private final TextView postTimeStamp;
        private final TextView postCookingTime;
        private final TextView postAuthor;
        private final TextView  postLikesAmount;
        private final TextView  postCommentsAmount;
        private final ImageView postAuthorProfilePicture;
        private final ImageView postImage;
        private final ImageView postLikeButton;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            postTitle          = itemView.findViewById(R.id.postTitleTextView);
            postTimeStamp      = itemView.findViewById(R.id.postTimeStampTextView);
            postCookingTime    = itemView.findViewById(R.id.postCookingTimeTextView);
            postAuthor         = itemView.findViewById(R.id.postAuthor);
            postLikesAmount    = itemView.findViewById(R.id.postLikesAmountTextView);
            postLikeButton     = itemView.findViewById(R.id.postLikesIcon);
            postCommentsAmount = itemView.findViewById(R.id.postCommentsAmountTextView);

            postAuthorProfilePicture = itemView.findViewById(R.id.postAuthorProfilePicture);
            postImage                = itemView.findViewById(R.id.postImageView);

        }

        public TextView getPostTitle()
        {
            return postTitle;
        }

        public TextView getPostTimeStamp()
        {
            return postTimeStamp;
        }

        public TextView getPostCookingTime()
        {
            return postCookingTime;
        }

        public TextView getPostAuthor()
        {
            return postAuthor;
        }

        public TextView getPostLikesAmount()
        {
            return postLikesAmount;
        }

        public TextView getPostCommentsAmount()
        {
            return postCommentsAmount;
        }

        public ImageView getPostAuthorProfilePicture()
        {
            return postAuthorProfilePicture;
        }

        public ImageView getPostImage()
        {
            return postImage;
        }
        public ImageView getPostLikeButton() {return postLikeButton;}
    }
}

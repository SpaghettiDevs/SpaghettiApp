package com.bitebybyte.ui.saved.recipes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;
import com.bitebybyte.ServiceablePostFragment;
import com.bitebybyte.ServiceableUserFragment;
import com.bitebybyte.backend.models.FeedPost;
import com.bitebybyte.backend.models.User;
import com.bitebybyte.backend.services.PostService;
import com.bitebybyte.backend.services.UserService;
import com.bitebybyte.holders.AbstractViewHolder;
import com.bitebybyte.holders.CompactViewHolder;

import java.util.List;

/**
 * Adapter for displaying a list of user's saved recipes.
 */
public class SavedRecipesAdapter extends RecyclerView.Adapter<CompactViewHolder>
        implements ServiceablePostFragment, ServiceableUserFragment {

    private final List<String> postIds;
    private final PostService postService;
    private final UserService userService;

    /**
     * Constructor for SavedRecipesAdapter.
     * 
     * @param postIds List of post IDs to be displayed in the adapter
     */
    SavedRecipesAdapter(List<String> postIds) {
        this.postIds = postIds;
        postService = new PostService();
        userService = new UserService();
    }

    @NonNull
    @Override
    public CompactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // specify which xml layout to use for the recycler view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_compact, parent, false);

        return new CompactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedViewHolder holder, int position) {
        postService.inflatePostById(postIds.get(position), this, holder, "posts");
    }

    @Override
    public int getItemCount() {
        return postIds.size();
    }

    @Override
    public void addDataToView(FeedPost post) {

    }

    @Override
    public void addDataToView(FeedPost post, AbstractViewHolder holder) {
        if (post == null) {
            // Display message for deleted post
            deletedPost(holder);
            return;
        }

        // Display post data
        holder.getPostTitle().setText(post.getTitle());
        userService.getUser(post.getIdOwner(), holder, this);

        int cookingTime = post.getRecipe().getPreparationTime();
        String cookingTimeScale = post.getRecipe().getPreparationTimeScale();

        holder.getPostCookingTime().setText(String.format("%d %s", cookingTime, cookingTimeScale));

        postService.loadImage(holder.getPostImage(), post.getPostId(), "images/");

        // TODO: Load user profile image from firebase if it is set

        // Add delete button listener
        holder.getDeletePostButton().setOnClickListener(v -> onDeleteButtonClicked((CompactViewHolder) holder));
    }

    /**
     * Handles the delete button click.
     * Removes the post from the saved recipes list.
     * Displays a message to the user.
     * 
     * @param holder the view holder for the deleted post
     */
    private void onDeleteButtonClicked(CompactViewHolder holder) {
        System.out.println("Delete button clicked");
        // Remove post from saved recipes and display message
        userService.removeSavedPost(holder.getAdapterPosition());

        // Notify the adapter that the data has changed
        notifyItemRemoved(holder.getAdapterPosition());

        // TODO this toast doesn't work. The changes apply after refreshing!
        Toast.makeText(holder.getDeletePostButton().getContext(), "Unsaved post", Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays message for deleted post.
     *
     * @param holder the view holder for the deleted post
     */
    private void deletedPost(AbstractViewHolder holder) {
        // Display message for deleted post
        holder.getPostTitle().setText("Deleted post");
        holder.getPostAuthor().setText("");
        holder.getPostCookingTime().setText("That's sad!");

        // Add delete button listener
        holder.getDeletePostButton().setOnClickListener(v -> {
            System.out.println("Delete button clicked");
            // Remove post from saved recipes and display message
            userService.removeSavedPost(holder.getAdapterPosition());

            // Notify the adapter that the data has changed
            notifyItemRemoved(holder.getAdapterPosition());
        });
    }

    @Override
    public void getListOfPosts(List<FeedPost> posts) {

    }

    @Override
    public void addUserData(User user) {

    }

    @Override
    public void addUserData(User user, AbstractViewHolder viewHolder) {
        viewHolder.getPostAuthor().setText(user.getUsername());
    }
}

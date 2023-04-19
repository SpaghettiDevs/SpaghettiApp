package com.bitebybyte.ui.saved.my;

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
public class MyRecipesAdapter extends RecyclerView.Adapter<CompactViewHolder>
        implements ServiceablePostFragment, ServiceableUserFragment {

    private final List<String> postIds;
    private final PostService postService;
    private final UserService userService;

    /**
     * Constructor for MyRecipesAdapter.
     *
     * @param postIds List of post IDs to be displayed in the adapter
     */
    MyRecipesAdapter(List<String> postIds) {
        this.postIds = postIds;
        postService = new PostService();
        userService = new UserService();
    }

    /**
     * Inflates the layout for each list item in the adapter.
     *
     * @param parent   The ViewGroup into which the new View will be added
     * @param viewType The type of the new view
     * @return The new ViewHolder
     */
    @NonNull
    @Override
    public CompactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_compact, parent, false);

        return new CompactViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder   The ViewHolder which should be updated to represent the
     *                 contents of the item at the given position in the data set
     * @param position The position of the item within the adapter's data set
     */
    @Override
    public void onBindViewHolder(@NonNull SavedViewHolder holder, int position) {
        postService.inflatePostById(postIds.get(position), this, holder, "posts");
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter
     */
    @Override
    public int getItemCount() {
        return postIds.size();
    }

    @Override
    public void addDataToView(FeedPost post) {
        // Not used yet
    }

    /**
     * Adds data to the ViewHolder for the given post.
     *
     * @param post   The FeedPost object to be added to the view
     * @param holder The ViewHolder that should be updated to represent the contents
     *               of the item at the given position in the data set
     */
    @Override
    public void addDataToView(FeedPost post, AbstractViewHolder holder) {
        // TODO: Handle case where post has been deleted by moderator in Firebase
        // -- Then the post doesn't exist so we don't even get here right? -Tristan
        holder.getPostTitle().setText(post.getTitle());
        userService.getUser(post.getIdOwner(), holder, this);

        // Set the preparation time test
        int preparationTime = post.getRecipe().getPreparationTime();
        String preparationTimeScale = post.getRecipe().getPreparationTimeScale();

        holder.getPostCookingTime().setText(String.format("%d %s", preparationTime, preparationTimeScale));

        // Load post image
        postService.loadImage(holder.getPostImage(), post.getPostId(), "images/");

        // Set delete button listener
        holder.getDeletePostButton().setOnClickListener(v -> onDeleteButtonClicked((CompactViewHolder) holder, post));
    }

    /**
     * Called when the delete button is clicked for a post in the adapter.
     * Deletes the post from the database and displays a toast message.
     * 
     * @param holder The ViewHolder for the post being deleted
     * @param post   The FeedPost object for the post being deleted
     */
    private void onDeleteButtonClicked(SavedViewHolder holder, FeedPost post) {
        postService.deletePost(post.getPostId(), "posts");
        Toast.makeText(holder.getDeletePostButton().getContext(), "Post Deleted", Toast.LENGTH_SHORT).show();

        // Notifies the adapter that the data has changed
        notifyItemRemoved(holder.getAdapterPosition());
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

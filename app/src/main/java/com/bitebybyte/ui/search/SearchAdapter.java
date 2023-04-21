package com.bitebybyte.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;
import com.bitebybyte.ServiceableUserFragment;
import com.bitebybyte.backend.models.FeedPost;
import com.bitebybyte.backend.models.User;
import com.bitebybyte.backend.services.PostService;
import com.bitebybyte.backend.services.UserService;
import com.bitebybyte.holders.AbstractViewHolder;
import com.bitebybyte.holders.CompactViewHolder;

import java.util.List;

/**
 * Adapter that takes search results from the backend and feeds that to the recycler view.
 */
public class SearchAdapter extends RecyclerView.Adapter<CompactViewHolder>
        implements ServiceableUserFragment
{
    int itemCount;
    List<FeedPost> posts;
    UserService userService;
    PostService postService;

    public SearchAdapter(List<FeedPost> posts) {
        this.posts = posts;
        this.itemCount = posts.size();
        userService = new UserService();
        postService = new PostService();
    }

    @NonNull
    @Override
    public CompactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.post_compact, parent, false);

        return new CompactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompactViewHolder holder, int position)
    {
        FeedPost post = posts.get(position);

        holder.getPostTitle().setText(post.getTitle());
        postService.loadImage(holder.getPostImage(), post.getPostId(), "images/");

        // Get the username and user image
        loadUserData(holder, post);

        holder.getPostTitle().setText(post.getTitle());
        holder.getPostCookingTime().setText(String.format("%d %s", post.getRecipe().getPreparationTime(),
                post.getRecipe().getPreparationTimeScale()));

        holder.getDeletePostButton().setVisibility(View.GONE);

    }

    @Override
    public int getItemCount()
    {
        return itemCount;
    }

    /**
     * Load the user data for the given post.
     *
     * @param holder The CompactViewHolder to be updated
     * @param post   The post to load the user data for
     */
    private void loadUserData(CompactViewHolder holder, FeedPost post) {
        userService.getUser(post.getIdOwner(), holder, this);
    }

    @Override
    public void addUserData(User user) {

    }

    @Override
    public void addUserData(User user, AbstractViewHolder holder) {
        holder.getPostAuthor().setText(user.getUsername());
        // TODO add author profile image
    }
}

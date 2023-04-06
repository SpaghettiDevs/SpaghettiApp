package com.bitebybyte.ui.saved.recipes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;
import com.bitebybyte.backend.database.PostService;
import com.bitebybyte.backend.database.UserService;
import com.bitebybyte.backend.local.FeedPost;
import com.bitebybyte.ui.ServicableFragment;
import com.bitebybyte.ui.saved.ViewHolder;

import java.util.List;

public class SavedRecipesAdapter extends RecyclerView.Adapter<ViewHolder> implements ServicableFragment {

    private List<String> postIds;
    private PostService postService;
    private UserService userService;

    SavedRecipesAdapter(List<String> postIds) {
        this.postIds = postIds;
        postService = new PostService();
        userService = new UserService();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // specify which xml layout to use for the recycler view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_saved, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        postService.getPostById(postIds.get(position), this, holder);
    }

    @Override
    public int getItemCount() {
        return postIds.size();
    }

    @Override
    public void addDataToView(FeedPost post) {

    }

    @Override
    public void addDataToView(FeedPost post, ViewHolder holder) {

        if (post == null) {
            Log.e("addDataToView", "Post at this positon doesn't exist (position unknown)");
            this.deletedPost(holder);
            return;
        }

        holder.getPostTitle().setText(post.getTitle());
        holder.getPostAuthor().setText(post.getIdOwner());
        holder.getPostCookingTime().setText(String.format("%d %s", post.getRecipe().getPreparationTime(), post.getRecipe().getPreparationTimeScale()));

        postService.loadImage(holder.getPostImage(), post.getPostId());
        //TODO: Load user profile image from firebase if it is set

        //Add delete button listener
        holder.getDeletePostButton().setOnClickListener(v -> {
            System.out.println("Delete button clicked");
            String msg = userService.updateSavedPosts(post.getPostId());
            //Toast.makeText(holder.getDeletePostButton().getContext(), msg, Toast.LENGTH_SHORT);
            //TODO this toast doesn't work. The changes apply after refreshing!
        });
    }

    private void deletedPost(ViewHolder holder) {
        holder.getPostTitle().setText("Deleted post");
        holder.getPostAuthor().setText("");
        holder.getPostCookingTime().setText("That's sad!");

        holder.getDeletePostButton().setOnClickListener(v -> {
            System.out.println("Delete button clicked");
            String msg = userService.updateSavedPosts(holder.getAdapterPosition());
            //Toast.makeText(holder.getDeletePostButton().getContext(), msg, Toast.LENGTH_SHORT);

            //TODO refresh here or the rest of the saved recipes will bug.
            //TODO this toast doesn't work. The changes apply after refreshing!
        });
    }

    @Override
    public void getListOfPosts(List<FeedPost> posts) {

    }
}



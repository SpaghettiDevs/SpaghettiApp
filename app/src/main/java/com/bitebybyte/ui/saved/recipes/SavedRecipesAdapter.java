package com.bitebybyte.ui.saved.recipes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;
import com.bitebybyte.backend.database.PostService;
import com.bitebybyte.backend.local.FeedPost;
import com.bitebybyte.ui.ServicableFragment;
import com.bitebybyte.ui.saved.ViewHolder;

import java.util.List;

public class SavedRecipesAdapter extends RecyclerView.Adapter<ViewHolder> implements ServicableFragment {

    private List<String> postIds;
    private PostService postService;

    SavedRecipesAdapter(List<String> postIds) {
        this.postIds = postIds;
        postService = new PostService();
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
        holder.getPostTitle().setText(post.getTitle());
        holder.getPostAuthor().setText(post.getIdOwner());
        holder.getPostCookingTime().setText(Integer.toString(post.getRecipe().getPreparationTime()));

        postService.loadImage(holder.getPostImage(), post.getPostId());
        //TODO: Load user profile image from firebase if it is set

        //Add delete button listener
        holder.getDeletePostButton().setOnClickListener(v -> {
            System.out.println("Delete button clicked");
        });
    }

    @Override
    public void getListOfPosts(List<FeedPost> posts) {

    }
}



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
import com.bitebybyte.backend.database.PostService;
import com.bitebybyte.backend.local.FeedPost;

import java.util.List;

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.ViewHolder>
{
    private NavController navController;
    int itemCount;
    List<FeedPost> posts;
    PostService postService;

    public HomeFeedAdapter(List<FeedPost> posts) {
        this.itemCount = posts.size();
        this.posts = posts;
        postService = new PostService();
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

    // Note that the random values change if you reload the fragment
    // TODO: Connect the view with a database/firebase
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        FeedPost post = posts.get(position);
        holder.getPostTitle().setText(post.getTitle());

        //TODO get the name of the owner not the ID.
        holder.getPostAuthor().setText(post.getIdOwner());
        //Creating correct date
        String completeDate = postService.dateFormat(post.getDate());
        holder.getPostTimeStamp().setText(completeDate);
        holder.getPostCookingTime().setText(Integer.toString(post.getRecipe().getPreparationTime()));
        //TODO add comments below
        //holder.getPostCommentsAmount().setText(post.getComents().length());

        postService.loadImage(holder.getPostImage(), post.getPostId());
        //TODO add image from received URL.

        //set the likes when the post is loaded
            holder.getPostLikesAmount().setText(Integer.toString(post.getLikes().size()));

        //update the likes when someone presses the like button
        holder.getPostLikeButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postService.updateLikes(post);
                holder.getPostLikesAmount().setText(Integer.toString(post.getLikes().size()));
            }
        });

        holder.getPostTitle().setOnClickListener(event -> {
            NavDirections action = HomeFragmentDirections.actionNavigationHomeToPostDetail(post.getPostId());
            navController.navigate(action);
        });

        holder.getPostImage().setOnClickListener(event -> {
            NavDirections action = HomeFragmentDirections.actionNavigationHomeToPostDetail(post.getPostId());
            navController.navigate(action);
        });
    }

    // For now, display 25 posts in the recycler view
    @Override
    public int getItemCount()
    {
        return itemCount;
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

package com.bitebybyte.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;
import com.bitebybyte.backend.local.FeedPost;

import java.util.List;
import java.util.function.BiFunction;

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.ViewHolder>
{

    int itemCount;
    List<FeedPost> posts;

    public HomeFeedAdapter(List<FeedPost> posts) {
        this.itemCount = posts.size();
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // specify which xml layout to use for the recycler view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_home, parent,
                                                                     false);
        return new ViewHolder(view);
    }

    // Note that the random values change if you reload the fragment
    // TODO: Connect the view with a database/firebase
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        // returns a random integer within the range [min, max]
//        BiFunction<Integer, Integer, Integer> randomInt = (min, max) -> (int) Math.floor(
//                Math.random() * (max - min + 1) + min);

        // cycle through the same post titles
        //String[] titles = {"Lasagne", "Hamburger", "Pizza", "Hutspot"};
        FeedPost post = posts.get(position);
        holder.getPostTitle().setText(post.getTitle());

        // cycle through the same post authors
        //String[] names = {"Ashley", "John", "Bob", "Emma"};
        //TODO get the name of the owner not the ID.
        holder.getPostAuthor().setText(post.getIdOwner());

        holder.getPostTimeStamp().setText(post.getDate().toString());
        holder.getPostCookingTime().setText(Integer.toString(post.getRecipe().getPreparationTime()));
        //TODO add comments below
        //holder.getPostCommentsAmount().setText(post.getComents().length());
        holder.getPostLikesAmount().setText(Integer.toString(post.getLikes()));
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

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            postTitle          = itemView.findViewById(R.id.postTitleTextView);
            postTimeStamp      = itemView.findViewById(R.id.postTimeStampTextView);
            postCookingTime    = itemView.findViewById(R.id.postCookingTimeTextView);
            postAuthor         = itemView.findViewById(R.id.postAuthor);
            postLikesAmount    = itemView.findViewById(R.id.postLikesAmountTextView);
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
    }
}

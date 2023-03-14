package com.bitebybyte.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;

import java.util.function.BiFunction;

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.ViewHolder>
{
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
        BiFunction<Integer, Integer, Integer> randomInt = (min, max) -> (int) Math.floor(
                Math.random() * (max - min + 1) + min);

        // cycle through the same post titles
        String[] titles = {"Lasagne", "Hamburger", "Pizza", "Hutspot"};
        holder.getPostTitle().setText(titles[position % titles.length]);

        // cycle through the same post authors
        String[] names = {"Ashley", "John", "Bob", "Emma"};
        holder.getPostAuthor().setText(names[position % names.length]);

        // use random numbers as placeholders for real data
        holder.getPostTimeStamp().setText(String.format("%dd ago", randomInt.apply(1, 31)));
        holder.getPostCookingTime().setText(String.format("%d min", randomInt.apply(5, 59)));
        holder.getPostCommentsAmount().setText(String.format("%d", randomInt.apply(0, 50)));
        holder.getPostLikesAmount().setText(String.format("%d", randomInt.apply(0, 50)));
    }

    // For now, display 25 posts in the recycler view
    @Override
    public int getItemCount()
    {
        return 25;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView  postTitle;
        private TextView  postTimeStamp;
        private TextView  postCookingTime;
        private TextView  postAuthor;
        private TextView  postLikesAmount;
        private TextView  postCommentsAmount;
        private ImageView postAuthorProfilePicture;
        private ImageView postImage;

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

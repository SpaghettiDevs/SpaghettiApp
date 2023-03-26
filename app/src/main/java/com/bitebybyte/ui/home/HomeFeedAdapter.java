package com.bitebybyte.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;
import com.bitebybyte.backend.database.PostService;
import com.bitebybyte.backend.local.FeedPost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.ViewHolder>
{

    int itemCount;
    List<FeedPost> posts;
    PostService postService;
    FirebaseFirestore db;

    public HomeFeedAdapter(List<FeedPost> posts) {
        this.itemCount = posts.size();
        this.posts = posts;
        postService = new PostService();
        this.db = FirebaseFirestore.getInstance();

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
        FeedPost post = posts.get(position);
        holder.getPostTitle().setText(post.getTitle());

        //TODO get the name of the owner not the ID.
        holder.getPostAuthor().setText(post.getIdOwner());

        holder.getPostTimeStamp().setText(post.getDate().toString());
        holder.getPostCookingTime().setText(Integer.toString(post.getRecipe().getPreparationTime()));
        //TODO add comments below
        //holder.getPostCommentsAmount().setText(post.getComents().length());

        postService.loadImage(holder.getPostImage(), post.getPostId());
        //TODO add image from received URL.

        //set the likes in the beginning
        holder.getPostLikesAmount().setText(Integer.toString(post.getLikes()));
        System.out.println(post.getLikes());

        //update the likes when someone presses the like button
        holder.getPostLikeButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // update the likes amount
                DocumentReference postRef = db.collection("posts").document(holder.getPostUUID());
                postRef
                        .update("likes", Integer.parseInt(holder.getPostLikesAmount().getText().toString()) + 1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                System.out.println("DocumentSnapshot successfully updated!");
                                System.out.println(post.getLikes());
                                post.incrementLikes();
                                holder.getPostLikesAmount().setText(Integer.toString(post.getLikes()));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("Error updating document:" + e);
                            }
                        });
              // holder.getPostLikesAmount().setText(Integer.toString(post.getLikes()));
            }
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
        private final String postUUID;

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
            postUUID                 = "9b1180a0-4d88-4f42-9240-11036ac2b0c9";
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
        public String getPostUUID() {return postUUID;}
    }
}

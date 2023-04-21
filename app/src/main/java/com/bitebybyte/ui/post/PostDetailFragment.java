package com.bitebybyte.ui.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bitebybyte.R;
import com.bitebybyte.ServiceablePostFragment;
import com.bitebybyte.ServiceableUserFragment;
import com.bitebybyte.backend.models.FeedPost;
import com.bitebybyte.backend.models.User;
import com.bitebybyte.backend.services.PostService;
import com.bitebybyte.backend.services.UserService;
import com.bitebybyte.databinding.FragmentPostDetailBinding;
import com.bitebybyte.holders.AbstractViewHolder;

import java.util.List;

public class PostDetailFragment extends Fragment
        implements ServiceablePostFragment, ServiceableUserFragment {

    private FragmentPostDetailBinding binding;
    private UserService userService;

    private TextView title;
    private TextView estimatedTime;
    private TextView description;
    private TextView ingredients;
    private TextView method;
    private TextView author;
    private ImageView authorImage;
    private TextView likeAmount;
    private TextView commentAmount;
    private ImageView likeIcon;
    private ImageView commentIcon;
    private ImageView bookmarkIcon;
    private TextView addComment;
    private NavController navController;

    private PostService postService;
    private FeedPost post;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPostDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (getActivity() == null)
            throw new RuntimeException("Activity is null");

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);

        //Check if the post id was provided.
        if (getArguments() == null) {
            Toast.makeText(getContext(), "No post id provided", Toast.LENGTH_SHORT).show();

            //Navigate back to the home page.
            navController.navigate(R.id.navigation_home);
        }

        //Initialize the services
        postService = new PostService();
        userService = new UserService();

        initializeUI(root);

        //Find the add comment edit text
        addComment = root.findViewById(R.id.post_detail_comment_input);

        //Add event listener for the add comment button
        addComment.setOnClickListener(this::onCommentButtonPressed);
        commentIcon.setOnClickListener(this::onCommentButtonPressed);

        String postId = PostDetailFragmentArgs.fromBundle(getArguments()).getPostId();
        postService.inflatePostById(postId, this);

        return root;
    }

    /**
     * Initialize the UI elements.
     *
     * @param view The view to initialize the UI elements in.
     */
    private void initializeUI(View view) {
        //Find the text-inputs
        title = view.findViewById(R.id.post_detail_title);
        estimatedTime = view.findViewById(R.id.post_detail_cooking_time_text);
        description = view.findViewById(R.id.post_detail_description_text);
        ingredients = view.findViewById(R.id.post_detail_ingredients_text);
        method = view.findViewById(R.id.post_detail_method_text);
        author = view.findViewById(R.id.post_detail_author_name);
        authorImage = view.findViewById(R.id.post_detail_author_picture);
        likeAmount = view.findViewById(R.id.post_detail_likes_amount);
        commentAmount = view.findViewById(R.id.post_detail_comment_amount);

        //Find the image-inputs
        likeIcon = view.findViewById(R.id.post_detail_likes_icon);
        commentIcon = view.findViewById(R.id.post_detail_comment_icon);
        bookmarkIcon = view.findViewById(R.id.post_detail_bookmark_icon);
    }

    /**
     * This method is called when the user clicks on the like button.
     * Updates the like icon and the like amount.
     *
     * @param view The view that was clicked.
     */
    private void onLikeButtonPressed(View view) {
        int oldLikes = post.getLikes().size();
        postService.updateLikes(post);
        int newLikes = post.getLikes().size();

        if (oldLikes < newLikes)
            //Update the like icon to be solid if the user has liked the post
            likeIcon.setImageResource(R.drawable.baseline_favorite_24);
        else
            //Update the like icon to be outline if the user has liked the post
            likeIcon.setImageResource(R.drawable.round_favorite_border_24);

        likeAmount.setText(String.valueOf(newLikes));
    }

    private void onBookmarkButtonPressed(View view) {
        boolean didSave = userService.updateSavedPosts(post.getPostId());

        if(didSave) {
            //Update the bookmark icon to be solid if the user has bookmarked the post
            bookmarkIcon.setImageResource(R.drawable.ic_favorites_black_filled_24);
            Toast.makeText(this.getContext(), "Post saved", Toast.LENGTH_SHORT).show();
        } else {
            //Update the bookmark icon to be outline if the user has bookmarked the post
            bookmarkIcon.setImageResource(R.drawable.ic_favorites_black_24dp);
            Toast.makeText(this.getContext(), "Post unsaved", Toast.LENGTH_SHORT).show();
        }

    }

    private void onCommentButtonPressed(View view) {
        //Navigate to the comments page.
        NavDirections action = PostDetailFragmentDirections.actionPostDetailToPostComments(post.getPostId());
        navController.navigate(action);
    }

    @Override
    public void addDataToView(FeedPost post) {
        this.post = post;

        title.setText(post.getTitle());
        estimatedTime.setText(String.format("%d %s", post.getRecipe().getPreparationTime(), post.getRecipe().getPreparationTimeScale()));
        description.setText(post.getContent());
        ingredients.setText(post.getRecipe().getIngredients());
        method.setText(post.getRecipe().getMethods());
        userService.getUser(post.getIdOwner(), this);
        likeAmount.setText(Integer.toString(post.getLikes().size()));
        commentAmount.setText(Integer.toString(post.getComments().size()));

        //Add event listener for the like button
        likeIcon.setOnClickListener(this::onLikeButtonPressed);

        //Add event listener for the bookmark button
        bookmarkIcon.setOnClickListener(this::onBookmarkButtonPressed);

        //Check if this user has liked the post on load
        if (postService.hasLikedContent(post))
            //Update the like icon to be solid if the user has liked the post
            likeIcon.setImageResource(R.drawable.baseline_favorite_24);
        else
            //Update the like icon to be outline if the user has liked the post
            likeIcon.setImageResource(R.drawable.round_favorite_border_24);

        //Check if the user has bookmarked the post on load
        if (userService.userSavedPost(post.getPostId()))
            //Update the bookmark icon to be solid if the user has bookmarked the post
            bookmarkIcon.setImageResource(R.drawable.ic_favorites_black_filled_24);
        else
            //Update the bookmark icon to be outline if the user has bookmarked the post
            bookmarkIcon.setImageResource(R.drawable.ic_favorites_black_24dp);
    }

    @Override
    public void addDataToView(FeedPost post, AbstractViewHolder holder) {

    }

    @Override
    public void getListOfPosts(List<FeedPost> posts) {

    }

    @Override
    public void addUserData(User user) {
        author.setText(user.getUsername());
        postService.loadImage(authorImage, user.getUserId(), "pfPictures/");
    }

    @Override
    public void addUserData(User user, AbstractViewHolder viewHolder) {

    }
}
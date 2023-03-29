package com.bitebybyte.ui.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bitebybyte.R;
import com.bitebybyte.backend.database.PostService;
import com.bitebybyte.backend.local.FeedPost;
import com.bitebybyte.databinding.FragmentPostDetailBinding;
import com.bitebybyte.ui.ServicableFragment;

import java.util.List;

public class PostDetailFragment extends Fragment implements ServicableFragment {

    private FragmentPostDetailBinding binding;

    private TextView title;
    private TextView estimatedTime;
    private TextView description;
    private TextView ingredients;
    private TextView method;
    private TextView author;
    private TextView likeAmount;
    private TextView commentAmount;
    private ImageView authorImage;
    private ImageView likeIcon;
    private ImageView commentIcon;
    private ImageView bookmarkIcon;
    private TextView addComment;

    private NavController navController;

    private PostService postService;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPostDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
        postService = new PostService();

        //Find the text-inputs
        title = root.findViewById(R.id.post_detail_title);
        estimatedTime = root.findViewById(R.id.post_detail_cooking_time_text);
        description = root.findViewById(R.id.post_detail_description_text);
        ingredients = root.findViewById(R.id.post_detail_ingredients_text);
        method = root.findViewById(R.id.post_detail_method_text);
        author = root.findViewById(R.id.post_detail_author_name);
        likeAmount = root.findViewById(R.id.post_detail_likes_amount);
        commentAmount = root.findViewById(R.id.post_detail_comment_amount);

        //Find the image-inputs
        authorImage = root.findViewById(R.id.post_detail_author_picture);
        likeIcon = root.findViewById(R.id.post_detail_likes_icon);
        commentIcon = root.findViewById(R.id.post_detail_comment_icon);
        bookmarkIcon = root.findViewById(R.id.post_detail_bookmark_icon);

        //Find the add comment edit text
        addComment = root.findViewById(R.id.post_detail_comment_input);
        String postId = PostDetailFragmentArgs.fromBundle(getArguments()).getPostId();

        commentIcon.setOnClickListener(event -> {
            //Navigate to the comments page.
            NavDirections action = PostDetailFragmentDirections.actionPostDetailToPostComments(postId);
            navController.navigate(action);
        });

        bookmarkIcon.setOnClickListener(event -> {
            //Handle bookmarking.
        });

        //Add event listener for the add comment button
        addComment.setOnClickListener(event -> {
            //Navigate to the comments page.
            NavDirections action = PostDetailFragmentDirections.actionPostDetailToPostComments(postId);
            navController.navigate(action);
        });
        postService.getPostById(postId, this);
        return root;


    }

    @Override
    public void addDataToView(FeedPost post) {
        title.setText(post.getTitle());
        estimatedTime.setText(Integer.toString(post.getRecipe().getPreparationTime()));
        description.setText(post.getContent());
        ingredients.setText(post.getRecipe().getIngredients());
        method.setText(post.getRecipe().getMethods());
        author.setText(post.getIdOwner());
        likeAmount.setText(Integer.toString(post.getLikes().size()));
        commentAmount.setText(Integer.toString(post.getComments().size()));

        //Add event listeners for the icons
        likeIcon.setOnClickListener(event -> {
            postService.updateLikes(post);
            likeAmount.setText(Integer.toString(post.getLikes().size()));
        });
    }

    @Override
    public void getListOfPosts(List<FeedPost> posts) {

    }
}
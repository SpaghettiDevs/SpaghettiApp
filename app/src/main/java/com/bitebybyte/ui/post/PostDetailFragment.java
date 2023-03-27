package com.bitebybyte.ui.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bitebybyte.R;
import com.bitebybyte.databinding.FragmentPostDetailBinding;
import com.bitebybyte.ui.home.HomeFragmentDirections;

public class PostDetailFragment extends Fragment {

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
    private EditText addComment;

    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPostDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);

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



        //Add event listeners for the icons
        likeIcon.setOnClickListener(event -> {
            //Handle post like.
        });

        commentIcon.setOnClickListener(event -> {
            //Navigate to the comments page.
            NavDirections action = HomeFragmentDirections.actionNavigationHomeToPostDetail();
            navController.navigate(action);
        });

        bookmarkIcon.setOnClickListener(event -> {
            //Handle bookmarking.
        });

        //Add event listener for the add comment button
        addComment.setOnClickListener(event -> {
            //Navigate to the comments page.
            NavDirections action = HomeFragmentDirections.actionNavigationHomeToPostDetail();
            navController.navigate(action);
        });

        //TODO: Add firebase code to populate the post details.

        return root;


    }

}
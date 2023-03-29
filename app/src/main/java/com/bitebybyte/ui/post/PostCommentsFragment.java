package com.bitebybyte.ui.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;
import com.bitebybyte.backend.database.PostService;
import com.bitebybyte.backend.local.FeedPost;
import com.bitebybyte.ui.ServicableFragment;
import com.bitebybyte.ui.home.HomeItemDecoration;

import java.util.List;

public class PostCommentsFragment extends Fragment implements ServicableFragment {
    private RecyclerView commentsRecycler;
    private PostService postService;
    private FeedPost post;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_comments, container, false);
        postService = new PostService();
        String postId = PostCommentsFragmentArgs.fromBundle(getArguments()).getPostId();

        commentsRecycler = view.findViewById(R.id.post_comments_recycler);

        commentsRecycler.setHasFixedSize(true);

        //Display linearly
        commentsRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));

        postService.getPostById(postId, this);

        //Find input and button
        ConstraintLayout commentInputHolder = view.findViewById(R.id.comment_input_holder);
        EditText commentInput = commentInputHolder.findViewById(R.id.post_comments_input);
        ImageButton commentButton = commentInputHolder.findViewById(R.id.post_comments_send_button);

        //Set the button to send the comment
        commentButton.setOnClickListener(v -> {
            if (!commentInput.getText().toString().equals("")) {
                postService.addComment(post, commentInput.getText().toString());

                System.out.println("Comment: " + commentInput.getText().toString());
                // TODO Add Toast response to the user

                commentInput.setText("");
            }
        });

        // use ItemDecoration to get consistent margins inbetween items
        HomeItemDecoration decoration = new HomeItemDecoration(32, 1);
        commentsRecycler.addItemDecoration(decoration);

        return view;
    }

    @Override
    public void addDataToView(FeedPost post) {
        //Set the adapter and add the data there
        commentsRecycler.setAdapter(new PostCommentsAdapter(post));
        this.post = post;
    }

    @Override
    public void getListOfPosts(List<FeedPost> posts) {

    }
}
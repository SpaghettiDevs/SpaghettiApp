package com.bitebybyte.ui.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;
import com.bitebybyte.backend.services.PostService;
import com.bitebybyte.backend.models.FeedPost;
import com.bitebybyte.ServiceablePostFragment;
import com.bitebybyte.holders.AbstractViewHolder;
import com.bitebybyte.ui.home.HomeItemDecoration;

import java.util.List;

public class PostCommentsFragment extends Fragment implements ServiceablePostFragment {
    private RecyclerView commentsRecycler;
    private PostService postService;
    private FeedPost post;

    /**
     * Inflate the layout for this fragment and initialize the view elements.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_comments, container, false);

        postService = new PostService();

        //If the arguments are null, display an error message and return back to the previous fragment
        if(getArguments() == null) {
            Toast.makeText(this.getContext(), "Error: No post id found", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
            return view;
        }

        String postId = PostCommentsFragmentArgs.fromBundle(getArguments()).getPostId();

        //Set up recycler view
        commentsRecycler = view.findViewById(R.id.post_comments_recycler);
        commentsRecycler.setHasFixedSize(true);
        commentsRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // Use ItemDecoration to get consistent margins between items
        HomeItemDecoration decoration = new HomeItemDecoration(32, 1);
        commentsRecycler.addItemDecoration(decoration);

        //Inflate the view with the post data
        postService.inflatePostById(postId, this);

        // Initialize the comment input field and send button
        initializeCommentInput(view);

        return view;
    }

    /**
     * Add the comments data to the view by setting the adapter and adding the data.
     */
    @Override
    public void addDataToView(FeedPost post) {
        commentsRecycler.setAdapter(new PostCommentsAdapter(post));
        this.post = post;
    }

    /**
     * Initialize the comment input field and send button, and set the button to send the comment when clicked.
     *
     * @param view The view to initialize the comment input field and send button in.
     */
    private void initializeCommentInput(View view) {
        ConstraintLayout commentInputHolder = view.findViewById(R.id.comment_input_holder);
        EditText commentInput = commentInputHolder.findViewById(R.id.post_comments_input);
        ImageButton commentButton = commentInputHolder.findViewById(R.id.post_comments_send_button);

        commentButton.setOnClickListener(v -> onCommentButtonClicked(commentInput));
    }

    /**
     * Send the comment when the send button is clicked.
     * First checks if the comment is empty, then adds the comment to the post and clears the input field.
     * If the comment is empty, show a message and set the error on the input field.
     * @param commentInput The input field to get the comment from.
     */
    private void onCommentButtonClicked(EditText commentInput) {
        if(commentInput.getText().toString().equals("")) {
            Toast.makeText(this.getContext(), "Comment cannot be empty", Toast.LENGTH_SHORT).show();
            commentInput.setError("Comment cannot be empty");
            return;
        }

        // Add the comment to the post
        postService.addComment(post, commentInput.getText().toString());

        // Clear the input field and show a message
        commentInput.setText("");
        Toast.makeText(this.getContext(), "Comment Posted", Toast.LENGTH_SHORT).show();

        // Close the keyboard
        commentInput.clearFocus();

        //Notify the adapter that the data has changed
        commentsRecycler.getAdapter().notifyItemInserted(post.getComments().size() - 1);
    }

    @Override
    public void addDataToView(FeedPost post, AbstractViewHolder holder) {

    }

    @Override
    public void getListOfPosts(List<FeedPost> posts) {

    }
}

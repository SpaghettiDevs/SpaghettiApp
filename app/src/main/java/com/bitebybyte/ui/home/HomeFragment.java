package com.bitebybyte.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;
import com.bitebybyte.backend.services.PostService;
import com.bitebybyte.backend.models.FeedPost;
import com.bitebybyte.ServiceablePostFragment;
import com.bitebybyte.holders.AbstractViewHolder;

import java.util.List;

/**
 * This class represents the Home Fragment, which displays the user's feed of posts
 * and allows the user to interact with the posts by liking them or clicking on them
 */
public class HomeFragment extends Fragment implements ServiceablePostFragment {

    private RecyclerView feed;
    private PostService postService;

    /**
     * Creates the view of the Home Fragment and sets up the recycler view and post service
     * @param inflater The layout inflater to inflate the fragment_home.xml
     * @param container The view group of the container that holds this fragment
     * @param savedInstanceState The saved instance state of the fragment
     * @return The view of the Home Fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Store the fragment_home.xml as a view
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Create a new Post Service to handle the posts
        postService = new PostService();

        // Find the recycler view for the feed and set it up
        feed = view.findViewById(R.id.home_feed);
        feed.setHasFixedSize(true);
        feed.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // Get all the posts and add them to the recycler view
        postService.getAllPosts(this);

        // Add item decoration to the recycler view to get consistent margins between items
        HomeItemDecoration decoration = new HomeItemDecoration(32, 1);
        feed.addItemDecoration(decoration);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void addDataToView(FeedPost post) {

    }

    @Override
    public void addDataToView(FeedPost post, AbstractViewHolder holder) {

    }

    @Override
    public void getListOfPosts(List<FeedPost> posts) {
        feed.setAdapter(new HomeFeedAdapter(posts));
    }

    /**
     * Show the search icon when this fragment gets loaded.
     *
     * @param menu The options menu as last shown or first initialized by
     *             onCreateOptionsMenu().
     */
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu)
    {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.app_bar_search).setVisible(true);
    }
}
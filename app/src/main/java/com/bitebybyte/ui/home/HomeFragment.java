package com.bitebybyte.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;
import com.bitebybyte.backend.database.PostService;
import com.bitebybyte.backend.local.FeedPost;
import com.bitebybyte.ui.ServicableFragment;

import java.util.List;

public class HomeFragment extends Fragment implements ServicableFragment {

    private RecyclerView feed;
    private PostService postService;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // store the fragment_home.xml as a view
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        postService = new PostService();

        feed = view.findViewById(R.id.home_feed);
        feed.setHasFixedSize(true);

        // display the posts linearly
        feed.setLayoutManager(new LinearLayoutManager(view.getContext()));

        postService.getAllPosts(this);
        
        // use ItemDecoration to get consistent margins inbetween items
        HomeItemDecoration decoration = new HomeItemDecoration(32, 1);
        feed.addItemDecoration(decoration);

        return view;
    }

    @Override
    public void addDataToView(FeedPost post) {

    }

    @Override
    public void getListOfPosts(List<FeedPost> posts) {
        feed.setAdapter(new HomeFeedAdapter(posts));
    }
}
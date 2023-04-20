package com.bitebybyte.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;
import com.bitebybyte.ServiceablePostFragment;
import com.bitebybyte.backend.models.FeedPost;
import com.bitebybyte.backend.services.PostService;
import com.bitebybyte.holders.AbstractViewHolder;
import com.bitebybyte.ui.home.ItemDecoration;

import java.util.List;

/**
 * Fragment that displays the search results from a search action.
 */
public class SearchFragment extends Fragment implements ServiceablePostFragment
{
    private RecyclerView searchResults;
    private PostService postService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        postService = new PostService();
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);

        searchResults = view.findViewById(R.id.search_results_recycler);
        searchResults.setHasFixedSize(true);
        searchResults.setLayoutManager(new LinearLayoutManager(view.getContext()));

        String query = SearchFragmentArgs.fromBundle(getArguments()).getQuery();
        postService.searchByTitle(query, this);

        searchResults.addItemDecoration(new ItemDecoration());

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
        searchResults.setAdapter(new SearchAdapter(posts));
    }
}

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
import com.bitebybyte.ui.home.ItemDecoration;

/**
 * Fragment that displays the search results from a search action.
 */
public class SearchFragment extends Fragment
{
    private RecyclerView searchResults;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);

        searchResults = view.findViewById(R.id.search_results_recycler);
        searchResults.setHasFixedSize(true);
        searchResults.setLayoutManager(new LinearLayoutManager(view.getContext()));

        searchResults.setAdapter(new SearchAdapter());

        searchResults.addItemDecoration(new ItemDecoration());

        return view;
    }
}

package com.bitebybyte.ui.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;
import com.bitebybyte.ui.home.HomeItemDecoration;

public class PostCommentsFragment extends Fragment {
    private RecyclerView commentsRecycler;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_comments, container, false);

        commentsRecycler = view.findViewById(R.id.post_comments_recycler);

        commentsRecycler.setHasFixedSize(true);

        //Display linearly
        commentsRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //Set the adapter
        commentsRecycler.setAdapter(new PostCommentsAdapter());

        // use ItemDecoration to get consistent margins inbetween items
        HomeItemDecoration decoration = new HomeItemDecoration(32, 1);
        commentsRecycler.addItemDecoration(decoration);

        return view;
    }
}

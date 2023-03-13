package com.bitebybyte.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;
import com.bitebybyte.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private RecyclerView feed;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        feed = view.findViewById(R.id.home_feed);
        feed.setHasFixedSize(true);
        feed.setLayoutManager(new LinearLayoutManager(view.getContext()));
        feed.setAdapter(new HomeFeedAdapter());

        HomeItemDecoration decoration = new HomeItemDecoration(32, 1);
        feed.addItemDecoration(decoration);

        return view;
    }
}
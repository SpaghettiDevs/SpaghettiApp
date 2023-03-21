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
import com.bitebybyte.backend.local.FeedPost;
import com.bitebybyte.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView feed;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // store the fragment_home.xml as a view
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        feed = view.findViewById(R.id.home_feed);
        feed.setHasFixedSize(true);

        // display the posts linearly
        feed.setLayoutManager(new LinearLayoutManager(view.getContext()));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<FeedPost> posts = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                try {
                                    posts.add(document.toObject(FeedPost.class));
                                    System.out.println("Successfully loaded document " + document.getId());
                                } catch (Exception e) {
                                    System.out.println("Failed to load document " + document.getId());
                                }
                            }
                            feed.setAdapter(new HomeFeedAdapter(posts));
                        } else {
                            System.out.println("Error getting documents: " + task.getException());
                        }
                    }
                });

        // use ItemDecoration to get consistent margins inbetween items
        HomeItemDecoration decoration = new HomeItemDecoration(32, 1);
        feed.addItemDecoration(decoration);

        return view;
    }
}
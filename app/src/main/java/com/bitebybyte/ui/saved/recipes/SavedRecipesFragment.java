package com.bitebybyte.ui.saved.recipes;

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

public class SavedRecipesFragment extends Fragment {

    private RecyclerView savedRecipes;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_recipes, container, false);

        savedRecipes = view.findViewById(R.id.saved_recipes_recycler);
        savedRecipes.setHasFixedSize(true);

        //Display linearly
        savedRecipes.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //Set the adapter
        savedRecipes.setAdapter(new SavedRecipesAdapter());

        // use ItemDecoration to get consistent margins inbetween items
        HomeItemDecoration decoration = new HomeItemDecoration(32, 1);
        savedRecipes.addItemDecoration(decoration);

        return view;
    }
}
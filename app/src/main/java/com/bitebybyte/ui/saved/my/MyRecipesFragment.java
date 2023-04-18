package com.bitebybyte.ui.saved.my;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;
import com.bitebybyte.backend.services.UserService;
import com.bitebybyte.ui.home.ItemDecoration;

public class MyRecipesFragment extends Fragment {

    private RecyclerView myRecipes;
    private UserService userService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_recipes, container, false);
        userService = new UserService();

        myRecipes = view.findViewById(R.id.my_recipes_recycler);
        myRecipes.setHasFixedSize(true);

        //Display linearly
        myRecipes.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //Set the adapter
        myRecipes.setAdapter(new MyRecipesAdapter(userService.getMyPosts()));

        // use ItemDecoration to get consistent margins inbetween items
        myRecipes.addItemDecoration(new ItemDecoration());

        return view;
    }
}
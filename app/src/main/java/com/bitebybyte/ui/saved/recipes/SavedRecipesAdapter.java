package com.bitebybyte.ui.saved.recipes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bitebybyte.ui.saved.SavedFragment;

public class SavedRecipesAdapter extends FragmentStateAdapter {

    public SavedRecipesAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int)
        Fragment fragment = new SavedRecipesFragment();
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putInt(SavedRecipesFragment.ARG_OBJECT, position + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 100;
    }
}

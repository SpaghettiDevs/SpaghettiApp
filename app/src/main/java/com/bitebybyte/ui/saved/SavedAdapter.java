package com.bitebybyte.ui.saved;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bitebybyte.ui.saved.recipes.SavedRecipesFragment;

public class SavedAdapter extends FragmentStateAdapter {
    public SavedAdapter(Fragment fragment) {
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
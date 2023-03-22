package com.bitebybyte.ui.saved;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bitebybyte.R;
import com.bitebybyte.ui.saved.my.MyRecipesFragment;
import com.bitebybyte.ui.saved.recipes.SavedRecipesFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class SavedFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    public SavedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        // Find the TabLayout and ViewPager in the layout
        tabLayout = view.findViewById(R.id.saved_tab_layout);
        viewPager = view.findViewById(R.id.saved_view_pager);

        // Set up the ViewPager with the sections adapter
        viewPager.setAdapter(new SectionsPagerAdapter(this));

        // Connect the TabLayout and ViewPager
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText(R.string.saved_recipes);
                    } else {
                        tab.setText(R.string.my_recipes);
                    }
                }
        ).attach();

        return view;
    }

    private static class SectionsPagerAdapter extends FragmentStateAdapter {

        public SectionsPagerAdapter(Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new SavedRecipesFragment();
                case 1:
                    return new MyRecipesFragment();
                default:
                    throw new IllegalArgumentException("Invalid position: " + position);
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

}
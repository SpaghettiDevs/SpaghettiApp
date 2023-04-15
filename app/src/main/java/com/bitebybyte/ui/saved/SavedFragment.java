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

    /**
     * Required empty public constructor
     */
    public SavedFragment() {}

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     *                  The fragment should not add the view itself, but this can be used to generate the
     *                  LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     * @return Return the View for the fragment's UI
     */
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
        connectTabLayoutAndViewPager();

        return view;
    }

    /**
     * Connects the TabLayout and ViewPager
     */
    private void connectTabLayoutAndViewPager() {
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText(R.string.saved_recipes);
                    } else {
                        tab.setText(R.string.my_recipes);
                    }
                }
        ).attach();
    }

    /**
     * The adapter to handle the ViewPager
     */
    private static class SectionsPagerAdapter extends FragmentStateAdapter {

        /**
         * Constructor of SectionsPagerAdapter
         * @param fragment The fragment object
         */
        public SectionsPagerAdapter(Fragment fragment) {
            super(fragment);
        }

        /**
         * Returns the fragment associated with a specified position.
         * @param position The position of the fragment to be returned
         * @return Fragment instance
         */
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

        /**
         * Get the number of items in the adapter
         * @return Number of items in the adapter
         */
        @Override
        public int getItemCount() {
            return 2;
        }
    }

}
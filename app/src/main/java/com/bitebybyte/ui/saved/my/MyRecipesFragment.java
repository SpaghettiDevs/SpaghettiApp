package com.bitebybyte.ui.saved.my;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bitebybyte.R;
import com.bitebybyte.databinding.FragmentCreateBinding;

public class MyRecipesFragment extends Fragment {
    private FragmentCreateBinding binding;
    public static final String ARG_OBJECT = "object";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Store the fragment_saved.xml as a view
        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
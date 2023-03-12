package com.bitebybyte.ui.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bitebybyte.databinding.FragmentCreateBinding;

public class CreateFragment extends Fragment {

    private FragmentCreateBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CreateViewModel createViewModel =
                new ViewModelProvider(this).get(CreateViewModel.class);

        binding = FragmentCreateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textDashboard;
//        createViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
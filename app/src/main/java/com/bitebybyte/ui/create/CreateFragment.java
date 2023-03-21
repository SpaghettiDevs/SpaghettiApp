package com.bitebybyte.ui.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bitebybyte.R;
import com.bitebybyte.backend.database.PostService;
import com.bitebybyte.databinding.FragmentCreateBinding;

public class CreateFragment extends Fragment {


    private FragmentCreateBinding binding;

    private EditText title;
    private EditText estimatedTime;
    private EditText description;
    private EditText ingredients;
    private EditText method;
    private Button submitButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        PostService service = new PostService();

        // Find the spinner in the UI
        Spinner spinner = binding.spinner;

        //Create an adapter to load the spinner items
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.time_steps_array, android.R.layout.simple_spinner_dropdown_item);

        //Apply it to the spinner.
        spinner.setAdapter(adapter);

        //Find the text-inputs
        title = root.findViewById(R.id.create_post_title_input);
        estimatedTime = root.findViewById(R.id.create_post_estimated_time_edit_text);
        description = root.findViewById(R.id.create_post_description);
        ingredients = root.findViewById(R.id.create_post_ingredients_input);
        method = root.findViewById(R.id.create_post_method);
        submitButton = root.findViewById(R.id.create_post_submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("title " + title.getText());
                System.out.println("estimated time " + estimatedTime.getText());
                System.out.println("description " + description.getText());
                System.out.println("method " + method.getText());
                System.out.println("Button Pressed! ");
                //TODO `idOwner` get from login
                //TODO ingredients is not a List. Should reconsider!
                service.createPostWithRecipe("1", description.getText().toString(), title.getText().toString(),
                        null, null,
                        method.getText().toString(), null, Integer.parseInt(estimatedTime.getText().toString()));
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
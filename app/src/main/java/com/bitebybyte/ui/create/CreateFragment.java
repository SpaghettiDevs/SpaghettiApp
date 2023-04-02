package com.bitebybyte.ui.create;

import static com.bitebybyte.CameraActivity.URI_ID_CODE;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bitebybyte.CameraActivity;
import com.bitebybyte.R;
import com.bitebybyte.backend.database.PostService;
import com.bitebybyte.backend.database.UserService;
import com.bitebybyte.databinding.FragmentCreateBinding;

public class CreateFragment extends Fragment {

    //const
    private static final int CAMERA_ACTIVITY_CODE = 333;

    private FragmentCreateBinding binding;

    private EditText title;
    private EditText estimatedTime;
    private EditText description;
    private EditText ingredients;
    private EditText method;
    private Button submitButton;
    private ImageButton imageButton;
    private Uri imageURI;
    private UserService userService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCreateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        PostService service = new PostService();
        userService = new UserService();

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
        imageButton = root.findViewById(R.id.create_post_image_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("title " + title.getText());
                System.out.println("estimated time " + estimatedTime.getText());
                System.out.println("description " + description.getText());
                System.out.println("method " + method.getText());
                System.out.println("Button Pressed! ");

                if (title.getText().toString().equals("Title") || title.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Filling in a title is required", Toast.LENGTH_SHORT).show();
                    title.setError("Filling in a title is required");
                    return;
                }

                if (description.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Filling in a description is required", Toast.LENGTH_SHORT).show();
                    description.setError("Filling in a description is required");
                    return;
                }

                if (ingredients.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Filling in ingredients is required", Toast.LENGTH_SHORT).show();
                    ingredients.setError("Filling in ingredients is required");
                    return;
                }

                if (method.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Filling in a method is required", Toast.LENGTH_SHORT).show();
                    method.setError("Filling in a method is required");
                    return;
                }

                if (imageURI == null) {
                    Toast.makeText(getContext(), "Please add an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(estimatedTime.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Filling in an estimated time is required", Toast.LENGTH_SHORT).show();
                    estimatedTime.setError("Filling in an estimated time is required");
                    return;
                }

                //creating a post
                String postID = service.createPostWithRecipe(userService.getUsername(), description.getText().toString(), title.getText().toString(),
                        null, null,
                        method.getText().toString(), ingredients.getText().toString(),
                        estimatedTime.getText().toString().equals("") ? -1 : Integer.parseInt(estimatedTime.getText().toString()));

                service.saveImageToDatabase(imageURI, imageButton, postID);



            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                startActivityForResult(intent, CAMERA_ACTIVITY_CODE);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });



        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                imageURI = Uri.parse(data.getStringExtra(URI_ID_CODE));
                imageButton.setImageURI(imageURI);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
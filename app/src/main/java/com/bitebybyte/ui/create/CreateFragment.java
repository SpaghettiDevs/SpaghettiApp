package com.bitebybyte.ui.create;

import static com.bitebybyte.CameraActivity.URI_ID_CODE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.content.Intent;
import android.app.Activity;
import android.widget.Toast;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bitebybyte.CameraActivity;
import com.bitebybyte.R;
import com.bitebybyte.backend.database.PostService;
import com.bitebybyte.databinding.FragmentCreateBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.net.Uri;

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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
        imageButton = root.findViewById(R.id.create_post_image_button);

        String idOwner = user.getUid();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("title " + title.getText());
                System.out.println("estimated time " + estimatedTime.getText());
                System.out.println("description " + description.getText());
                System.out.println("method " + method.getText());
                System.out.println("Button Pressed! ");

                service.saveImageToDatabase(imageURI, imageButton);

                service.createPostWithRecipe(idOwner, description.getText().toString(), title.getText().toString(),
                        imageURI == null ? null: imageURI.toString(), null,
                        method.getText().toString(), ingredients.getText().toString(),
                        estimatedTime.getText().toString().equals("") ? -1 : Integer.parseInt(estimatedTime.getText().toString()));
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
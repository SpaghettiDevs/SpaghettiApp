package com.bitebybyte.ui.create;

import static com.bitebybyte.CameraActivity.URI_ID_CODE;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bitebybyte.CameraActivity;
import com.bitebybyte.R;
import com.bitebybyte.backend.services.PostService;
import com.bitebybyte.backend.services.UserService;
import com.bitebybyte.databinding.FragmentCreateBinding;

public class CreateFragment extends Fragment {

    //const
    private static final int CAMERA_ACTIVITY_CODE = 333;

    private FragmentCreateBinding binding;
    private NavController navController;

    private EditText title;
    private EditText estimatedTime;
    private EditText description;
    private EditText ingredients;
    private EditText method;
    private Button submitButton;
    private ImageButton imageButton;
    private Uri imageURI;
    private UserService userService;
    private PostService postService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if(getActivity() == null)
            throw new RuntimeException("Activity is null");


        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);

        binding = FragmentCreateBinding.inflate(inflater, container, false);

        postService = new PostService();
        userService = new UserService();

        initializeUI();

        submitButton.setOnClickListener(this::onSubmitButtonClick);

        imageButton.setOnClickListener(this::onImageButtonClick);

        setHasOptionsMenu(true);


        return binding.getRoot();
    }

    private void initializeUI() {
        //Find the text-inputs
        title = binding.createPostTitleInput;
        estimatedTime = binding.createPostEstimatedTimeEditText;
        description = binding.createPostDescription;
        ingredients = binding.createPostIngredientsInput;
        method = binding.createPostMethod;
        submitButton = binding.createPostSubmitButton;
        imageButton = binding.createPostImageButton;

        // Find the spinner in the UI
        Spinner spinner = binding.spinner;

        //Create an adapter to load the spinner items
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.time_steps_array, android.R.layout.simple_spinner_dropdown_item);

        //Apply it to the spinner.
        spinner.setAdapter(adapter);
    }

    /**
     * Click listener for the submit button.
     * @param v the view that was clicked.
     */
    private void onSubmitButtonClick(View v) {
        if(!inputIsValid())
            return;

        //Get the spinner from the UI bind.
        Spinner spinner = binding.spinner;

        //Create a post and get the PostID
        String postID = postService.createPostWithRecipe(userService.getCurrentUserId(), description.getText().toString(), title.getText().toString(),
                null, null,
                method.getText().toString(), ingredients.getText().toString(),
                Integer.parseInt(estimatedTime.getText().toString()),
                spinner.getSelectedItem().toString());

        //Save the image to the database
        postService.saveImageToDatabase(imageURI, imageButton, postID, "images/");

        Toast.makeText(getContext(), "Post created successfully", Toast.LENGTH_SHORT).show();

        //Clear the fields
        clearFields();

        //Navigate back to feed (home) page after 500ms.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navController.navigate(R.id.navigation_home);
            }
        }, 500);
    }

    /**
     * Click listener for the image button.
     * @param v the view that was clicked.
     */
    private void onImageButtonClick(View v) {
        //Start the camera activity
        Intent intent = new Intent(getActivity(), CameraActivity.class);

        //Start the activity and wait for a result.
        startActivityForResult(intent, CAMERA_ACTIVITY_CODE);

        //Disable the animation
        getActivity().overridePendingTransition(0, 0);
    }

    /**
     * Checks if the input is valid.
     * Rules:
     * - Title is required
     * - Description is required
     * - Ingredients is required
     * - Method is required
     * - Image is required
     * - Estimated time is required
     * @return true if input is valid, false otherwise.
     */
    private boolean inputIsValid() {
        if (title.getText().toString().equals("Title") || title.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Filling in a title is required", Toast.LENGTH_SHORT).show();
            title.setError("Filling in a title is required");
            return false;
        }

        if (description.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Filling in a description is required", Toast.LENGTH_SHORT).show();
            description.setError("Filling in a description is required");
            return false;
        }

        if (ingredients.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Filling in ingredients is required", Toast.LENGTH_SHORT).show();
            ingredients.setError("Filling in ingredients is required");
            return false;
        }

        if (method.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Filling in a method is required", Toast.LENGTH_SHORT).show();
            method.setError("Filling in a method is required");
            return false;
        }

        if (imageURI == null) {
            Toast.makeText(getContext(), "Please add an image", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(estimatedTime.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Filling in an estimated time is required", Toast.LENGTH_SHORT).show();
            estimatedTime.setError("Filling in an estimated time is required");
            return false;
        }

        return true;
    }

    /**
     * Clears all the fields.
     */
    private void clearFields() {
        title.setText("Title");
        estimatedTime.setText("");
        description.setText("");
        ingredients.setText("");
        method.setText("");
        imageURI = null;
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with,
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
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

    /**
     * Called when the fragment is no longer in use. This is called after onStop() and before
     * onDetach().
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Hide the search icon when this fragment gets loaded.
     *
     * @param menu The options menu as last shown or first initialized by
     *             onCreateOptionsMenu().
     */
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu)
    {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.app_bar_search).setVisible(false);
    }

}
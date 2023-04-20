package com.bitebybyte;


import static android.app.PendingIntent.getActivity;

import static com.bitebybyte.CameraActivity.URI_ID_CODE;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;

import com.bitebybyte.backend.services.PostService;
import com.bitebybyte.backend.services.UserService;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {

    private static final int CAMERA_ACTIVITY_CODE = 101;

    private FirebaseAuth auth;

    private EditText emailText;
    private EditText usernameText;
    private Button usernameButton;
    private Button emailButton;
    private Button passwordButton;
    private ImageButton profilePictureButton;
    private Button deleteAccountButton;

    private FirebaseUser user;
    private UserService userService;
    private PostService postService;
    private String currentEmail;
    private Uri imageURI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        postService = new PostService();
        userService = new UserService();

        setupUI();
        fillInUI();
        setupListeners();
    }

    /**
     * Fills in the UI with the current user's information
     * If the user is null, the activity is finished.
     */
    private void fillInUI () {
        if (user != null) {
            ((TextView) findViewById(R.id.textViewEmail)).setText(user.getEmail());
            ((TextView) findViewById(R.id.textViewUsername)).setText(userService.getCurrentUsername());
            currentEmail = user.getEmail();
            postService.loadImage(profilePictureButton, userService.getCurrentUserId(), "pfPictures/");
        } else {
            Toast.makeText(getApplicationContext(), "Error: current user can not be loaded", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    /**
     * Sets up the UI
     */
    private void setupUI() {
        emailText = findViewById(R.id.change_email_text);
        emailButton = findViewById(R.id.change_email_button);
        usernameText = findViewById(R.id.change_username_text);
        usernameButton = findViewById(R.id.change_username_button);
        passwordButton = findViewById(R.id.reset_password);
        profilePictureButton = findViewById(R.id.profile_picture_button);
        deleteAccountButton = findViewById(R.id.delete_account);
    }

    /**
     * Sets up the listeners
     */
    private void setupListeners() {
        usernameButton.setOnClickListener(this::onUsernameButtonClicked);
        emailButton.setOnClickListener(this::onEmailButtonClicked);
        passwordButton.setOnClickListener(this::onPasswordButtonClicked);
        profilePictureButton.setOnClickListener(this::onProfilePictureButtonClicked);
        deleteAccountButton.setOnClickListener(this::onDeleteButtonClicked);
    }

    /**
     * On click handler for the change username button
     * Changes the username of the user
     * Signs the user out and redirects to the main activity.
     * @param v the view that was clicked
     */
    private void onUsernameButtonClicked(View v) {
        String username = usernameText.getText().toString().trim();

        if(!usernameIsValid(username)) {
            return;
        }

        try {
            userService.changeUsername(user.getUid(), username);
            TextView usernameText = findViewById(R.id.textViewUsername);

            usernameText.setText(userService.getCurrentUsername());

            // Sign out the user and redirect to the main activity
            auth.signOut();
            startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            finish();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks if the username is valid
     * Rules:
     * - Username must be between 1 and 16 characters long
     * - Username must not be empty
     * @param username the username to check
     * @return true if the username is valid, false otherwise
     */
    private boolean usernameIsValid(String username) {
        if (username.isEmpty()) {
            usernameText.setError("Username cannot be empty");
            return false;
        }

        if(username.length() > 16) {
            usernameText.setError("Username must be in between 1 and 16 characters long");
            return false;
        }

        return true;
    }


    /**
     * On click handler for the change email button
     * Changes the email of the user
     * Sends a verification email to the new email
     * Signs the user out and redirects to the main activity
     *
     * @param v the view that was clicked
     */
    private void onEmailButtonClicked(View v) {
        String email = emailText.getText().toString().trim();

        if (!emailIsValid(email)) {
            return;
        }

        user.updateEmail(email).addOnCompleteListener(completedTask -> {
            if (completedTask.isSuccessful()) {
                user.sendEmailVerification();
                auth.signOut();
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    /**
     * Checks if the email is valid
     * Rules:
     * - Email must be between 1 and 32 characters long
     * - Email must not be empty
     * - Email must contain an @
     * - Email must contain a .
     * @param email the email to check
     * @return true if the email is valid, false otherwise
     */
    private boolean emailIsValid(String email) {
        if (email.isEmpty()) {
            emailText.setError("Email cannot be empty");
            return false;
        }

        if (!email.contains("@")) {
            emailText.setError("Email must contain an @");
            return false;
        }

        if (!email.contains(".")) {
            emailText.setError("Email must contain a .");
            return false;
        }

        return true;
    }

    /**
     * On click handler for the reset password button
     * Sends a password reset email to the user
     * Signs the user out and redirects to the main activity
     *
     * @param v the view that was clicked
     */
    private void onPasswordButtonClicked(View v) {
        auth.sendPasswordResetEmail(currentEmail).addOnCompleteListener(completedTask -> {
            if (completedTask.isSuccessful()) {
                auth.signOut();
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    /**
     * On click handler for changing the users profile picture
     * Opens the camera activity, (on completion the photo will be send to the backend)
     *
     * @param v the view that was clicked
     */
    private void onProfilePictureButtonClicked(View v) {
        //Start the camera activity
        Intent intent = new Intent(this, CameraActivity.class);

        //Start the activity and wait for a result.
        startActivityForResult(intent, CAMERA_ACTIVITY_CODE);

        //Disable the animation
        this.overridePendingTransition(0, 0);
    }

    /**
     * On click handler for the delete account button
     * Deletes the user from the database and the authentication
     * Signs the user out and redirects to the main activity
     *
     * @param v the view that was clicked
     */
    private void onDeleteButtonClicked(View v) {
        userService.deleteUser(user.getUid());
        user.delete().addOnCompleteListener(completedTask -> {
            if (completedTask.isSuccessful()) {
                auth.signOut();
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                finish();
            }
        });
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
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                imageURI = Uri.parse(data.getStringExtra(URI_ID_CODE));

                String userID = userService.getCurrentUserId();

                //Save the image to the database
                postService.saveImageToDatabase(imageURI, profilePictureButton, userID, "pfPictures/");
                auth.signOut();
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                finish();
            }
        }
    }
}

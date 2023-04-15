package com.bitebybyte;


import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bitebybyte.backend.database.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private EditText emailText;
    private EditText usernameText;
    private Button usernameButton;
    private Button emailButton;
    private Button passwordButton;
    private Button deleteAccountButton;

    private FirebaseUser user;
    private UserService userService;
    private String currentEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
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
        deleteAccountButton = findViewById(R.id.delete_account);
    }

    /**
     * Sets up the listeners
     */
    private void setupListeners() {
        usernameButton.setOnClickListener(this::onUsernameButtonClicked);
        emailButton.setOnClickListener(this::onEmailButtonClicked);
        passwordButton.setOnClickListener(this::onPasswordButtonClicked);
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

        userService.changeUsername(user.getUid(), username);
        ((TextView) findViewById(R.id.textViewUsername)).setText(userService.getCurrentUsername());
        auth.signOut();
        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        finish();
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
}

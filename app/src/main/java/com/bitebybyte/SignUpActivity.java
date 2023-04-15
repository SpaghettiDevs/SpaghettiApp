package com.bitebybyte;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bitebybyte.backend.database.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * The SignUpActivity class represents the screen where the user can sign up for a new account.
 */
public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword, signupUsername;
    private Button signupButton;
    private TextView loginRedirectText;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        //Assign the fields to the correct views
        assignFields();

        //Get the authentication instance
        auth = FirebaseAuth.getInstance();

        //Get the user service instance
        userService = new UserService();

        //Create a new on click listener for the signup button
        signupButton.setOnClickListener(this::onSignUpButtonClick);

        //Create a new on click listener for the login redirect text
        loginRedirectText.setOnClickListener(this::onLoginRedirectTextClick);
    }

    /**
     * Assigns the fields to the correct views
     */
    private void assignFields() {
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupUsername = findViewById(R.id.signup_username);
    }

    /**
     * Handles the click event of the sign up button.
     * @param view The view that was clicked.
     */
    private void onSignUpButtonClick(View view) {
        String email = signupEmail.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();
        String username = signupUsername.getText().toString().trim();



        //check if everything is filled in correctly.
        if (username.isEmpty()) {
            signupUsername.setError("Username cannot be empty");
            return;
        }

        if (email.isEmpty()) {
            signupEmail.setError("Email cannot be empty");
            return;
        }

        if (password.isEmpty()) {
            signupPassword.setError("Password cannot be empty");
            return;
        }

        if(!userService.checkIfUsernameInUse(username)) {
            signupUsername.setError("Username already exists");
            return;
        }

        if(username.length() > 16) {
            signupUsername.setError("Username is longer then 16 characters");
            return;
        }

        createUserWithEmailAndPassword(email, password, username);
    }

    /**
     * Handles the click event of the login redirect text.
     * @param view The view that was clicked.
     */
    private void onLoginRedirectTextClick(View view) {
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
    }

    /**
     * Creates a new user in FirebaseAuth for authentication.
     * @param email The email of the user.
     * @param password The password of the user.
     * @param username The username of the user.
     */
    private void createUserWithEmailAndPassword(String email, String password, String username) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {

                String errorMessage = task.getException().getMessage();

                Toast.makeText(SignUpActivity.this, "SignUp Failed" + errorMessage, Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, password);
            FirebaseUser user = auth.getCurrentUser();

            if (user == null) {
                Toast.makeText(SignUpActivity.this, "Could not get user, because it was null.", Toast.LENGTH_SHORT).show();
                return;
            }

            sendEmailVerification(user, username);
        });
    }

    /**
     * Sends a verification email to the user.
     * @param user The user to send the email to.
     * @param username The username of the user.
     */
    private void sendEmailVerification(@NonNull FirebaseUser user, String username) {
        user.sendEmailVerification().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                auth.signOut();
                Toast.makeText(SignUpActivity.this, "Could not send verification email, please check your filled in email", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(SignUpActivity.this, "SignUp Successful, please verify your email", Toast.LENGTH_LONG).show();

            //Save the newly created user to the database
            userService.saveNewUserToDb(username, auth.getUid());
            auth.signOut();
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class)); //go to login when signup successful
            finish();
        });
    }

}
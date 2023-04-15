package com.bitebybyte;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * This activity allows the user to login to the app using their email and password.
 */
public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText loginEmail, loginPassword;
    private Button loginButton;
    private TextView signupRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        auth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signUpRedirectText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                if (validateEmailAndPassword(email, password)) {
                    loginWithEmailAndPassword(email, password);
                }
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToSignUp();
            }
        });

    }

    /**
     * Validates the email and password fields.
     *
     * @param email    the email address entered by the user.
     * @param password the password entered by the user.
     * @return true if the email and password fields are filled in correctly, false otherwise.
     */
    private boolean validateEmailAndPassword(String email, String password) {
        if (email.isEmpty()) {
            loginEmail.setError("Email cannot be empty");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError("Please enter a valid email");
            return false;
        }

        if (password.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        }

        return true;
    }

    /**
     * Attempts to login to the app using the email and password entered by the user.
     *
     * @param email    the email address entered by the user.
     * @param password the password entered by the user.
     */
    private void loginWithEmailAndPassword(String email, String password) {

        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                FirebaseUser user = auth.getCurrentUser();

                // If the user is null, we can't continue. Show a toast and return.
                if (user == null) {
                    Toast.makeText(LoginActivity.this, "Login Failed: User is null", Toast.LENGTH_SHORT).show();
                    return;
                }

                // If the user's email is not verified, we can't continue. Show a toast and return.
                if (!auth.getCurrentUser().isEmailVerified()) {
                    auth.signOut();
                    Toast.makeText(LoginActivity.this, "Please verify your email, make sure to check your spam folder.", Toast.LENGTH_LONG).show();
                    return;
                }

                // If we get here, the user is logged in and their email is verified. Show a toast and redirect to the main activity.
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();

            }).addOnFailureListener(failureResult -> {
                Toast.makeText(LoginActivity.this, "Login Failed: " + failureResult.getMessage(), Toast.LENGTH_LONG).show();
            });
    }

    /**
     * Redirects the user to the sign up screen.
     */
    private void redirectToSignUp() {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }

}
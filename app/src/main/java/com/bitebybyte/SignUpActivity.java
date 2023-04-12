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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupUsername = findViewById(R.id.signup_username);

        //sign up button is clicked
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = signupEmail.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();
                String username = signupUsername.getText().toString().trim();
                userService = new UserService();

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

                if(!userService.usernameCheck(username)) {
                    signupUsername.setError("Username already exists");
                    return;
                }

                if(username.length() > 16) {
                    signupUsername.setError("Username is longer then 16 characters");
                    return;
                }

                //create a new user in FirebaseAuth for authentication.
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        auth.signInWithEmailAndPassword(email, password);
                        FirebaseUser user = auth.getCurrentUser();
                        //send verification email
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    auth.signOut();
                                    Toast.makeText(SignUpActivity.this, "Could not send verification email, please check your filled in email", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                Toast.makeText(SignUpActivity.this, "SignUp Successful, please verify your email", Toast.LENGTH_LONG).show();
                                userService.saveNewUserToDb(username, auth.getUid()); //save the newly created user to the database
                                auth.signOut();
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class)); //go to login when signup successful
                                finish();

                            }
                        });
                    }
                });
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }
}
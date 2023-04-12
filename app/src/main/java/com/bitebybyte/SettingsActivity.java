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

        if (user != null) {
            ((TextView)findViewById(R.id.textViewEmail)).setText(user.getEmail());
            ((TextView)findViewById(R.id.textViewUsername)).setText(userService.getCurrentUsername());
            currentEmail = user.getEmail();
        } else {
            Toast.makeText(getApplicationContext(), "Error: current user can not be loaded", Toast.LENGTH_SHORT).show();
            finish();
        }


        emailText = findViewById(R.id.change_email_text);
        emailButton = findViewById(R.id.change_email_button);
        usernameText = findViewById(R.id.change_username_text);
        usernameButton = findViewById(R.id.change_username_button);
        passwordButton = findViewById(R.id.reset_password);
        deleteAccountButton = findViewById(R.id.delete_account);

        usernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameText.getText().toString().trim();

                if (!username.isEmpty() && username.length() <= 16) {
                    userService.changeUsername(user.getUid(), username);
                    ((TextView)findViewById(R.id.textViewUsername)).setText(userService.getCurrentUsername());
                    auth.signOut();
                    startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                    finish();
                } else {
                    usernameText.setError("Username length min = 1 and max = 16");
                }
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString().trim();

                if(!email.isEmpty()) {
                    user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                user.sendEmailVerification();
                                auth.signOut();
                                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    });
                } else {
                    emailText.setError("Email cannot be empty");
                    return;
                }
            }
        });

        passwordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.sendPasswordResetEmail(currentEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            auth.signOut();
                            startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userService.deleteUser(user.getUid());
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            auth.signOut();
                            startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
            }
        });
    }
}

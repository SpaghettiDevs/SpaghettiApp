package com.bitebybyte;


import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.jakewharton.processphoenix.ProcessPhoenix;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private EditText emailText;
    private Button emailButton;
    private Button passwordButton;
    private Button deleteAccountButton;

    private FirebaseUser user;

    private String currentEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            ((TextView)findViewById(R.id.textViewEmail)).setText(user.getEmail());
            ((TextView)findViewById(R.id.textViewUsername)).setText(user.getDisplayName());
            currentEmail = user.getEmail();
        } else {
            Toast.makeText(getApplicationContext(), "Error: current user can not be loaded", Toast.LENGTH_SHORT).show();
            finish();
        }


        emailText = findViewById(R.id.change_email_text);
        emailButton = findViewById(R.id.change_email_button);
        passwordButton = findViewById(R.id.reset_password);
        deleteAccountButton = findViewById(R.id.delete_account);

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString().trim();

                if(!email.isEmpty()) {
                    user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                currentEmail = user.getEmail();
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

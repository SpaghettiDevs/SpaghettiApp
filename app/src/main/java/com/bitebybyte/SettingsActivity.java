package com.bitebybyte;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private EditText emailText;
    private EditText usernameText;
    private EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        auth = FirebaseAuth.getInstance();

        emailText = findViewById(R.id.change_email);
        usernameText = findViewById(R.id.change_username);
        passwordText = findViewById(R.id.change_password);
    }
}

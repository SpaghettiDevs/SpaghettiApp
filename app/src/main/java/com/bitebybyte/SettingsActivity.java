package com.bitebybyte;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import com.jakewharton.processphoenix.ProcessPhoenix;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private EditText emailText;
    private EditText usernameText;
    private EditText passwordText;

    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        auth = FirebaseAuth.getInstance();

        emailText = findViewById(R.id.change_email);
        usernameText = findViewById(R.id.change_username);
        passwordText = findViewById(R.id.change_password);
        confirmButton = findViewById(R.id.confirm_changes_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString().trim();
                String username = usernameText.getText().toString().trim();
                String password = passwordText.getText().toString().trim();

                // custom dialog
                final Dialog dialog = new Dialog(getApplicationContext());
                dialog.setContentView(R.layout.dialog_authenticate);
                dialog.setTitle("Authenticate to confirm changes");

                /*EditText emailAuth = (EditText) dialog.findViewById(R.id.email);
                EditText passwordAuth = (EditText) dialog.findViewById(R.id.password);*/
                dialog.show();

                if(!email.isEmpty()) {

                }

                if(!username.isEmpty()) {

                }

                if(!password.isEmpty()) {

                }

                //Restart application
                ProcessPhoenix.triggerRebirth(getApplicationContext());
            }
        });
    }
}

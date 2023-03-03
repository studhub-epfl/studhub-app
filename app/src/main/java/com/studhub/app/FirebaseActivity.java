package com.studhub.app;

import static com.studhub.app.Globals.DATABASE;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FirebaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);
    }

    /**
     * Handles `get` button click
     * Retrieves email and phone from firebase db and displays the information in the corresponding fields
     * @param view event target
     */
    public void handleGetClick(View view) {
        TextView emailInput = findViewById(R.id.firebase_input_email);
        TextView phoneInput = findViewById(R.id.firebase_input_phone);

        DATABASE.get("/users/user1/email", String.class).thenAccept(emailInput::setText);
        DATABASE.get("/users/user1/phone", String.class).thenAccept(phoneInput::setText);
    }

    /**
     * Handles `set` button
     * Get email and phone from the corresponding fields and push the values to the firebase db
     * @param view event target
     */
    public void handleSetClick(View view) {
        TextView emailInput = findViewById(R.id.firebase_input_email);
        TextView phoneInput = findViewById(R.id.firebase_input_phone);

        String emailValue = emailInput.getText().toString();
        String phoneValue = phoneInput.getText().toString();

        DATABASE.set("/users/user1/email", emailValue);
        DATABASE.set("/users/user1/phone", phoneValue);
    }
}
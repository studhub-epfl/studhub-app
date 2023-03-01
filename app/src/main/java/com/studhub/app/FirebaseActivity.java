package com.studhub.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.studhub.app.database.Database;
import com.studhub.app.database.FireDatabase;

import java.util.concurrent.CompletableFuture;

public class FirebaseActivity extends AppCompatActivity {
    private final Database db = new FireDatabase();

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

        db.get("/users/user1/email", String.class).thenAccept(emailInput::setText);
        db.get("/users/user1/phone", String.class).thenAccept(phoneInput::setText);
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

        db.set("/users/user1/email", emailValue);
        db.set("/users/user1/phone", phoneValue);
    }
}
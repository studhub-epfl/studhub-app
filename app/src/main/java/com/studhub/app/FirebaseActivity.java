package com.studhub.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.CompletableFuture;

public class FirebaseActivity extends AppCompatActivity {
    private final DatabaseReference db = FirebaseDatabase.getInstance().getReference();

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

        // CompletableFuture<String> emailFuture = new CompletableFuture<>();
        // CompletableFuture<String> phoneFuture = new CompletableFuture<>();

        db.child("/users/user1/email").get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.getValue() == null) {
                        emailInput.setText("");
                    } else {
                        emailInput.setText(snapshot.getValue(String.class));
                    }
                    })
                .addOnFailureListener(error -> emailInput.setText(""));

        db.child("/users/user1/phone").get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.getValue() == null) {
                        phoneInput.setText("");
                    } else {
                        phoneInput.setText(snapshot.getValue(String.class));
                    }})
                .addOnFailureListener(error -> phoneInput.setText(""));
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

        db.child("/users/user1/email").setValue(emailValue);
        db.child("/users/user1/phone").setValue(phoneValue);
    }
}
package com.studhub.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class GreetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greet);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String message = String.format("Hello my dear %s, how is you?", name);
        ((TextView) findViewById(R.id.greetingMessage)).setText(message);
    }
}
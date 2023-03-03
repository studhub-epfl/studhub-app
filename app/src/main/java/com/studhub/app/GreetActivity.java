package com.studhub.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class GreetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greet);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String message = String.format("Hello my dear %s, how are you?", name);
        ((TextView) findViewById(R.id.greetingMessage)).setText(message);


    }

    public void handleContinue(View view) {
        Intent intent = new Intent(this, NavigationDrawer.class);
        this.startActivity(intent);
    }
}
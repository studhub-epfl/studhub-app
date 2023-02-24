package com.studhub.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Handles submission of the name form
     * Retrieve input value and send it to the greeting view
     * @param view
     */
    public void handleNameSubmit(View view) {
        Intent intent = new Intent(this, GreetActivity.class);
        EditText nameField = findViewById(R.id.mainName);
        intent.putExtra("name", nameField.getText().toString());
        this.startActivity(intent);
    }
}
package com.studhub.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainActivityContent()
            }
        }
    }

    private fun handleSubmit(name: String) {
        val intent = Intent(this, GreetActivity2::class.java)
        intent.putExtra("name", name)
        startActivity(intent)
    }

    private fun showMap() {
        val intent = Intent(this, MapsActivity2::class.java)
        startActivity(intent)
    }

    @Preview(showBackground = true)
    @Composable
    fun MainActivityContent() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Type in your name",
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.padding(10.dp))
            val nameState = remember { mutableStateOf("") }
            TextField(
                value = nameState.value,
                onValueChange = { value -> nameState.value = value },
                label = { Text(text = "Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { handleSubmit(nameState.value) },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Submit")
            }
            Spacer(modifier = Modifier.padding(3.dp))
            Button(
                onClick = { showMap() },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Maps")
            }
        }
    }
}
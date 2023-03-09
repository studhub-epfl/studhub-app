package com.studhub.app

import android.os.Bundle
import androidx.compose.material.Surface
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.studhub.app.ui.theme.StudHubTheme


class GreetActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudHubTheme {
                // A surface container using the 'background' color from the theme
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Surface(
                        color = MaterialTheme.colors.background
                    ) {
                        val myIntent = intent
                        GreetContent(myIntent.getStringExtra("name").toString())
                    }
                }
            }
        }
    }
}

@Composable
fun GreetContent(name: String?) {
        Text(modifier = Modifier.testTag("greetingMessage"),
            text = "Hello my dear $name, how are you?",
            color = MaterialTheme.colors.primary
        )
}
package com.studhub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.studhub.app.ui.theme.Kotlin_bootcampTheme

class GreetingActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Kotlin_bootcampTheme {
                // A surface container using the 'background' color from the theme
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Surface(
                        color = MaterialTheme.colors.background
                    ) {
                        val myIntent = intent
                        Greeting(myIntent.getStringExtra("name").toString())
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(
        modifier = Modifier.testTag("greetingMessage"),
        text = "Hello $name!",
        color = MaterialTheme.colors.secondary
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    Kotlin_bootcampTheme {
        Greeting("Android")
    }
}
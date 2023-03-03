package com.studhub.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.studhub.app.ui.theme.Kotlin_bootcampTheme

class MainActivity2 : ComponentActivity() {
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
                        Column {
                            Text("Enter your name: ")
                            Spacer(modifier = Modifier.height(2.dp))
                            val txtState = remember { TextFieldState() }
                            TextInput(txtState)
                            Spacer(modifier = Modifier.height(2.dp))
                            MainGoButton(value = txtState.text)
                        }
                    }
                }
            }
        }
    }
}

class TextFieldState {
    var text: String by mutableStateOf("")
}

fun handleGoButtonClick(context: Context, value: String) {
    val myIntent = Intent(context, GreetingActivity2::class.java)
    myIntent.putExtra("name", value)
    context.startActivity(myIntent)
}

@Composable
fun TextInput(state: TextFieldState) {
    OutlinedTextField(
        modifier = Modifier.testTag("textInput"),
        value = state.text,
        onValueChange = { newVal ->
            state.text = newVal
        },
        label = {
            Text("Name")
        },
        placeholder = {
            Text("Enter your name")
        }
    )
}

@Composable
fun MainGoButton(value: String) {
    val context = LocalContext.current
    Button(
        modifier = Modifier.testTag("mainGoButton"),
        onClick = {
            handleGoButtonClick(context, value)
        }
    ) {
        Text("Submit")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Kotlin_bootcampTheme {
        // A surface container using the 'background' color from the theme
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(
                color = MaterialTheme.colors.background
            ) {
                Column {
                    Text("Enter your name: ")
                    Spacer(modifier = Modifier.height(2.dp))
                    val txtState = remember { TextFieldState() }
                    TextInput(txtState)
                    Spacer(modifier = Modifier.height(2.dp))
                    MainGoButton(value = txtState.text)
                }
            }
        }
    }
}
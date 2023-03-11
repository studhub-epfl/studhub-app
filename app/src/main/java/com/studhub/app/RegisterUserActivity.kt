package com.studhub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.studhub.app.presentation.ui.*
import com.studhub.app.ui.theme.BootcampTheme

class RegisterUserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BootcampTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UserForm()
                }
            }
        }
    }
}

fun submit(
    firstName: String,
    lastName: String,
    userName: String,
    email: String,
    phoneNumber: String,
    profilePic: String
) {
    /*TODO*/
}

@Composable
fun UserForm() {
    val firstName = rememberSaveable { mutableStateOf("") }
    val lastName = rememberSaveable { mutableStateOf("") }
    val userName = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val phoneNumber = rememberSaveable { mutableStateOf("") }
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BigLabel("Create Profile")
            BasicTextField("Firstname", firstName)
            BasicTextField("Lastname", lastName)
            BasicTextField("Username", userName)
            EmailTextField("Email", email)
            NumericTextField("Phone number", phoneNumber)
            AddFileButton("Add profile picture")
            Spacer(Modifier.size(10.dp))
            BasicFilledButton(
                onClickHandler = {
                    submit(
                        firstName.value,
                        lastName.value,
                        userName.value,
                        email.value,
                        phoneNumber.value,
                        //TODO implement actual profile picture saving
                        "pf_placeholder.png"
                    )
                },
                label = "Submit"
            )
        }
    }
}

@Composable
fun AddFileButton(label: String) {
    OutlinedButton(
        onClick = { /*TODO*/ },
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
        modifier = Modifier.padding(top = 3.dp, bottom = 3.dp)
    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = "Plus Icon",
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(label)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BootcampTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            UserForm()
        }
    }
}
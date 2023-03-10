package com.studhub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studhub.app.domain.repository.UserRepository
import com.studhub.app.domain.usecase.user.CreateUser
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
    Column{
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTextField(
    label: String,
    rememberedValue: MutableState<String> = rememberSaveable { mutableStateOf("") }
) {
    OutlinedTextField(
        value = rememberedValue.value,
        onValueChange = { rememberedValue.value = it },
        label = { Text(label) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailTextField(
    label: String,
    rememberedValue: MutableState<String> = rememberSaveable { mutableStateOf("") }
) {
    OutlinedTextField(
        value = rememberedValue.value,
        onValueChange = { rememberedValue.value = it },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumericTextField(
    label: String,
    rememberedValue: MutableState<String> = rememberSaveable { mutableStateOf("") }
) {
    OutlinedTextField(
        value = rememberedValue.value,
        onValueChange = { rememberedValue.value = it },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
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

@Composable
fun BigLabel(label: String) {
    Text(
        text = label,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun BasicFilledButton(onClickHandler: () -> Unit, label: String) {
    Button(
        onClick = { onClickHandler() },
        modifier = Modifier.padding(top = 3.dp, bottom = 3.dp)
    ) {
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
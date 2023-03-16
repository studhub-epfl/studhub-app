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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.domain.repository.UserRepository
import com.studhub.app.domain.usecase.user.CreateUser
import com.studhub.app.presentation.ui.*
import com.studhub.app.ui.theme.StudHubTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class RegisterUserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudHubTheme {
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

private val repository: UserRepository = object : UserRepository {
    private val userDB = HashMap<String, User>()

    override suspend fun createUser(user: User): Flow<ApiResponse<User>> {
        return flow {
            emit(ApiResponse.Loading)
            delay(1000)
            userDB[user.id] = user
            emit(ApiResponse.Success(user))
        }
    }

    override suspend fun getUser(userId: String): Flow<ApiResponse<User>> {
        return flow {
            // empty implementation
            emit(ApiResponse.Success(User()))
        }
    }

    override suspend fun updateUser(userId: String, updatedUser: User): Flow<ApiResponse<User>> {
        return flow {
            // empty implementation
            emit(ApiResponse.Success(User()))
        }
    }

    override suspend fun removeUser(userId: String): Flow<ApiResponse<Boolean>> {
        return flow {
            // empty implementation
            emit(ApiResponse.Success(true))
        }
    }

}

suspend fun submit(user: User) {
    val createUser = CreateUser(repository)
    createUser(user)
}

@Composable
fun UserForm() {
    val firstName = rememberSaveable { mutableStateOf("") }
    val lastName = rememberSaveable { mutableStateOf("") }
    val userName = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val phoneNumber = rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
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
                onClick = {
                    scope.launch {
                        submit(
                            User(
                                id = "1",
                                email = email.value,
                                phoneNumber = phoneNumber.value,
                                firstName = firstName.value,
                                lastName = lastName.value,
                                userName = userName.value,
                                profilePicture = "pf_placeholder.png"
                            )
                        )
                    }
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
    RegisterUserActivity()
}

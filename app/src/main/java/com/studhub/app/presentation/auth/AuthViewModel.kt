package com.studhub.app.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository,
    val oneTapClient: SignInClient
) : ViewModel() {
    val isUserAuthenticated get() = repo.isUserAuthenticatedInFirebase

    var oneTapSignInResponse by mutableStateOf<ApiResponse<BeginSignInResult>>(ApiResponse.Loading)
        private set
    var signInWithGoogleResponse by mutableStateOf<ApiResponse<Boolean>>(ApiResponse.Loading)
        private set

    fun oneTapSignIn() = viewModelScope.launch {
        oneTapSignInResponse = ApiResponse.Loading
        repo.oneTapSignInWithGoogle().collect {
            when (it) {
                is ApiResponse.Success -> {
                    oneTapSignInResponse = it
                }
                else -> {}
            }
        }
    }

    fun signInWithGoogle(googleCredential: AuthCredential) = viewModelScope.launch {
        oneTapSignInResponse = ApiResponse.Loading
        repo.firebaseSignInWithGoogle(googleCredential).collect {
            when (it) {
                is ApiResponse.Success -> {
                    signInWithGoogleResponse = it
                }
                else -> {}
            }
        }
    }
}

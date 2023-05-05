package com.studhub.app.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository,
) : ViewModel() {
    val isUserAuthenticated get() = repo.isUserAuthenticatedInFirebase

    var signInResponse by mutableStateOf<ApiResponse<Boolean>>(ApiResponse.Loading)
        private set

    var signUpResponse by mutableStateOf<ApiResponse<Boolean>>(ApiResponse.Loading)
        private set

    var sendEmailVerificationResponse by mutableStateOf<ApiResponse<Boolean>>(ApiResponse.Loading)
        private set

    var sendPasswordResetEmailResponse by mutableStateOf<ApiResponse<Boolean>>(ApiResponse.Loading)

    var reloadUserResponse by mutableStateOf<ApiResponse<Boolean>>(ApiResponse.Loading)
        private set

    val isEmailVerified get() = repo.isEmailVerified

    fun signOut() = viewModelScope.launch {
        repo.signOut()
    }

    fun reloadUser() = viewModelScope.launch {
        reloadUserResponse = ApiResponse.Loading
        repo.reloadUser().collect {
            when (it) {
                is ApiResponse.Success -> {
                    reloadUserResponse = it
                }
                else -> {}
            }
        }
    }

    fun getAuthState() = repo.getAuthState().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        repo.isUserAuthenticatedInFirebase
    )

    fun signInWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {
        signInResponse = ApiResponse.Loading
        repo.signInWithEmailAndPassword(email, password).collect {
            when (it) {
                is ApiResponse.Success -> {
                    signInResponse = it
                }
                else -> {}
            }
        }
    }

    fun signUpWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {
        signUpResponse = ApiResponse.Loading
        repo.signUpWithEmailAndPassword(email, password).collect {
            signUpResponse = it
        }
    }

    fun sendEmailVerification() = viewModelScope.launch {
        sendEmailVerificationResponse = ApiResponse.Loading
        repo.sendEmailVerification().collect {
            when (it) {
                is ApiResponse.Success -> {
                    sendEmailVerificationResponse = it
                }
                else -> {}
            }
        }
    }

    fun sendPasswordResetEmail(email: String) = viewModelScope.launch {
        sendPasswordResetEmailResponse = ApiResponse.Loading
        repo.sendPasswordResetEmail(email).collect {
            when (it) {
                is ApiResponse.Success -> {
                    sendPasswordResetEmailResponse = it
                }
                else -> {}
            }
        }
    }
}

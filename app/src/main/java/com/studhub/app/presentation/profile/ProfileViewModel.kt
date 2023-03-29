package com.studhub.app.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.User
import com.studhub.app.domain.usecase.user.GetCurrentUser
import com.studhub.app.domain.usecase.user.SignOut
import com.studhub.app.domain.usecase.user.UpdateCurrentUserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val _signOut: SignOut,
    private val getCurrentUser: GetCurrentUser,
    private val updateCurrentUserInfo: UpdateCurrentUserInfo
) : ViewModel() {
    var signOutResponse by mutableStateOf<ApiResponse<Boolean>>(ApiResponse.Loading)
        private set

    var currentUser by mutableStateOf<ApiResponse<User>>(ApiResponse.Loading)
        private set

    init {
        getLoggedInUser()
    }

    private fun getLoggedInUser() =
        viewModelScope.launch {
            getCurrentUser().collect {
                currentUser = it
            }
        }

    fun updateUserInfo(updatedUserInfo: User) =
        viewModelScope.launch {
            updateCurrentUserInfo(updatedUserInfo).collect {
                currentUser = it
            }
        }


    fun signOut() = viewModelScope.launch {
        signOutResponse = ApiResponse.Loading
        _signOut().collect {
            when (it) {
                is ApiResponse.Loading -> {}
                is ApiResponse.Failure -> {
                    /** HANDLE ERROR */
                }
                is ApiResponse.Success -> {
                    signOutResponse = it
                }
            }
        }
    }
}

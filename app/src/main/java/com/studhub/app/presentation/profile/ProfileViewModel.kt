package com.studhub.app.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.usecase.user.GetCurrentUser
import com.studhub.app.domain.usecase.user.GetFavoriteListings
import com.studhub.app.domain.usecase.user.SignOut
import com.studhub.app.domain.usecase.user.UpdateCurrentUserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val _signOut: SignOut,
    private val getCurrentUser: GetCurrentUser,
    private val updateCurrentUserInfo: UpdateCurrentUserInfo,
    private val getFavoriteListings: GetFavoriteListings
) : ViewModel() {
    var signOutResponse by mutableStateOf<ApiResponse<Boolean>>(ApiResponse.Loading)
        private set

    var currentUser by mutableStateOf<ApiResponse<User>>(ApiResponse.Loading)
        private set

    private val _userFavorites = MutableSharedFlow<List<Listing>>(replay = 0)
    val userFavorites: SharedFlow<List<Listing>> = _userFavorites

    init {
        getLoggedInUser()
    }

    private fun getLoggedInUser() =
        viewModelScope.launch {
            getCurrentUser().collect {
                currentUser = it
            }
        }

    fun updateUserInfo(updatedUserInfo: User, callback: () -> Unit) =
        viewModelScope.launch {
            updateCurrentUserInfo(updatedUserInfo).collect {
                currentUser = it
                if (it is ApiResponse.Success)
                    callback()
            }
        }

    fun getFavorites() = viewModelScope.launch {
        getFavoriteListings().collect {
            when (it) {
                is ApiResponse.Success -> _userFavorites.emit(it.data)
                else -> _userFavorites.emit(emptyList())
            }
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

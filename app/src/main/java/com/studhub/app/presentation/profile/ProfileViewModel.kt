package com.studhub.app.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.User
import com.studhub.app.domain.usecase.listing.GetOwnListings
import com.studhub.app.domain.usecase.user.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val _signOut: SignOut,
    private val getCurrentUser: GetCurrentUser,
    private val updateCurrentUserInfo: UpdateCurrentUserInfo,
    private val getFavoriteListings: GetFavoriteListings,
    private val _getOwnListings: GetOwnListings,
    private val getBlockedUsers: GetBlockedUsers,
    private val addBlockedUser: AddBlockedUser,
    private val unblockUser: UnblockUser
) : ViewModel() {
    var signOutResponse by mutableStateOf<ApiResponse<Boolean>>(ApiResponse.Loading)
        private set

    var currentUser by mutableStateOf<ApiResponse<User>>(ApiResponse.Loading)
        private set

    var ownListings by mutableStateOf<ApiResponse<List<Listing>>>(ApiResponse.Loading)
        private set

    private val _userFavorites = MutableSharedFlow<List<Listing>>(replay = 0)
    val userFavorites: SharedFlow<List<Listing>> = _userFavorites

    private val _blockedUsers = MutableSharedFlow<List<User>>(replay = 0)
    val blockedUsers: SharedFlow<List<User>> = _blockedUsers


    init {
        getLoggedInUser()
    }

    private fun getLoggedInUser() =
        viewModelScope.launch {
            getCurrentUser().collect {
                currentUser = it
            }
        }

    fun getOwnListings() =
        viewModelScope.launch {
            _getOwnListings().collect {
                ownListings = it
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

    fun getBlocked() = viewModelScope.launch {
        getBlockedUsers().collect {
            when (it) {
                is ApiResponse.Success -> _blockedUsers.emit(it.data)
                else -> _blockedUsers.emit(emptyList())
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
